# TechMart Search Performance Analysis

This project provides a performance analysis of different search algorithms for a product inventory system named "TechMart". It compares the execution time of sequential search, binary search, and a hybrid approach presumably using a HashMap for name-based lookups.

## Project Structure and Code Quality

The `src` folder contains two main executable classes:

- `TechMartSearchTest.java`: Benchmarks sequential search vs. binary search for finding products by ID.
- `TechMartSearchHybridTest.java`: Benchmarks sequential search, binary search, and a name-based search using a `HybridProductManager`.

**IMPORTANT:** This project has significant structural issues that prevent it from compiling and running correctly as-is:

1. **Missing Class:** The `TechMartSearchHybridTest.java` file relies on a class named `HybridProductManager`, which is not defined or included in the provided source files. This will cause a compilation failure.
2. **Duplicate Classes:** The `Product` and `SearchEngine` classes are defined in both `TechMartSearchTest.java` and `TechMartSearchHybridTest.java`. This duplication prevents compiling the project with a command like `javac src/*.java`. These classes should be extracted into their own files (`Product.java`, `SearchEngine.java`).

## Compilation Instructions

Due to the issues mentioned above, the files must be compiled individually. The following instructions assume the missing `HybridProductManager` class has been provided.

1. Create a directory for the compiled class files:

   ```bash
   mkdir bin
   ```

2. Compile the source code file by file:

   ```bash
   # Compile the first test (this should succeed)
   javac -d bin src/TechMartSearchTest.java

   # Compile the second test (this will FAIL without HybridProductManager)
   javac -d bin src/TechMartSearchHybridTest.java
   ```

   A proper fix would involve refactoring the duplicated classes into their own files and providing the `HybridProductManager` class.

## Execution Instructions

After successful compilation, you can run the benchmarks from the project's root directory.

1. **Run the basic search comparison:**

   ```bash
   java -cp bin TechMartSearchTest
   ```

2. **Run the hybrid search comparison:**
   _(This will only work if the project has been fixed and compiled successfully)._

   ```bash
   java -cp bin TechMartSearchHybridTest
   ```

## Dependencies

- Java Development Kit (JDK) 8 or higher.

No external libraries are required.

## Sample Usage

### TechMartSearchTest Output

Running the first test will produce output similar to this, comparing sequential and binary search on a large dataset.

````TechMart Search Performance Analysis (n=100,000)
--------------------------------------------------------------------------------
Method               | Best Case (ns)  | Average (ns)    | Worst Case (ns)
--------------------------------------------------------------------------------
Sequential Search    | 1500            | 2450000         | 4900000
Binary Search        | 800             | 1200            | 1500
``` _(Note: Nanosecond timings are highly variable and depend on the system.)_

### TechMartSearchHybridTest Output

If the project were functional, running the second test would produce output comparing all three search types.

```TechMart Search Performance Analysis (n=100,000)
--------------------------------------------------------------------------------
Method               | Best Case (ns)  | Average (ns)    | Worst Case (ns)
--------------------------------------------------------------------------------
Sequential Search    | 1600            | 2500000         | 9900000
Binary Search        | 900             | 1300            | 1600
Name Search (HashMap)| 1100            | 1100            | 1400
````

## Known Limitations

- **Fundamentally Broken:** The project is non-functional as provided. `TechMartSearchHybridTest.java` cannot be compiled due to the missing `HybridProductManager` class.
- **Poor Structure:** The duplication of `Product` and `SearchEngine` classes is a major structural flaw that needs refactoring.
- **Hardcoded Parameters:** The dataset size (`n=100000`) and the specific test cases (best, average, worst) are hardcoded directly in the `main` methods. The tests are not configurable without editing the source code.
- **Incomplete Benchmarking:** The benchmarks are simple and may be subject to inaccuracies from JIT compilation and other JVM optimizations. A more robust benchmarking framework like JMH would be required for rigorous analysis.
