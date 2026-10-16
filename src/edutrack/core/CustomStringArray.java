package edutrack.core;

public class CustomStringArray {
    private String[] data;
    private int size;

    public CustomStringArray() {
        this(10);
    }

    public CustomStringArray(int initialCapacity) {
        data = new String[initialCapacity > 0 ? initialCapacity : 10];
        size = 0;
    }

    public void add(String val) {
        if (size == data.length) {
            resize(data.length * 2);
        }
        data[size++] = val;
    }

    public String get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return data[index];
    }

    public void set(int index, String val) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        data[index] = val;
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

    public boolean contains(String val) {
        if (val == null) {
            for (int i = 0; i < size; i++) if (data[i] == null) return true;
        } else {
            for (int i = 0; i < size; i++) if (val.equals(data[i])) return true;
        }
        return false;
    }

    public String[] toArray() {
        String[] res = new String[size];
        for (int i = 0; i < size; i++) {
            res[i] = data[i];
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }
        return sb.append("]").toString();
    }

    private void resize(int newCapacity) {
        String[] next = new String[newCapacity];
        for (int i = 0; i < size; i++) {
            next[i] = data[i];
        }
        data = next;
    }
}
