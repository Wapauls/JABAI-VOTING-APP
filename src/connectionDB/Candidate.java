package connectionDB;

public class Candidate {
    public String name;
    public String position;
    public String course;
    public String year;
    public String section;
    public String description;

    public Candidate(String name, String position, String course, String year, String section, String description) {
        this.name = name;
        this.position = position;
        this.course = course;
        this.year = year;
        this.section = section;
        this.description = description;
    }

    public static Candidate fromLine(String line) {
        String[] p = line.split("\\|", -1);
        String name = p.length > 0 ? p[0] : "";
        String position = p.length > 1 ? p[1] : "";
        String course = p.length > 2 ? p[2] : "";
        String year = p.length > 3 ? p[3] : "";
        String section = p.length > 4 ? p[4] : "";
        String description = p.length > 5 ? p[5].replace("<br>", "\n") : "";
        return new Candidate(name, position, course, year, section, description);
    }

    public String toLine() {
        // Use <br> to store newlines safely in a single-line record
        String descSafe = description == null ? "" : description.replace("\n", "<br>");
        return String.join("|", name, position, course, year, section, descSafe);
    }
}
