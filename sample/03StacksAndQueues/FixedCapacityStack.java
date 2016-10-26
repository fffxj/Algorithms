import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FixedCapacityStack<Item> {
    private Item[] s;
    private int N = 0;

    public FixedCapacityStack(int capacity) {
        s = (Item[]) new Object[capacity];
    }
    public boolean isEmpty() {
        return N == 0;
    }
    public void push(Item item) {
        s[N++] = item;
    }
    public Item pop() {
        Item item = s[--N];
        s[N] = null;
        return item;
    }

    public Iterator<Item> iterator() { return new FixedCapacityStackIterator(); }
    private class FixedCapacityStackIterator implements Iterator<Item> {
        private int i = N;
        public boolean hasNext() {  return i > 0;        }
        public void remove()     {  /* not supported */  }
        public Item next()       {  return s[--i];       }
    }

    public static void main(String[] args) {
        FixedCapacityStack<String> stack = new FixedCapacityStack<String>(100);
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (s.equals("-")) StdOut.print(stack.pop() + " ");
            else               stack.push(s);
        }
    }
}
