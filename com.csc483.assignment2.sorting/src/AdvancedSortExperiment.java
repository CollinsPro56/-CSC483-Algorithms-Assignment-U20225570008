import java.util.*;

/**
 * A class to conduct an empirical performance analysis of four common sorting algorithms:
 * Insertion Sort, Merge Sort, Quick Sort, and Heap Sort.
 *
 * The experiment measures three key metrics for each algorithm:
 * 1. Execution Time (in milliseconds)
 * 2. Number of Comparisons
 * 3. Number of Data Movements (assignments/swaps)
 *
 * The analysis is run on randomly generated datasets of various sizes to observe
 * how each algorithm's performance scales.
 */
public class AdvancedSortExperiment {

    // --- METRIC COUNTERS ---
    // These are static so they can be accessed and modified by the sorting methods
    // without needing to pass them as parameters. They are reset before each sort.
    static long comparisons = 0;
    static long assignments = 0; // Represents data movements (swaps or direct assignments).

    // --- ALGORITHM INTERFACE & IMPLEMENTATIONS ---

    /**
     * A functional interface defining the contract for a sorting algorithm.
     * This allows treating all sorting algorithms uniformly in the experiment.
     */
    interface SortingAlgorithm {
        void sort(int[] arr);
        String getName();
    }

    /**
     * Implements Insertion Sort. It builds the final sorted array one item at a time.
     * It is efficient for small datasets but has a quadratic O(n^2) time complexity.
     */
    static class InsertionSort implements SortingAlgorithm {
        public void sort(int[] arr) {
            for (int i = 1; i < arr.length; i++) {
                int key = arr[i];
                int j = i - 1;
                // This loop finds the correct position for 'key' by shifting larger elements to the right.
                while (j >= 0 && arr[j] > key) {
                    comparisons++; // One comparison for arr[j] > key
                    arr[j + 1] = arr[j];
                    assignments++; // One assignment for the shift
                    j--;
                }
                if (j >= 0) comparisons++; // The final comparison that fails the loop condition.
                arr[j + 1] = key;
                assignments++;
            }
        }
        public String getName() { return "Insertion"; }
    }

    /**
     * Implements Merge Sort, a classic "divide and conquer" algorithm.
     * It has a stable O(n log n) time complexity but requires O(n) auxiliary space.
     */
    static class MergeSort implements SortingAlgorithm {
        public void sort(int[] arr) { mergeSort(arr, 0, arr.length - 1); }
        private void mergeSort(int[] arr, int l, int r) {
            if (l < r) {
                int m = l + (r - l) / 2;
                mergeSort(arr, l, m);
                mergeSort(arr, m + 1, r);
                merge(arr, l, m, r);
            }
        }
        // Merges two sorted subarrays arr[l..m] and arr[m+1..r]
        private void merge(int[] arr, int l, int m, int r) {
            int n1 = m - l + 1, n2 = r - m;
            // Create temporary arrays to hold the two halves.
            int[] L = new int[n1], R = new int[n2];
            // Copy data to temp arrays. System.arraycopy is efficient but doesn't count as "assignments" in our metric.
            // The assignments counted below are for moving data from temp arrays back to the main array.
            System.arraycopy(arr, l, L, 0, n1);
            System.arraycopy(arr, m + 1, R, 0, n2);

            int i = 0, j = 0, k = l;
            // Merge the temp arrays back into the original array arr[l..r]
            while (i < n1 && j < n2) {
                comparisons++;
                if (L[i] <= R[j]) {
                    arr[k++] = L[i++];
                } else {
                    arr[k++] = R[j++];
                }
                assignments++; // Count one assignment for each element moved back to arr.
            }
            // Copy any remaining elements of L[]
            while (i < n1) { arr[k++] = L[i++]; assignments++; }
            // Copy any remaining elements of R[]
            while (j < n2) { arr[k++] = R[j++]; assignments++; }
        }
        public String getName() { return "Merge"; }
    }

    /**
     * Implements Quick Sort, another "divide and conquer" algorithm.
     * It is very fast on average O(n log n) and sorts in-place, but has a worst-case of O(n^2).
     */
    static class QuickSort implements SortingAlgorithm {
        public void sort(int[] arr) { quickSort(arr, 0, arr.length - 1); }
        private void quickSort(int[] arr, int low, int high) {
            if (low < high) {
                int pi = partition(arr, low, high);
                quickSort(arr, low, pi - 1);
                quickSort(arr, pi + 1, high);
            }
        }
        // This function takes the last element as pivot, places the pivot element at its
        // correct position in the sorted array, and places all smaller elements to the left
        // and all greater elements to the right.
        private int partition(int[] arr, int low, int high) {
            int pivot = arr[high];
            int i = (low - 1); // Index of smaller element
            for (int j = low; j < high; j++) {
                comparisons++;
                // If current element is smaller than the pivot
                if (arr[j] < pivot) {
                    i++;
                    // Swap arr[i] and arr[j]
                    int temp = arr[i]; arr[i] = arr[j]; arr[j] = temp;
                    assignments += 3; // A swap involves 3 assignments.
                }
            }
            // Place the pivot in its correct final position
            int temp = arr[i + 1]; arr[i + 1] = arr[high]; arr[high] = temp;
            assignments += 3;
            return i + 1;
        }
        public String getName() { return "Quick"; }
    }

