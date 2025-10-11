import java.util.NoSuchElementException;

public class SimpleLinkedList<T> {
    private Node<T> head;
    private int size;

    public SimpleLinkedList() {
        this.head = null;
        this.size = 0;
    }

    /*
     * We need to add the following methods for this file;
     * addFirst
     * addLast
     * removeFirst
     * isEmpty
     * size
     * Object toArray
     */


    // First one. We are adding a new value to the head of the list
    public void addFirst(T value) {
        Node<T> newNode = new Node<>(value);
        newNode.next = head;
        head = newNode;
        size++;
    }

    // Add to the last;
    public void addLast(T value) {
        Node<T> newNode = new Node<>(value);

        if(isEmpty()) {
            head = newNode;
        } else {
            Node<T> current = head;
            // we gotta traverse the list to find the last node
            
            // while loop
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++; //fix: added ';'. Cant believe I forgot it :(
    }

    // Removes and returns the element from the head of the list

    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty. Cannot remove first element!");
        }
        
        T dataToReturn = head.data;
        head = head.next;
        size--; // shrink the list;

        return dataToReturn;
    }

    // Check if the list is empty and size of the list

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    // GetHead fix for main
    public Node<T> getHead() {
        return head;
    }

    // Collect all of the elements for sorting at a later point
    // Im a little confused on this...
    public T[] toArray(T[] a) {
        Node<T> current = head;
        int i = 0;
        while (current != null && i < a.length) {
            a[i++] = current.data; // fix: make sure its data!!!
            current = current.next;
        }

        return a;
    }
        // bugfix: fixed return statement!
}