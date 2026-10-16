package edutrack.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataLoader {

    public static Student[] load(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            String first = br.readLine();
            if (first == null) throw new IOException("Dataset is empty: " + path);
            String[] header = parseLine(first);
            Student.setHeader(header);

            Student[] arr = new Student[64];
            int count = 0;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] fields = parseLine(line);
                if (fields.length != header.length) continue; 

                if (count == arr.length) {
                    Student[] next = new Student[count * 2];
                    for (int i = 0; i < count; i++) next[i] = arr[i];
                    arr = next;
                }
                arr[count] = new Student(String.format("S%03d", count + 1), fields);
                count++;
            }

            Student[] result = new Student[count];
            for (int i = 0; i < count; i++) result[i] = arr[i];
            return result;
        } finally {
            br.close();
        }
    }

    public static String loadCorpusText(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    public static String[] parseLine(String line) {
        CustomStringArray out = new CustomStringArray(35);
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                out.add(cur.toString().trim());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        out.add(cur.toString().trim());
        return out.toArray();
    }
}
