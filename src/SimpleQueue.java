
import java.util.NoSuchElementException;

public class SimpleQueue<T> {
    private Node<T> head; // front of the queue
    private Node<T> tail; // end of the queue
    private int size;

    public SimpleQueue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /*
     * Required methods for this file:
     * - Enqueue (T val) which should add to tail
     * - T dequeue() should remove
     * - T peek() should see in
     * - Boolean is empty
     * - Int size
     */

     private void enqueue(T value) {
        Node<T> newNode = new Node<>(value);

        if (isEmpty()) {
            head = newNode; // if empty; new node is both head and tail right?
        } else {
            tail.next = newNode; // if not empty; the current tail is updated to new node
        }

        tail = newNode;
        size++;
     }

     // As mentioned above, we should create something to remove from the queue. Similar to pop method
     private T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty. Nothing to dequeue!");
        } 

        T dataToReturn = head.data;

        head = head.next;
        size--;

        if (head == null) { // bugfix: make sure head == null...
            tail = null; // If head is now null; the queue is empty!!! Make sure tail is null...
        }

        return dataToReturn;
     }

     // A method to peek into the queue
     private T peek() {
        if (isEmpty()) {
             throw new NoSuchElementException("Queue is empty, nothing to see here!");
        }
        return head.data;
     }

     // isEmpty from the last file
     public boolean isEmpty() {
        return size == 0;
     }

     public int size() {
        return size;
     }
}
