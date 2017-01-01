import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class LinkedStackOfStrings {
    private Node first = null;
    private class Node {
        String item;
        Node next;
    }

    public boolean isEmpty() {
        return first == null;
    }
    public void push(String item) {
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
    }
    public String pop() {
        String item = first.item;
        first = first.next;
        return item;
    }

    public static void main(String[] args) {
        LinkedStackOfStrings stack = new LinkedStackOfStrings();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (s.equals("-")) StdOut.print(stack.pop() + " ");
            else               stack.push(s);
        }
    }
}
