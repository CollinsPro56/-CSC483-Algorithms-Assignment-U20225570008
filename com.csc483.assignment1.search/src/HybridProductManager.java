import java.util.HashMap;
import java.util.Map;

/**
 * Manages products using a hybrid data structure approach.
 * This manager uses a sorted array for product IDs and a HashMap for O(1) name lookups,
 * demonstrating a trade-off between insertion and search performance.
 */
public class HybridProductManager {
    // A secondary index (HashMap) to provide constant-time O(1) lookups by product name.
    private Map<String, Product> nameIndex = new HashMap<>();
    
    /**
     * Adds a new product to the sorted array while maintaining order and updating the name index.
     * This method uses a binary search to find the correct insertion point and then shifts
     * elements to make space, resulting in an O(n) time complexity for insertion. */
    public Product[] addProduct(Product[] currentArray, int currentSize, Product newProduct) {
        // 1. Update the secondary index (name-based HashMap) for O(1) name lookups.
        nameIndex.put(newProduct.getProductName(), newProduct);

        // 2. Find the correct insertion point for the new product using binary search logic.
        // This operation has a time complexity of O(log n).
        int low = 0;
        int high = currentSize - 1;
        int indexToInsert = currentSize;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (currentArray[mid].getProductId() > newProduct.getProductId()) {
                indexToInsert = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        // 3. Shift elements to the right of the insertion point to make space for the new product.
        // This is the most expensive part of the operation, with a time complexity of O(n).
        for (int i = currentSize; i > indexToInsert; i--) {
            currentArray[i] = currentArray[i - 1];
        }

        // 4. Insert the new product into its sorted position.
        currentArray[indexToInsert] = newProduct;
        
        return currentArray;
    }

    /**
     * Searches for a product by its name using the auxiliary name index.
     * This provides a very fast O(1) average-case time complexity for lookups.
     *
     */
    public Product searchByName(String name) {
        return nameIndex.get(name);
    }
}