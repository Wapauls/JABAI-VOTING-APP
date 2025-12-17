package connectionDB;

public class Vote {
    public String timestamp; // simple ISO string
    public String candidate;
    public String position;
    public String course;
    public String year;
    public String section;
    public String studentID; // voter who cast this vote (may be null)

    public Vote(String timestamp, String candidate, String position, String course, String year, String section, String studentID) {
        this.timestamp = timestamp;
        this.candidate = candidate;
        this.position = position;
        this.course = course;
        this.year = year;
        this.section = section;
        this.studentID = studentID;
    }

    public static Vote fromLine(String line) {
        String[] p = line.split("\\|", -1);
        String timestamp = p.length > 0 ? p[0] : "";
        String candidate = p.length > 1 ? p[1] : "";
        String position = p.length > 2 ? p[2] : "";
        String course = p.length > 3 ? p[3] : "";
        String year = p.length > 4 ? p[4] : "";
        String section = p.length > 5 ? p[5] : "";
        String studentID = p.length > 6 ? p[6] : "";
        return new Vote(timestamp, candidate, position, course, year, section, studentID);
    }

    public String toLine() {
        return String.join("|", timestamp, candidate, position, course, year, section, studentID == null ? "" : studentID);
    }
}
