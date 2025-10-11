

public class MergeSort {
    public static <T extends Comparable<T>> void sort(T[] arr) { // bear with me on this. So we need to create a merge sort algo for the project...
        // We need to create a base case...
        if (arr.length < 2) {
            return; // so it returns if the array is smaller than 2 because it would be hard to implement a sorting algo with 2 (pointless)
        }

        int mid = arr.length / 2; // finding the midpoint

        // This creates the left and right subarrays for sorting with the midpoint we found above
        T[] left = (T[]) new Comparable[mid];
        T[] right = (T[]) new Comparable[arr.length - mid];

        // Create the left array with pop
        for (int i = 0; i < mid; i++) {
            left[i] = arr[i];
        }

        // Pop the right array
        for (int i = mid; i < arr.length; i++) {
            right[i - mid] = arr[i];
        }

        // Rec sort left and right
        sort(left);
        sort(right);

        // Now we have to merge them back and copy to the arr???
        merge(arr,left,right); // Thats it right??
    }

    // Merge helper!

    private static <T extends Comparable<T>> void merge (T[] arr, T[] left, T[] right) {
        int i = 0; // Index of left
        int j = 0; // Index of right
        int k = 0; // Index of the merge

        // While for merge the subarrays
        while (i < left.length && j < right.length) {
            if(left[i].compareTo(right[j]) <= 0) {
                arr[k++] = left[i++];
            } else {
                arr[k++] = right[j++];
            }
        }

        // Then we copy the remaining to the left ar
        while (i < left.length) {
            arr[k++] = left[i++];
        }

        // Copy the right
        while (j < right.length) {
            arr[k++] = right[j++];
        }
    }
}