# Sorting Algorithm Performance Analysis

This project provides an empirical performance analysis of four common sorting algorithms: Insertion Sort, Merge Sort, Quick Sort, and Heap Sort.

The experiment measures and compares the following metrics for each algorithm on randomly generated datasets of various sizes:

1. **Execution Time** (in milliseconds)
2. **Number of Comparisons**
3. **Number of Data Movements** (assignments/swaps)

## Compilation Instructions

This is a standard Java project with no external dependencies. You can compile it from the root directory of the project using a standard Java compiler.

1. Create a directory for the compiled class files:

   ```bash
   mkdir bin
   ```

2. Compile the source code:

   ```bash
   javac -d bin src/AdvancedSortExperiment.java
   ```

## Execution Instructions

After successful compilation, you can run the experiment from the project's root directory.

```bash
java -cp bin AdvancedSortExperiment
```

The program will execute the benchmarks and print a formatted table of the results to the console.

## Dependencies

- Java Development Kit (JDK) 8 or higher.

No external libraries are required.

## Sample Usage

Running the program will produce output similar to the following, showing a comparison of the sorting algorithms across different input sizes for randomly generated data.

```
RESULT PRESENTATION:
==========================================================================
SORTING ALGORITHMS COMPARISON - RANDOM DATA
==========================================================================
Input Size   Algorithm    Time (ms)    Comparisons     Swaps
100          Insertion    0.01         2,450           2,549
100          Merge        0.02         475             N/A
100          Quick        0.01         485             141
100          Heap         0.02         580             297
1,000        Insertion    0.85         248,511         249,510
1,000        Merge        0.15         9,958           N/A
1,000        Quick        0.09         10,215          2,217
1,000        Heap         0.16         13,846          5,991
10,000       Insertion    55.12        24,981,530      24,982,529
10,000       Merge        1.25         132,838         N/A
10,000       Quick        0.98         138,445         29,880
10,000       Heap         1.95         178,308         89,901

CONCLUSIONS:
- Quick Sort is fastest on average for random data due to lower constant factors.
- Insertion Sort is competitive only for n < 1000; at n=10,000, its O(n^2) nature is prohibitive.
- Merge Sort provides consistent performance regardless of data order but incurs O(n) space.
- Heap Sort uses least memory (O(1) auxiliary) but is slower than Quick Sort due to cache misses.
```

## Known Limitations

- **Fixed Data Type:** The experiment is currently configured to run only on randomly generated integer arrays. The `main` method would need to be modified to use the other data generators provided (e.g., `genSorted`, `genReverse`).
- **Hardcoded Parameters:** The input sizes (`100, 1000, 10000, 100000`) and the number of runs for averaging (`5`) are hardcoded in the `main` method.
- **Metric Nuances:** The "Swaps" metric for Merge Sort is displayed as "N/A" to avoid a misleading comparison with in-place algorithms. The underlying `assignments` counter still tracks data movements from the temporary array back to the main array, which is a valid measure of work but is not a direct swap.
