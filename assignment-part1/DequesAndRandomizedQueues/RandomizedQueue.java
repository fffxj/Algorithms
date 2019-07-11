import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] array;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        size = 0;
        array = (Item[]) new Object[size+1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; ++i)
            copy[i] = array[i];
        array = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (size == array.length) resize(array.length*2);
        array[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        if (size > 0 && size == array.length/4) resize(array.length/2);
        int index = StdRandom.uniform(size);
        Item item = array[index];
        array[index] = array[size-1];
        array[size-1] = null;
        --size;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniform(size);
        return array[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        Item[] copy;
        int index;

        public RandomizedQueueIterator() {
            index = -1;
            copy = (Item[]) new Object[size];
            for (int i = 0; i < size; ++i)
                copy[i] = array[i];
            StdRandom.shuffle(copy);
        }

        public boolean hasNext() {
            return index < size - 1;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = copy[++index];
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rdqueue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if      (s.equals("?"))  StdOut.println(rdqueue.sample());
            else if (s.equals("-"))  StdOut.println(rdqueue.dequeue());
            else                     rdqueue.enqueue(s);
            for (String v : rdqueue) StdOut.print(v + " ");
            StdOut.println();
        }
    }
}
