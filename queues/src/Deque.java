import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int n;          //number of elements in deque
    private Node first;
    private Node last;

    private class Node{
        private Item item;
        private Node next;
        private Node prev;
    }

    // construct an empty deque
    public Deque(){
        first = null;
        last = null;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty(){
        return n == 0;
    }

    // return the number of items on the deque
    public int size(){
        return n;
    }

    // add the item to the front
    public void addFirst(Item item){
        if(item == null) throw new IllegalArgumentException("Cannot add a null item the deque.");
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.prev = null;
        if(!isEmpty())  oldFirst.prev = first;
        else            last = first;
        n++;
    }

    // add the item to the back
    public void addLast(Item item){
        if(item == null) throw new IllegalArgumentException("Cannot add a null item the deque.");
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldLast;
        if(isEmpty())   first = last;
        else            oldLast.next = last;
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst(){
        if(isEmpty()) throw new NoSuchElementException("Stack Underflow. Deque is empty.");
        Item item = first.item;     // save first's item to return.
        first = first.next;
        if(first != null) first.prev = null;
        if(this.n == 1) last = null;
        n--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast(){
        if(isEmpty()) throw new NoSuchElementException("Stack Underflow. Deque is empty.");
        Item item = last.item;      // save last's item to return.
        last = last.prev;
        if(last != null) last.next = null;
        if(this.n == 1) first = null;
        n--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator(){
        return new LinkedIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class LinkedIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        StdOut.println("Testing front add and remove ops.");
        // test adding and removing from the front.
        Deque<String> deq1 = new Deque<String>();
        Deque<String> deq2 = new Deque<String>();
        while(!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) {
                deq1.addFirst(item);
                deq2.addLast(item);
            }
            else if (!deq1.isEmpty()) {
                StdOut.print(deq1.removeFirst() + " ");
                StdOut.print(deq2.removeLast() + " ");
            }
        }
        StdOut.println("\n");
        StdOut.println("(" + deq1.size() + " left on deque 1)");
        StdOut.println("(" + deq1.size() + " left on deque 2)");

        // Use the iterator to display the contents of deque 1 and deque 2.
        StdOut.println("Testing iterator 1 of deque 1.");
        Iterator<String> it1 =  deq1.iterator();
        for (String s: deq1){
            StdOut.println(s + " ");
        }

        // Use the iterator to display the contents of the deque.
        StdOut.println("Testing iterator 2 of the deque 2.");
        Iterator<String> it2 =  deq2.iterator();
        for (String s: deq2){
            StdOut.println(s + " ");
        }
    }
}

//javac -cp "/Volumes/Projects C/Java Projects/Princeton Algorithms I/shared/algs4.jar" Deque.java RandomizedQueue.java Permutation.java
//java -cp ".:/Volumes/Projects C/Java Projects/Princeton Algorithms I/shared/algs4.jar" Deque < algs4-data/tobe.txt