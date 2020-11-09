import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args){
       final int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> q = new RandomizedQueue<String>();

        while(!StdIn.isEmpty()){
            String item = StdIn.readString();
            q.enqueue(item);
        }

        if (k < 0 || k > q.size()) return;

        for(int i = 0; i < k; i++){
            String s = q.dequeue();
            StdOut.println(s);
        }
    }
}

//javac -cp "/Volumes/Projects C/Java Projects/Princeton Algorithms I/shared/algs4.jar" Deque.java RandomizedQueue.java Permutation.java
//java -cp ".:/Volumes/Projects C/Java Projects/Princeton Algorithms I/shared/algs4.jar" Permutation 3 < algs4-data/words3.txt