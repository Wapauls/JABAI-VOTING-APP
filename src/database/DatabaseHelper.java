package database;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.*;

public class DatabaseHelper {
    private static final File CANDIDATES_FILE = new File("database/candidates.txt");
    private static final File VOTES_FILE = new File("database/votes.txt");
    private static final File VOTERS_FILE = new File("database/voters.txt");

    static {
        try {
            File dir = CANDIDATES_FILE.getParentFile();
            if (!dir.exists()) dir.mkdirs();

            if (!CANDIDATES_FILE.exists()) {
                CANDIDATES_FILE.createNewFile();
                List<Candidate> samples = Arrays.asList(
                    new Candidate("Juan E. Dela Cruz", "President", "BSCS", "3rd", "A",
                                  "Position: President\nPlatform: Academic Excellence"),
                    new Candidate("Jack N. Jill", "Vice President", "BSHM", "2nd", "B",
                                  "Position: Vice President\nPlatform: Student Welfare"),
                    new Candidate("Mang E. juan", "Secretary", "BSED", "4th", "C",
                                  "Position: Secretary\nPlatform: Campus Development")
                );
                try (BufferedWriter w = new BufferedWriter(new FileWriter(CANDIDATES_FILE))) {
                    for (Candidate c : samples) {
                        w.write(c.toLine());
                        w.write(System.lineSeparator());
                    }
                }
            }

            if (!VOTES_FILE.exists()) {
                VOTES_FILE.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Candidate> readCandidates() {
        List<Candidate> out = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(CANDIDATES_FILE))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                out.add(Candidate.fromLine(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static void appendCandidate(Candidate c) {
        try {
            String line = c.toLine() + System.lineSeparator();
            Files.write(CANDIDATES_FILE.toPath(), line.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void recordVote(Vote v) {
        try {
            String line = v.toLine() + System.lineSeparator();
            Files.write(VOTES_FILE.toPath(), line.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Integer> countVotes() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        try (BufferedReader r = new BufferedReader(new FileReader(VOTES_FILE))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                Vote v = Vote.fromLine(line);
                counts.put(v.candidate, counts.getOrDefault(v.candidate, 0) + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return counts;
    }

    public static List<Vote> readVotes() {
        List<Vote> out = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(VOTES_FILE))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                out.add(Vote.fromLine(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static Vote makeVote(String candidate, String position, String course, String year, String section) {
        return new Vote(Instant.now().toString(), candidate == null ? "" : candidate, position == null ? "" : position,
                        course == null ? "" : course, year == null ? "" : year, section == null ? "" : section);
    }

    /**
     * Update a candidate record by matching the old name. Returns true if an update occurred.
     */
    public static boolean updateCandidate(String oldName, Candidate newCandidate) {
        List<Candidate> list = readCandidates();
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            Candidate c = list.get(i);
            if (c.name.equals(oldName)) {
                list.set(i, newCandidate);
                found = true;
                break;
            }
        }

        if (!found) return false;

        // write entire file back
        try (BufferedWriter w = new BufferedWriter(new FileWriter(CANDIDATES_FILE, false))) {
            for (Candidate c : list) {
                w.write(c.toLine());
                w.write(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Delete a candidate record by name. Returns true if a deletion occurred.
     */
    public static boolean deleteCandidate(String name) {
        List<Candidate> list = readCandidates();
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            Candidate c = list.get(i);
            if (c.name.equals(name)) {
                list.remove(i);
                found = true;
                break;
            }
        }

        if (!found) return false;

        // write entire file back
        try (BufferedWriter w = new BufferedWriter(new FileWriter(CANDIDATES_FILE, false))) {
            for (Candidate c : list) {
                w.write(c.toLine());
                w.write(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // ============ VOTER DATABASE METHODS ============

    public static List<Voter> readVoters() {
        List<Voter> out = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(VOTERS_FILE))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                out.add(Voter.fromLine(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static void addVoter(Voter voter) {
        try {
            String line = voter.toLine() + System.lineSeparator();
            Files.write(VOTERS_FILE.toPath(), line.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if a student with this ID has already voted.
     */
    public static boolean hasVoted(String studentID) {
        List<Voter> voters = readVoters();
        for (Voter v : voters) {
            if (v.studentID.equals(studentID)) {
                return v.hasVoted;
            }
        }
        return false;
    }

    /**
     * Mark a voter as having voted and record the timestamp.
     */
    public static boolean markVoterAsVoted(String studentID) {
        List<Voter> voters = readVoters();
        boolean found = false;
        for (Voter v : voters) {
            if (v.studentID.equals(studentID)) {
                v.hasVoted = true;
                v.voteTimestamp = Instant.now().toString();
                found = true;
                break;
            }
        }

        if (!found) return false;

        // write entire file back
        try (BufferedWriter w = new BufferedWriter(new FileWriter(VOTERS_FILE, false))) {
            for (Voter v : voters) {
                w.write(v.toLine());
                w.write(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Get a voter by student ID.
     */
    public static Voter getVoterByID(String studentID) {
        List<Voter> voters = readVoters();
        for (Voter v : voters) {
            if (v.studentID.equals(studentID)) {
                return v;
            }
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
        List<Voter> voters = readVoters();
        int totalVoters = voters.size();
        int votedCount = 0;
        for (Voter v : voters) {
            if (v.hasVoted) votedCount++;
        }
        Map<String, Integer> stats = new LinkedHashMap<>();
        stats.put("total", totalVoters);
        stats.put("voted", votedCount);
        stats.put("notVoted", totalVoters - votedCount);
        return stats;
    }
}
