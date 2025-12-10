package database;

public class Voter {
    public String studentID;
    public String name;
    public String email;
    public String course;
    public String year;
    public String section;
    public boolean hasVoted;
    public String voteTimestamp; // timestamp of when they voted, empty if not voted

    public Voter(String studentID, String name, String email, String course, String year, String section, boolean hasVoted, String voteTimestamp) {
        this.studentID = studentID;
        this.name = name;
        this.email = email;
        this.course = course;
        this.year = year;
        this.section = section;
        this.hasVoted = hasVoted;
        this.voteTimestamp = voteTimestamp == null ? "" : voteTimestamp;
    }

    public static Voter fromLine(String line) {
        String[] p = line.split("\\|", -1);
        String studentID = p.length > 0 ? p[0] : "";
        String name = p.length > 1 ? p[1] : "";
        String email = p.length > 2 ? p[2] : "";
        String course = p.length > 3 ? p[3] : "";
        String year = p.length > 4 ? p[4] : "";
        String section = p.length > 5 ? p[5] : "";
        boolean hasVoted = p.length > 6 ? Boolean.parseBoolean(p[6]) : false;
        String voteTimestamp = p.length > 7 ? p[7] : "";
        return new Voter(studentID, name, email, course, year, section, hasVoted, voteTimestamp);
    }

    public String toLine() {
        return String.join("|", studentID, name, email, course, year, section, String.valueOf(hasVoted), voteTimestamp);
    }
}
