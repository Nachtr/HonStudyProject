public class BST<T extends Comparable<T>> {
    // create the fields
    private Node<T> root;
    private int size;

    public static interface Visitor<T> {
        void visit(T value);
    }

    // Node helper
    static class Node<T> {
        T value;
        Node<T> left;
        Node<T> right;

        public Node(T value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    // Con
    public BST() {
        this.root = null;
        this.size = 0;
    }


    /*
     * What public methods do we need?
     * insert
     * boolean contains
     * size
     * void inOrder (visit<T>)
     */

     // I think thats it???

     public void insert(T value) {
        root = insertRecursive(root, value);
     } // Insert function should insert new data into the BST


     // Check if the value is present in the BST : CONTAINS

     public boolean contains(T value) {
        return containsRecursive(root, value); // I feel like this part is all still pretty hard because Im still trying to learn about it.
     }

     // Returns the number of elements in the tree : SIZE

     public int size() {
        return size;
     }

     // Performs the inorder traversal of going left > node > right...
     // Hopefully...

     public void inOrder(Visitor<T> v) {
        inOrderRecursive(root, v);
     }

     /*
      * Below will be the necessary recursive methods needed to help the program
      */

      private Node<T> insertRecursive(Node<T> current, T value) {
        if (current == null) {
            size++;
            return new Node<>(value);
        }

        int comparison = value.compareTo(current.value);

        if (comparison < 0) {
            current.left = insertRecursive(current.left, value);
        } else if (comparison > 0) {
            current.right = insertRecursive(current.right, value);
        } // so it should go right if our value is greater than the value of the current node????

        return current;
      }

      // I think ^ will work???

      private boolean containsRecursive(Node<T> current, T value) {
        if (current == null) {
            return false;
        }

        int comparison = value.compareTo(current.value);
        if (comparison == 0) {
            return true;
        }

        // If not continue search
        if (comparison < 0) {
            return containsRecursive(current.left, value);
        } else {
            return containsRecursive(current.right, value);
        }
    }

    private void inOrderRecursive(Node<T> current, Visitor<T> v) {
        if (current != null) {
            inOrderRecursive(current.left, v);
            v.visit(current.value); //visiting node?
            // visit right?
            inOrderRecursive(current.right, v);
        }
    }
}
