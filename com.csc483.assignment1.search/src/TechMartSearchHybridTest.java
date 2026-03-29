import java.util.*;

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

    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
}

class SearchEngine {
    public static Product sequentialSearchById(Product[] products, int targetId) {
        for (Product p : products) {
            if (p != null && p.getProductId() == targetId) {
                return p;
            }
        }
        return null;
    }

    public static Product binarySearchById(Product[] products, int targetId) {
        int low = 0;
        int high = products.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            Product midProd = products[mid];
            if (midProd == null) break;
            int midId = midProd.getProductId();
            if (midId == targetId) return midProd;
            if (midId < targetId) low = mid + 1;
            else high = mid - 1;
        }
        return null;
    }

    /**
     * Finds a product by its name using a sequential search.
     * The search is case-insensitive.
     *
     */
    public static Product searchByName(Product[] products, String targetName) {
        // Tokenize the target name for flexible matching
        Set<String> targetTokens = tokenize(targetName);
        for (Product p : products) {
            if (p != null) {
                // Tokenize the product name and check for any matching tokens
                Set<String> productTokens = tokenize(p.getProductName());
                for (String token : targetTokens) {
                    if (productTokens.contains(token)) {
                        return p;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Tokenizes a string into a set of lowercase words, ignoring punctuation and extra spaces.
     * This enables partial matching in searches.
     */
    public static Set<String> tokenize(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new HashSet<>();
        }
        // Split by whitespace and punctuation, convert to lowercase, and collect unique tokens
        String[] words = text.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "").split("\\s+");
        return new HashSet<>(Arrays.asList(words));
    }
}

/**
 * A test class to benchmark the performance of the hybrid search approach.
 * This class compares the performance of sequential search, binary search (on ID),
 * and the new HashMap-based name search.
 */
public class TechMartSearchHybridTest {
    /* The main method to run the hybrid search performance analysis. */
    public static void main(String[] args) {
        // Define the number of products and allocate extra capacity for additions.
        int n = 100000;
        int extraCapacity = 1000; 
        Product[] products = new Product[n + extraCapacity];
        Random rand = new Random();
        // Instantiate the manager that handles the hybrid data structures.
        HybridProductManager manager = new HybridProductManager();

        // Use a Set to ensure unique product IDs are generated.
        Set<Integer> usedIds = new HashSet<>();
        int currentSize = 0;
        // Populate the products array by adding products one by one through the manager.
        // The manager ensures the array remains sorted by ID after each insertion.
        for (int i = 0; i < n; i++) {
            int id;
            do {
                id = rand.nextInt(200000) + 1;
            } while (usedIds.contains(id));
            usedIds.add(id);
            Product newProduct = new Product(id, "Product_" + id, "Electronics", rand.nextDouble() * 1000, 50);
            // Use the manager to add the product, which handles sorting and indexing.
            products = manager.addProduct(products, currentSize, newProduct);
            currentSize++;
        }

        // Define product IDs and names for different test scenarios.
        int bestCaseId = products[n / 2].getProductId();
        int averageCaseId = products[n / 4].getProductId();
        int worstCaseId = products[n - 1].getProductId();
        int nonExistentId = 999999;
        // Define names for the name-based search benchmark.
        String existingName = products[0].getProductName();
        String nonExistentName = "NonExistentProduct";

        // Print the header for the results table.
        System.out.println("TechMart Search Performance Analysis (n=100,000)");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-20s | %-15s | %-15s | %-15s\n", "Method", "Best Case (ns)", "Average (ns)", "Worst Case (ns)");
        System.out.println("--------------------------------------------------------------------------------");

        // Run and display benchmarks for ID-based searches.
        runIdBenchmark("Sequential Search", products, products[0].getProductId(), averageCaseId, nonExistentId, true);
        runIdBenchmark("Binary Search", products, bestCaseId, averageCaseId, nonExistentId, false);
        // Run and display benchmark for the HashMap-based name search.
        runNameBenchmark("Name Search (HashMap)", existingName, nonExistentName, manager);
    }

    private static void runIdBenchmark(String label, Product[] products, int best, int avg, int worst, boolean isSeq) {
        long tBest = measure(products, best, isSeq);
        long tAvg = measure(products, avg, isSeq);
        long tWorst = measure(products, worst, isSeq);
        System.out.printf("%-20s | %-15d | %-15d | %-15d\n", label, tBest, tAvg, tWorst);
    }

    private static void runNameBenchmark(String label, String existingName, String nonExistentName, HybridProductManager manager) {
        // Best/average case for HashMap is finding an existing item.
        long tBest = measureName(existingName, manager);
        long tAvg = tBest; // Assumed to be the same for simplicity.
        // Worst case for HashMap is searching for a non-existent item.
        long tWorst = measureName(nonExistentName, manager);
        
        System.out.printf("%-20s | %-15d | %-15d | %-15d\n", label, tBest, tAvg, tWorst);
    }

    private static long measure(Product[] products, int target, boolean isSeq) {
        long start = System.nanoTime();
        if (isSeq) {
            SearchEngine.sequentialSearchById(products, target);
        } else {
            SearchEngine.binarySearchById(products, target);
        }
        return System.nanoTime() - start;
    }

    /* Measures the time taken to perform a name-based search using the HybridProductManager. */
    private static long measureName(String targetName, HybridProductManager manager) {
        if (manager == null || targetName == null || targetName.trim().isEmpty()) {
            return 0;
        }
        long start = System.nanoTime();
        manager.searchByName(targetName);
        return System.nanoTime() - start;
    }
}