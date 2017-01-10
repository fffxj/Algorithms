import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;

public class ResizingArrayStack<Item> implements Iterable<Item> {
    private Item[] a;
    private int n;
    
    public ResizingArrayStack() {
        a = (Item[]) new Object[2];
        n = 0;
    }
    public boolean isEmpty() {
        return n == 0;
    }
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0;  i < n; ++i) {
            copy[i] = a[i];
        }
        a = copy;
    }
    public void push(Item item) {
        if (n == a.length) resize(2*a.length);
        a[n++] = item;
    }
    public Item pop() {
        Item item = a[--n];
        a[n] = null;
        if (n > 0 && n == a.length/4) resize(a.length/2);
        return item;
    }    

    public Iterator<Item> iterator() {
        return new ResizingArrayIterator();
    }
    private class ResizingArrayIterator implements Iterator<Item> {
        private int i;

        public ResizingArrayIterator() { i = n; }
        public boolean hasNext() { return i > 0; }
        public void remove() {}
        public Item next() {
            return a[--i];
        }
    }

    public static void main(String[] args) {
        ResizingArrayStack<String> stack = new ResizingArrayStack<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (s.equals("-")) StdOut.print(stack.pop() + " ");
            else               stack.push(s);
        }
    }
}
