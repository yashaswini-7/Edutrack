package edutrack.core;

public class CustomIntArray {
    private int[] data;
    private int size;

    public CustomIntArray() {
        this(10);
    }

    public CustomIntArray(int initialCapacity) {
        data = new int[initialCapacity > 0 ? initialCapacity : 10];
        size = 0;
    }

    public void add(int val) {
        if (size == data.length) {
            resize(data.length * 2);
        }
        data[size++] = val;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return data[index];
    }

    public void set(int index, int val) {
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

    public int removeLast() {
        if (size == 0) throw new IllegalStateException("Array is empty");
        return data[--size];
    }

    public boolean contains(int val) {
        for (int i = 0; i < size; i++) {
            if (data[i] == val) return true;
        }
        return false;
    }

    public int[] toArray() {
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = data[i];
        }
        return result;
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
        int[] next = new int[newCapacity];
        for (int i = 0; i < size; i++) {
            next[i] = data[i];
        }
        data = next;
    }
}
