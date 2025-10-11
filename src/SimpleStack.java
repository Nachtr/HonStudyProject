import java.util.NoSuchElementException;

public class SimpleStack<T> {
    private Node<T> top;
    private int size;

    public SimpleStack() { // Today I learned that you have to have S for the constructor instead of s
        this.top = top;
        this.size = 0;
    }


    // Add a new node at the top of the stock using push method
    public void push(T value) {
        Node<T> newNode = new Node<>(value);
        newNode.next = top;
        top = newNode;
        size++;
    }


    // Removes and returns the top value of the stack
    public T pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty. Cannot pop!");
        } 
        T dataToReturn = top.data; // using this to get the value of the top node
        top = top.next;
        size--;
        return dataToReturn;
    }

    // View without pop or adding
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty. Nothing to view!");            
        }

        return top.data; // return the value from the top
    }

    // Method check for if empty or not...
    public boolean isEmpty() {
        return size == 0; // we could also return top == null right?
    } // For some reason, I guess im tired and thought this was already created.

    // This next method should return the size of the stack hints its name
    public int size() {
        return size;
    }
}

