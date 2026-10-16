package edutrack.core;

public class Student {

    public static String[] header = new String[0];

    public static void setHeader(String[] h) {
        header = h;
    }

    private static int findColumn(String col) {
        for (int i = 0; i < header.length; i++) {
            if (header[i].equals(col)) return i;
        }
        return -1;
    }

    public final String id;
    public final String profile;
    private final String[] values;
    private final int g1;
    private final int g2;
    private final int g3;
    private final int absences;

    public Student(String id, String[] values) {
        this.id = id;
        this.values = values;

        StringBuilder sb = new StringBuilder(";id=").append(id).append(";");
        for (int i = 0; i < header.length; i++) {
            if (i < values.length) {
                sb.append(header[i]).append('=').append(values[i]).append(';');
            }
        }
        this.profile = sb.toString();

        this.g1 = parseNumeric("G1");
        this.g2 = parseNumeric("G2");
        this.g3 = parseNumeric("G3");
        this.absences = parseNumeric("absences");
    }

    public String get(String col) {
        int idx = findColumn(col);
        if (idx >= 0 && idx < values.length) {
            return values[idx];
        }
        return "";
    }

    public int getInt(String col) {
        return parseNumeric(col);
    }

    private int parseNumeric(String col) {
        String val = get(col).trim();
        if (val.isEmpty()) return 0;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int g1() { return g1; }
    public int g2() { return g2; }
    public int g3() { return g3; }
    public int absences() { return absences; }

    @Override
    public String toString() {
        return String.format("%s | School: %s, Sex: %s, Age: %s | Grades [G1=%d, G2=%d, G3=%d] | Failures: %s, StudyTime: %s, Absences: %d",
                id, get("school"), get("sex"), get("age"), g1, g2, g3, get("failures"), get("studytime"), absences);
    }
}
