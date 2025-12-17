package connectionDB;

import java.sql.*;
import java.time.Instant;
import java.util.*;
import java.io.File;

public class DatabaseHelper {
    
    // Database URL - relative path (recommended)
    private static final String DB_URL = "jdbc:sqlite:database/voting.db";
    
    // Static block to load driver once
    static {
        try {
            // Explicitly load the SQLite driver
            Class.forName("org.sqlite.JDBC");
            System.out.println("✅ SQLite JDBC Driver loaded successfully");
            
            // Ensure database directory exists
            File dbDir = new File("database");
            if (!dbDir.exists()) {
                dbDir.mkdirs();
                System.out.println("📁 Created database directory: " + dbDir.getAbsolutePath());
            }
            
            // Initialize database tables
            initializeDatabase();
            System.out.println("✨ Database initialized successfully");
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Failed to load SQLite JDBC driver");
            System.err.println("Make sure sqlite-jdbc-3.51.1.0.jar is in your classpath");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Failed to initialize database");
            e.printStackTrace();
        }
    }

    /**
     * Get database connection
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Get connection - driver is already loaded
            Connection conn = DriverManager.getConnection(DB_URL);
            
            // Enable foreign keys
            conn.createStatement().execute("PRAGMA foreign_keys = ON");
            
            return conn;
            
        } catch (SQLException e) {
            System.err.println("❌ Failed to connect to database: " + e.getMessage());
            System.err.println("Database URL: " + DB_URL);
            System.err.println("Working directory: " + System.getProperty("user.dir"));
            throw e;
        }
    }

    /**
     * Initialize database tables if they don't exist
     */
    private static void initializeDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Enable foreign keys
            stmt.execute("PRAGMA foreign_keys = ON");

            // Create candidates table
            String createCandidates = "CREATE TABLE IF NOT EXISTS candidates (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL UNIQUE," +
                    "position TEXT NOT NULL," +
                    "course TEXT," +
                    "year TEXT," +
                    "section TEXT," +
                    "description TEXT" +
                    ")";
            stmt.execute(createCandidates);

            // Create votes table
            String createVotes = "CREATE TABLE IF NOT EXISTS votes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "timestamp TEXT NOT NULL," +
                    "candidate TEXT NOT NULL," +
                    "position TEXT," +
                    "course TEXT," +
                    "year TEXT," +
                    "section TEXT," +
                    "studentID TEXT" +
                    ")";
            stmt.execute(createVotes);
            
            // Best-effort migration: ensure studentID column exists on existing databases
            try {
                stmt.execute("ALTER TABLE votes ADD COLUMN studentID TEXT");
            } catch (SQLException ignore) {
                // Column already exists – safe to ignore
            }

            // Create voters table
            String createVoters = "CREATE TABLE IF NOT EXISTS voters (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "studentID TEXT NOT NULL UNIQUE," +
                    "name TEXT NOT NULL," +
                    "email TEXT," +
                    "course TEXT," +
                    "year TEXT," +
                    "section TEXT," +
                    "hasVoted INTEGER DEFAULT 0," +
                    "voteTimestamp TEXT" +
                    ")";
            stmt.execute(createVoters);