    /**
     * Implements Heap Sort. It uses a binary heap data structure to sort elements.
     * It has a reliable O(n log n) time complexity and sorts in-place (O(1) space).
     */
    static class HeapSort implements SortingAlgorithm {
        public void sort(int[] arr) {
            int n = arr.length;
            // 1. Build a max-heap from the input data.
            for (int i = n / 2 - 1; i >= 0; i--) heapify(arr, n, i);

            // 2. One by one, extract elements from the heap.
            for (int i = n - 1; i > 0; i--) {
                // Move the current root (max element) to the end of the array.
                int temp = arr[0]; arr[0] = arr[i]; arr[i] = temp;
                assignments += 3;
                // Call max heapify on the reduced heap.
                heapify(arr, i, 0);
            }
        }
        // To heapify a subtree rooted with node i which is an index in arr[]. n is size of heap.
        private void heapify(int[] arr, int n, int i) {
            int largest = i, l = 2 * i + 1, r = 2 * i + 2;
            // Check if left child is larger than root
            if (l < n) { comparisons++; if (arr[l] > arr[largest]) largest = l; }
            // Check if right child is larger than largest so far
            if (r < n) { comparisons++; if (arr[r] > arr[largest]) largest = r; }
            // If largest is not root
            if (largest != i) {
                int swap = arr[i]; arr[i] = arr[largest]; arr[largest] = swap;
                assignments += 3;
                // Recursively heapify the affected sub-tree.
                heapify(arr, n, largest);
            }
        }
        public String getName() { return "Heap"; }
    }

    // --- DATA GENERATORS ---
    // Utility methods to create arrays with different properties for testing.

    /** Generates an array of size n with random integer values. */
    static int[] genRandom(int n) { 
        int[] a = new int[n]; Random r = new Random();
        for(int i=0; i<n; i++) a[i] = r.nextInt(1000000); return a; 
    }
    /** Generates an array of size n that is already sorted in ascending order. */
    static int[] genSorted(int n) { 
        int[] a = new int[n]; for(int i=0; i<n; i++) a[i] = i; return a; 
    }
    /** Generates an array of size n that is sorted in descending (reverse) order. */
    static int[] genReverse(int n) { 
        int[] a = new int[n]; for(int i=0; i<n; i++) a[i] = n - i; return a; 
    }
    /** Generates an array that is "nearly sorted" by taking a sorted array and swapping a few elements. */
    static int[] genNearlySorted(int n) {
        int[] a = genSorted(n); Random r = new Random();
        // Swap 10% of the elements to introduce some disorder.
        for(int i=0; i < n/10; i++) { 
            int idx1 = r.nextInt(n), idx2 = r.nextInt(n);
            int t = a[idx1]; a[idx1] = a[idx2]; a[idx2] = t;
        }
        return a;
    }
    /** Generates an array with many duplicate values. */
    static int[] genDuplicates(int n) {
        int[] a = new int[n]; Random r = new Random();
        for(int i=0; i<n; i++) a[i] = r.nextInt(10); return a;
    }

    // --- MAIN EXPERIMENT RUNNER ---
    /**
     * The main entry point for the sorting experiment. It sets up the test parameters,
     * runs the benchmarks, and prints the results in a formatted table.
     */
    public static void main(String[] args) {
        // Define the input sizes to test.
        int[] sizes = {100, 1000, 10000, 100000};
        // Define the algorithms to be benchmarked.
        SortingAlgorithm[] algorithms = {new InsertionSort(), new MergeSort(), new QuickSort(), new HeapSort()};
        
        System.out.println("RESULT PRESENTATION:");
        System.out.println("==========================================================================");
        System.out.println("SORTING ALGORITHMS COMPARISON - RANDOM DATA");
        System.out.println("==========================================================================");
        System.out.printf("%-12s %-12s %-12s %-15s %-12s%n", "Input Size", "Algorithm", "Time (ms)", "Comparisons", "Swaps");

        // Iterate over each defined size.
        for (int n : sizes) {
            // Iterate over each algorithm.
            for (SortingAlgorithm alg : algorithms) {
                long totalTime = 0, totalComp = 0, totalAssign = 0;
                
                // Run the experiment 5 times for each algorithm and size to get a stable average.
                // This helps mitigate variations from system load and JIT compilation.
                for (int i = 0; i < 5; i++) {
                    int[] data = genRandom(n);
                    comparisons = 0; assignments = 0;
                    long start = System.nanoTime();
                    alg.sort(data);
                    totalTime += (System.nanoTime() - start);
                    totalComp += comparisons;
                    totalAssign += assignments;
                }
                
                // Calculate the average time and convert from nanoseconds to milliseconds.
                double avgTime = (totalTime / 5.0) / 1_000_000.0;
                // For Merge Sort, "Swaps" isn't a direct equivalent to the in-place algorithms.
                // We display "N/A" to avoid a misleading comparison. The metric still counts
                // movements from the temp array back to the main array.
                String swapDisplay = alg.getName().equals("Merge") ? "N/A" : String.format("%,d", totalAssign/5);
                
                // Print the formatted results for the current algorithm and size.
                System.out.printf("%-12s %-12s %-12.2f %-15s %-12s%n", 
                    String.format("%,d", n), alg.getName(), avgTime, String.format("%,d", totalComp/5), swapDisplay);
            }
        }

        // Print the final summary of expected outcomes.
        printConclusions();
    }

    /** Prints a pre-written summary of the expected theoretical and practical outcomes of the experiment. */
    private static void printConclusions() {
        System.out.println("\nCONCLUSIONS:");
        System.out.println("- Quick Sort is fastest on average for random data due to lower constant factors.");
        System.out.println("- Insertion Sort is competitive only for n < 1000; at n=10,000, its O(n^2) nature is prohibitive.");
        System.out.println("- Merge Sort provides consistent performance regardless of data order but incurs O(n) space.");
        System.out.println("- Heap Sort uses least memory (O(1) auxiliary) but is slower than Quick Sort due to cache misses.");
    }
}