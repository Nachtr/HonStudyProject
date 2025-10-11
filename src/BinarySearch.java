public class BinarySearch {
    // Search for the specified key in a sorted array using the binary search algo we learned in class

    public static <T extends Comparable<T>> int indexOf(T[] sorted, T key) {
        int lo = 0;
        int hi = sorted.length - 1;

        // loop while the search space is valid (lo pointer is not past the hi pointer)

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2; // using this because of we use (lo + hi) and then / 2 we could get integer overflow in the long run.
            int comparison = key.compareTo(sorted[mid]);

            if (comparison == 0) {
                return mid;

            } else if (comparison < 0) {
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }

        return -1; // return -1 if the key was not found after the loop finishes.
    }
}