            System.out.println("✅ Database tables created/verified successfully");
        }
    }

    // ============ CANDIDATE DATABASE METHODS ============

    public static List<Candidate> readCandidates() {
        List<Candidate> out = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, position, course, year, section, description FROM candidates")) {

            while (rs.next()) {
                Candidate c = new Candidate(
                    rs.getString("name"),
                    rs.getString("position"),
                    rs.getString("course"),
                    rs.getString("year"),
                    rs.getString("section"),
                    rs.getString("description")
                );
                out.add(c);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error reading candidates: " + e.getMessage());
            e.printStackTrace();
        }
        return out;
    }

    public static void appendCandidate(Candidate c) {
        String query = "INSERT INTO candidates (name, position, course, year, section, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, c.name);
            pstmt.setString(2, c.position);
            pstmt.setString(3, c.course);
            pstmt.setString(4, c.year);
            pstmt.setString(5, c.section);
            pstmt.setString(6, c.description);
            pstmt.executeUpdate();
            System.out.println("✅ Candidate added: " + c.name);
        } catch (SQLException e) {
            System.err.println("❌ Error adding candidate: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Update a candidate record by matching the old name. Returns true if an update occurred.
     */
    public static boolean updateCandidate(String oldName, Candidate newCandidate) {
        String query = "UPDATE candidates SET name = ?, position = ?, course = ?, year = ?, section = ?, description = ? WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, newCandidate.name);
            pstmt.setString(2, newCandidate.position);
            pstmt.setString(3, newCandidate.course);
            pstmt.setString(4, newCandidate.year);
            pstmt.setString(5, newCandidate.section);
            pstmt.setString(6, newCandidate.description);
            pstmt.setString(7, oldName);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Candidate updated: " + oldName + " → " + newCandidate.name);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating candidate: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a candidate record by name. Returns true if a deletion occurred.
     */
    public static boolean deleteCandidate(String name) {
        String deleteVotes = "DELETE FROM votes WHERE candidate = ?";
        String query = "DELETE FROM candidates WHERE name = ?";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement voteStmt = conn.prepareStatement(deleteVotes);
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                voteStmt.setString(1, name);
                voteStmt.executeUpdate();

                pstmt.setString(1, name);
                int rowsAffected = pstmt.executeUpdate();
                conn.commit();
                if (rowsAffected > 0) {
                    System.out.println("✅ Candidate deleted: " + name + " (votes removed)");
                }
                return rowsAffected > 0;
            } catch (SQLException inner) {
                conn.rollback();
                throw inner;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error deleting candidate: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    // ============ VOTE DATABASE METHODS ============

    public static void recordVote(Vote v) {
        String query = "INSERT INTO votes (timestamp, candidate, position, course, year, section, studentID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, v.timestamp);
            pstmt.setString(2, v.candidate);
            pstmt.setString(3, v.position);
            pstmt.setString(4, v.course);
            pstmt.setString(5, v.year);
            pstmt.setString(6, v.section);
            pstmt.setString(7, v.studentID);
            pstmt.executeUpdate();
            System.out.println("✅ Vote recorded for: " + v.candidate);
        } catch (SQLException e) {
            System.err.println("❌ Error recording vote: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Vote> readVotes() {
        List<Vote> out = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT timestamp, candidate, position, course, year, section, studentID " +
                 "FROM votes v WHERE EXISTS (SELECT 1 FROM candidates c WHERE c.name = v.candidate)"
             )) {

            while (rs.next()) {
                Vote v = new Vote(
                    rs.getString("timestamp"),
                    rs.getString("candidate"),
                    rs.getString("position"),
                    rs.getString("course"),
                    rs.getString("year"),
                    rs.getString("section"),
                    rs.getString("studentID")
                );
                out.add(v);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error reading votes: " + e.getMessage());
            e.printStackTrace();
        }
        return out;
    }

    public static Map<String, Integer> countVotes() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT candidate, COUNT(*) as count FROM votes GROUP BY candidate")) {

            while (rs.next()) {
                counts.put(rs.getString("candidate"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error counting votes: " + e.getMessage());
            e.printStackTrace();
        }
        return counts;
    }

    public static Vote makeVote(String candidate, String position, String course, String year, String section) {
        return new Vote(Instant.now().toString(),
                        candidate == null ? "" : candidate,
                        position == null ? "" : position,
                        course == null ? "" : course,
                        year == null ? "" : year,
                        section == null ? "" : section,
                        null);
    }

    /**
     * Get all votes for a specific student.
     */
    public static List<Vote> readVotesForStudent(String studentID) {
        List<Vote> out = new ArrayList<>();
        String sql = "SELECT timestamp, candidate, position, course, year, section, studentID " +
                     "FROM votes v WHERE studentID = ? " +
                     "AND EXISTS (SELECT 1 FROM candidates c WHERE c.name = v.candidate)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Vote v = new Vote(
                        rs.getString("timestamp"),
                        rs.getString("candidate"),
                        rs.getString("position"),
                        rs.getString("course"),
                        rs.getString("year"),
                        rs.getString("section"),
                        rs.getString("studentID")
                    );
                    out.add(v);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error reading votes for student: " + e.getMessage());
            e.printStackTrace();
        }
        return out;
    }

    /**
     * Check if a student has already voted for a specific candidate/position.
     */
    public static boolean hasUserVotedForCandidate(String studentID, String candidate, String position) {
        String sql = "SELECT COUNT(*) FROM votes WHERE studentID = ? AND candidate = ? AND position = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentID);
            pstmt.setString(2, candidate);
            pstmt.setString(3, position);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error checking duplicate vote: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ============ VOTER DATABASE METHODS ============

    public static List<Voter> readVoters() {
        List<Voter> out = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT studentID, name, email, course, year, section, hasVoted, voteTimestamp FROM voters")) {

            while (rs.next()) {
                Voter v = new Voter(
                    rs.getString("studentID"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("course"),
                    rs.getString("year"),
                    rs.getString("section"),
                    rs.getInt("hasVoted") == 1,
                    rs.getString("voteTimestamp")
                );
                out.add(v);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error reading voters: " + e.getMessage());
            e.printStackTrace();
        }
        return out;
    }

    public static void addVoter(Voter voter) {
        String query = "INSERT INTO voters (studentID, name, email, course, year, section, hasVoted, voteTimestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, voter.studentID);
            pstmt.setString(2, voter.name);
            pstmt.setString(3, voter.email);
            pstmt.setString(4, voter.course);
            pstmt.setString(5, voter.year);
            pstmt.setString(6, voter.section);
            pstmt.setInt(7, voter.hasVoted ? 1 : 0);
            pstmt.setString(8, voter.voteTimestamp);
            pstmt.executeUpdate();
            System.out.println("✅ Voter added: " + voter.name + " (" + voter.studentID + ")");
        } catch (SQLException e) {
            System.err.println("❌ Error adding voter: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Check if a student with this ID has already voted.
     */
    public static boolean hasVoted(String studentID) {
        String query = "SELECT hasVoted FROM voters WHERE studentID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, studentID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("hasVoted") == 1;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error checking voter status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Mark a voter as having voted and record the timestamp.
     */
    public static boolean markVoterAsVoted(String studentID) {
        String query = "UPDATE voters SET hasVoted = 1, voteTimestamp = ? WHERE studentID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, Instant.now().toString());
            pstmt.setString(2, studentID);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Voter marked as voted: " + studentID);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error marking voter as voted: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get a voter by student ID.
     */
    public static Voter getVoterByID(String studentID) {
        String query = "SELECT studentID, name, email, course, year, section, hasVoted, voteTimestamp FROM voters WHERE studentID = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, studentID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Voter(
                        rs.getString("studentID"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("course"),
                        rs.getString("year"),
                        rs.getString("section"),
                        rs.getInt("hasVoted") == 1,
                        rs.getString("voteTimestamp")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error getting voter by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Check if a student ID already exists in the voter database.
     */
    public static boolean voterExists(String studentID) {
        return getVoterByID(studentID) != null;
    }

    /**
     * Get count of voters who have voted vs total voters.
     */
    public static Map<String, Integer> getVoterStats() {
        Map<String, Integer> stats = new LinkedHashMap<>();
        String query = "SELECT COUNT(*) as total, SUM(CASE WHEN hasVoted = 1 THEN 1 ELSE 0 END) as voted FROM voters";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                int total = rs.getInt("total");
                int voted = rs.getInt("voted");
                stats.put("total", total);
                stats.put("voted", voted);
                stats.put("notVoted", total - voted);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error getting voter statistics: " + e.getMessage());
            e.printStackTrace();
        }
        return stats;
    }
    
    /**
     * Test database connection
     */
    public static void testConnection() {
        System.out.println("\n🔧 Testing database connection...");
        System.out.println("Database URL: " + DB_URL);
        System.out.println("Working directory: " + System.getProperty("user.dir"));
        
        try (Connection conn = getConnection()) {
            System.out.println("✅ Connection test SUCCESSFUL!");
            
            // Test with a simple query
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT sqlite_version() AS version")) {
                if (rs.next()) {
                    System.out.println("📊 SQLite version: " + rs.getString("version"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Connection test FAILED!");
            e.printStackTrace();
        }
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        System.out.println("🚀 Starting DatabaseHelper test...\n");
        
        // Test connection
        testConnection();
        
        System.out.println("\n✨ DatabaseHelper test completed!");
    }
}
