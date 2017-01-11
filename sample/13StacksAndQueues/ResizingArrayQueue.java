import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;

public class ResizingArrayQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int first;
    private int last;
    private int n;

    public ResizingArrayQueue() {
        q = (Item[]) new Object[2];
        first = 0;
        last = 0;
        n = 0;
    }
    public boolean isEmpty() {
        return n == 0;
    }
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; ++i) {
            copy[i] = q[(first + i) % q.length];
        }
        q = copy;
        first = 0;
        last = n;
    }
    public void enqueue(Item item) {
        if (n == q.length) resize(2*q.length);
        q[last++] = item;
        if (last == q.length) last = 0;
        n++;
    }
    public Item dequeue() {
        Item item = q[first];
        q[first] = null;
        first++;
        n--;
        if (first == q.length) first = 0;
        if (n > 0 && n == q.length/4) resize(q.length/2);
        return item;
    }

    public Iterator<Item> iterator() {
        return new ResizingArrayIterator();
    }
    private class ResizingArrayIterator implements Iterator<Item> {
        private int i;

        public ResizingArrayIterator() { i = 0; }
        public boolean hasNext() { return i < n; }
        public void remove() {}
        public Item next() {
            Item item = q[(first + i) % q.length];
            i++;
            return item;
        }
    }

    public static void main(String[] args) {
        ResizingArrayQueue<String> queue = new ResizingArrayQueue<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (s.equals("-")) StdOut.print(queue.dequeue() + " ");
            else               queue.enqueue(s);
        }
    }
}
