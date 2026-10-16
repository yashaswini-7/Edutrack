package edutrack.core;

public class CustomMatchList {
    private TextMatch[] data;
    private int size;

    public CustomMatchList() {
        this(16);
    }

    public CustomMatchList(int initialCap) {
        data = new TextMatch[initialCap > 0 ? initialCap : 16];
        size = 0;
    }

    public void add(TextMatch match) {
        if (size == data.length) {
            TextMatch[] next = new TextMatch[data.length * 2];
            for (int i = 0; i < size; i++) next[i] = data[i];
            data = next;
        }
        data[size++] = match;
    }

    public TextMatch get(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);
        }
        return data[i];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
    }
}
