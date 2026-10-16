package edutrack.core;

public class CustomIntQueue {
    private int[] data;
    private int front;
    private int rear;
    private int count;

    public CustomIntQueue() {
        this(16);
    }

    public CustomIntQueue(int capacity) {
        data = new int[capacity > 0 ? capacity : 16];
        front = 0;
        rear = 0;
        count = 0;
    }

    public void enqueue(int val) {
        if (count == data.length) {
            resize(data.length * 2);
        }
        data[rear] = val;
        rear = (rear + 1) % data.length;
        count++;
    }

    public int dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        int val = data[front];
        front = (front + 1) % data.length;
        count--;
        return val;
    }

    public int peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return data[front];
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    public void clear() {
        front = 0;
        rear = 0;
        count = 0;
    }

    private void resize(int newCap) {
        int[] next = new int[newCap];
        for (int i = 0; i < count; i++) {
            next[i] = data[(front + i) % data.length];
        }
        data = next;
        front = 0;
        rear = count;
    }
}
