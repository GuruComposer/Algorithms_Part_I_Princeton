import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;   //queue of elements
    private int n;      //number of elements in queue

    // construct an empty randomized queue
    public RandomizedQueue(){
        q = (Item[]) new Object[2];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty(){
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size(){
        return n;
    }

    // resize the underlying array
    private void resize(int capacity){
        assert capacity >= n;
        Item[] copy = (Item[]) new Object[capacity];
        for(int i = 0; i < n; i++){
            copy[i] = q[i % q.length];
        }
        q = copy;
    }

    // add the item
    public void enqueue(Item item){
        if(item == null) throw new IllegalArgumentException("Cannot add null item to RandomizedQueue.");
        if(n == q.length) resize(2*q.length);
        q[n] = item;   //add item to end
        n++;
    }

    // remove and return a random item
    public Item dequeue(){
        if(isEmpty()) throw new NoSuchElementException("Queue underflow.");
        int randIndex = StdRandom.uniform(0, n);
        Item item = q[randIndex];
        q[randIndex] = q[n-1];
        q[n-1] = null;
        n--;
        // shrink the array if necessary
        if(n > 0 && n == q.length/4) resize(q.length/2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample(){
        if(isEmpty()) throw new NoSuchElementException("Queue is empty.");
        int randIndex = StdRandom.uniform(0, n);
        return q[randIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator(){
        return new RandomQueueIterator();
    }

    private class RandomQueueIterator implements Iterator<Item> {
        private Item[] items;
        private int current;

        public RandomQueueIterator(){
            items = (Item[]) new Object[n];
            current = 0;
            for(int i = 0; i < n; i ++){
                items[i] = q[i];
            }
            StdRandom.shuffle(items);
        }

        public boolean hasNext() { return current < n; }

        public void remove() { throw new UnsupportedOperationException();}

        public Item next(){
            if(!hasNext()) throw new NoSuchElementException();
            Item item = items[current % items.length];
            current++;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args){
        // test adding and removing from RandomizedQueue.
        RandomizedQueue<String> randQ = new RandomizedQueue<String>();
        while(!StdIn.isEmpty()){
            String item = StdIn.readString();
            if (!item.equals("-"))
                randQ.enqueue(item);
            else if (!randQ.isEmpty())
                StdOut.print(randQ.dequeue() + " ");
        }
        StdOut.println("(" + randQ.size() + " left on RandomizedQueue)");

        // Use the iterator to display the contents of the RandomizedQueue.
        StdOut.println("Testing iterator of the RandomizedQueue.");
        Iterator<String> it1 =  randQ.iterator();
        for (String s: randQ){
            StdOut.println(s + " ");
        }
        StdOut.println("Random Sample: " + randQ.sample());
    }
}

//javac -cp "/Volumes/Projects C/Java Projects/Princeton Algorithms I/shared/algs4.jar" Deque.java RandomizedQueue.java Permutation.java
//java -cp ".:/Volumes/Projects C/Java Projects/Princeton Algorithms I/shared/algs4.jar" RandomizedQueue < algs4-data/tobe.txt