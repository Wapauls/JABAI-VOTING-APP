package utils;

/**
 * Utility class for normalizing user inputs (course codes and year levels)
 * This ensures consistent data entry across the application
 */
public class NormalizationHelper {
    
    /**
     * Normalize course input: cs/bscs -> BSCS, CS -> BSCS, etc.
     * Maps common variations to standard course codes
     */
    public static String normalizeCourse(String course) {
        if (course == null || course.isEmpty()) {
            return course;
        }
        String upper = course.toUpperCase();
        // Map common variations to standard course codes
        if (upper.equals("CS") || upper.equals("BSCS")) {
            return "BSCS";
        } else if (upper.equals("HM") || upper.equals("BSHM")) {
            return "BSHM";
        } else if (upper.equals("POLS") || upper.equals("BAPOLS")) {
            return "BAPOLS";
        } else if (upper.equals("TM") || upper.equals("BSTM")) {
            return "BSTM";
        } else if (upper.equals("BA") || upper.equals("BSBA")) {
            return "BSBA";
        } else if (upper.equals("ED") || upper.equals("BSED")) {
            return "BSED";
        }
        // If it already starts with BS or BA, return as is
        if (upper.startsWith("BS") || upper.startsWith("BA")) {
            return upper;
        }
        // Otherwise return uppercase
        return upper;
    }
    
    /**
     * Normalize year input: 1 -> 1st, 2 -> 2nd, etc.
     * Also handles inputs like "1st", "2nd" and normalizes them
     */
    public static String normalizeYear(String year) {
        if (year == null || year.isEmpty()) {
            return year;
        }
        String trimmed = year.trim();
        // If it's just a number, convert to ordinal
        if (trimmed.matches("^\\d+$")) {
            int num = Integer.parseInt(trimmed);
            if (num >= 1 && num <= 4) {
                switch (num) {
                    case 1: return "1st";
                    case 2: return "2nd";
                    case 3: return "3rd";
                    case 4: return "4th";
                }
            }
        }
        // If it already has ordinal suffix, return as is (normalize case)
        if (trimmed.matches("^\\d+(st|nd|rd|th)$")) {
            return trimmed.toLowerCase();
        }
        return trimmed;
    }
}
