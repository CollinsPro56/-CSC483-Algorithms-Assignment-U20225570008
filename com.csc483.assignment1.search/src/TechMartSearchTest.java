import java.util.*;

/**
 * Represents a product in the TechMart inventory system.
 * This class encapsulates the data related to a single product.
 */
class Product {
    private int productId;
    private String productName;
    private String category;
    private double price;
    private int stockQuantity;

    public Product(int productId, String productName, String category, double price, int stockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Accessor methods for the product's properties.
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
}

/**
 * Contains various search algorithms for finding products.
 * This utility class provides static methods for searching products
 * based on different criteria and algorithms.
 */
class SearchEngine {
    /**
     * Finds a product by its ID using a sequential search algorithm.
     * This method iterates through the array from start to finish.
     * Time complexity: O(n)
     */
    public static Product sequentialSearchById(Product[] products, int targetId) {
        for (Product p : products) {
            if (p != null && p.getProductId() == targetId) {
                return p;
            }
        }
        return null;
    }

    /**
     * Finds a product by its ID using a binary search algorithm.
     * This method requires the product array to be sorted by product ID.
     * Time complexity: O(log n)
     */
    public static Product binarySearchById(Product[] products, int targetId) {
        int low = 0;
        int high = products.length - 1;
        while (low <= high) {
            // Calculate mid point to avoid potential overflow
            int mid = low + (high - low) / 2;
            Product midProd = products[mid];
            // This check is a safeguard, assuming the array might have nulls,
            // though in the current implementation it's fully populated.
            if (midProd == null) break;
            int midId = midProd.getProductId();

            if (midId == targetId) {
                return midProd;
            } else if (midId < targetId) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }

    /**
     * Finds a product by its name using a sequential search.
     * The search is case-insensitive.
     *
     */
    public static Product searchByName(Product[] products, String targetName) {
        for (Product p : products) {
            if (p != null && p.getProductName().equalsIgnoreCase(targetName)) {
                return p;
            }
        }
        return null;
    }
}

/**
 * Main class to conduct a performance analysis of search algorithms.
 * This class sets up a test environment to compare the performance of
 * sequential and binary search algorithms on a large dataset of products.
 */
public class TechMartSearchTest {
    /**
     * Entry point for the performance analysis program.
     * It initializes a dataset of products, runs search benchmarks,
     * and prints the performance results to the console.
     */
    public static void main(String[] args) {
        // The size of the dataset to be used for testing.
        int n = 100000;
        Product[] products = new Product[n];
        Random rand = new Random();

        // A set is used to ensure that all generated product IDs are unique.
        Set<Integer> usedIds = new HashSet<>();
        for (int i = 0; i < n; i++) {
            int id;
            // Loop to generate a unique random ID.
            do {
                id = rand.nextInt(200000) + 1;
            } while (usedIds.contains(id));
            usedIds.add(id);
            // Create a new product with the unique ID and add it to the array.
            products[i] = new Product(id, "Product_" + id, "Electronics", rand.nextDouble() * 1000, 50);
        }

        // The product array must be sorted by ID for binary search to work correctly.
        Arrays.sort(products, Comparator.comparingInt(Product::getProductId));

        // Define test cases for benchmarking: best, average, and worst scenarios.
        // Best case for binary search is the middle element.
        int bestCaseId = products[n / 2].getProductId();
        // Average case is an element somewhere in the array.
        int averageCaseId = products[n / 4].getProductId();
        // Worst case for sequential search is the last element.
        int worstCaseId = products[n - 1].getProductId();

        // Print the header for the performance results table.
        System.out.println("TechMart Search Performance Analysis (n=100,000)");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-20s | %-15s | %-15s | %-15s\n", "Method", "Best Case (ns)", "Average (ns)", "Worst Case (ns)");
        System.out.println("--------------------------------------------------------------------------------");

        // Execute and print the benchmarks for each search algorithm.
        // For sequential search, worst case is finding the last element.
        runBenchmark("Sequential Search", products, products[0].getProductId(), averageCaseId, worstCaseId, true);
        // For binary search, a plausible worst case is also finding an element near the end.
        runBenchmark("Binary Search", products, bestCaseId, averageCaseId, worstCaseId, false);
    }

    private static void runBenchmark(String label, Product[] products, int best, int avg, int worst, boolean isSeq) {
        // Measure execution time for best, average, and worst cases.
        long tBest = measure(products, best, isSeq);
        long tAvg = measure(products, avg, isSeq);
        long tWorst = measure(products, worst, isSeq);
        // Print the results in a formatted row.
        System.out.printf("%-20s | %-15d | %-15d | %-15d\n", label, tBest, tAvg, tWorst);
    }

    private static long measure(Product[] products, int target, boolean isSeq) {
        // Record the start time.
        long start = System.nanoTime();
        // Perform the search based on the selected algorithm.
        if (isSeq) {
            SearchEngine.sequentialSearchById(products, target);
        } else {
            SearchEngine.binarySearchById(products, target);
        }
        // Calculate and return the elapsed time.
        return System.nanoTime() - start;
    }
}
