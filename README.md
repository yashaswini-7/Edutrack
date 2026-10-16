# EduTrack: Advanced Educational Analytics & Algorithm Engine

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Data Structures](https://img.shields.io/badge/DSA-Advanced_Canon-blue.svg)]()
[![Compliance](https://img.shields.io/badge/Collections-Zero_java.util-brightgreen.svg)]()
[![Dataset](https://img.shields.io/badge/Dataset-649_Student_Records-yellow.svg)]()
[![Institution](https://img.shields.io/badge/KL_University-DSA_Project_Review--1-purple.svg)]()

> **Applying Data Structures and Algorithms to Academic Information Processing**  
> Developed for **DSA Project Review-1** at **KL University (Koneru Lakshmaiah Education Foundation)**.  
> **Author:** Keshitha

---

## 📌 Executive Summary

Academic student records are text-heavy, high-dimensional, and queried in diverse ways. Ad-hoc scans and unverified heuristics lack analytical justification and predictable asymptotic bounds.

**EduTrack** is an enterprise-grade academic information processing engine built in **Java 21** that maps real-world educational problems (search at scale, profile similarity, typo correction, exam scheduling, facility routing, and conflict resolution) directly to formal algorithmic paradigms.

### ⚡ Strict Course Rule Compliance: Zero `java.util` in Algorithmic Engines
All algorithm engines (`module1` through `module6`) and core utilities are implemented **from scratch** without relying on Java's standard collections (`ArrayList`, `HashMap`, `Queue`, `LinkedList`, etc.). Custom data structures engineered for this project include:
- `CustomIntArray`: Dynamic resizable integer buffer with amortized $O(1)$ push.
- `CustomIntQueue`: Ring-buffer FIFO queue for BFS and flow traversals.
- `CustomStringArray`: Dynamic array for string tokens.
- `CustomMatchList`: Coordinate-tracking list mapping raw text byte offsets to exact `(Line, Column)` coordinates.

---

## 🏗️ System Architecture

```
                       +-----------------------------+
                       |        Console User         |
                       +--------------+--------------+
                                      |
                                      v
                       +-----------------------------+
                       |   edutrack.cli.EduTrackMain  |
                       +--------------+--------------+
                                      |
         +----------------------------+----------------------------+
         |                            |                            |
         v                            v                            v
+------------------+         +------------------+         +------------------+
| Module 1: Found. |         | Module 2: String |         | Module 3: Adv DP |
| Miller-Rabin, QR |         | KMP, Z, SA, LCP  |         | Edit, MCM, TSP   |
+------------------+         +------------------+         +------------------+
         |                            |                            |
         v                            v                            v
+------------------+         +------------------+         +------------------+
| Module 4: Flow   |         | Module 5: NP/Red |         | Module 6: Rand/P |
| Edmonds-Karp/Din |         | Reductions, 2-AP |         | QuickSort, Scan  |
+------------------+         +------------------+         +------------------+
         |                            |                            |
         +----------------------------+----------------------------+
                                      |
                                      v
                       +-----------------------------+
                       |     edutrack.core layer     |
                       | CustomIntArray, DataLoader, |
                       | SortSearch, Student Record  |
                       +--------------+--------------+
                                      |
                                      v
                       +-----------------------------+
                       |   data/student-mat.csv      |
                       |   (649 records, 33 fields)  |
                       +-----------------------------+
```

---

## 🚀 The Six Algorithmic Modules

### 🔹 Module 1: Foundational Engine & Primality Testing
- **Query Router:** Analyzes query characteristics and automatically routes requests to the optimal algorithm family.
- **Asymptotic Design Matrix:** Formal analysis explaining why basic DSA-1/2 approaches fail on scale and why advanced canon structures are strictly required.
- **Miller-Rabin Primality Testing:** $O(k \log^3 n)$ probabilistic primality testing with Fermat witness and square-roots-of-unity diagnostics (e.g., for Carmichael numbers like 561).

### 🔹 Module 2: Exact String Algorithms & Suffix Structures
- **Knuth-Morris-Pratt (KMP):** $O(n + m)$ linear search with precomputed $\pi$ failure table. Zero text backtracking; maps byte offsets to exact (Line, Column) positions.
- **Z-Algorithm:** $O(n + m)$ linear cross-validation using longest common prefix $Z$-boxes.
- **Rabin-Karp:** Polynomial rolling hash with comparison between weak single modulus (101) producing spurious collisions versus robust double-hashing ($10^9+7, 10^9+9$).
- **Aho-Corasick Multi-Pattern Matcher:** Trie with failure and dictionary links in $O(n + m + z)$ time to locate multiple academic attributes in a single pass.
- **Suffix Array & Kasai LCP:**
  - Prefix doubling construction with custom merge-sort ranking: $O(n \log^2 n)$.
  - Kasai’s algorithm computes the Longest Common Prefix array in linear $O(n)$ time.
  - Used for detecting the **Longest Repeated Substring** and **Longest Common Substring** between student profiles joined with a sentinel character.

### 🔹 Module 3: Advanced Dynamic Programming
- **Edit Distance & Typo Correction:**
  - Standard Levenshtein: $O(nm)$ table for insertions, deletions, and substitutions.
  - Damerau-Levenshtein: Extends Levenshtein with adjacent transpositions (swaps count as 1 instead of 2).
- **Sequence Alignment:** Global Needleman-Wunsch and local Smith-Waterman sequence alignments for comparing prerequisite and grade progressions.
- **Interval DP:**
  - **Matrix-Chain Multiplication:** $O(n^3)$ algorithm determining the optimal parenthesization order (e.g., reducing 21,000 multiplications down to 4,500).
  - **Optimal Binary Search Tree (OBST):** Minimizes weighted search depth based on student access frequencies.
- **Bitmask DP (Held-Karp TSP):** $O(2^n \cdot n^2)$ exact tour optimizer for campus facility routing.
- **Tree DP:** Linear $O(n)$ tree rerooting to compute tree diameter, centroid, and Maximum Independent Set.
- **SOS DP (Sum Over Subsets):** $O(k \cdot 2^k)$ fast Walsh-Hadamard-style submask aggregation for student feature subsets.

### 🔹 Module 4: Network Flow, Matching & Reductions
- **Edmonds-Karp:** Shortest augmenting path Max-Flow using BFS in $O(V \cdot E^2)$.
- **Dinic's Algorithm:** Level-graph construction with blocking flow via DFS in $O(V^2 \cdot E)$ (and $O(E \sqrt{V})$ on unit networks).
- **Maximum Bipartite Matching:** Formulated as a Max-Flow reduction.
- **Min-Cost Max-Flow (MCMF):** Successive shortest path algorithm using SPFA.
- **Applications:** Academic exam invigilator scheduling and project selection with prerequisite closures.

### 🔹 Module 5: NP-Completeness, Reductions & Approximation
- **Karp Reductions Pipeline:**
  - $\text{3-SAT} \le_p \text{CLIQUE}$: Implemented in `reduce3SatToClique` mapping clauses to graph vertices and valid compatibility edges.
  - $\text{CLIQUE} \le_p \text{INDEPENDENT SET}$: Implemented in `reduceCliqueToIndependentSet` via graph complementation.
  - $\text{INDEPENDENT SET} \le_p \text{VERTEX COVER}$: Theoretical proof that $S$ is an independent set iff $V \setminus S$ is a vertex cover.
- **Polynomial-Time Certificate Checkers:** Verifies NP certificates for 3-SAT, Subset-Sum, and Vertex Cover in polynomial time.
- **Vertex Cover 2-Approximation:** Maximal matching algorithm guaranteeing $|C| = 2|M| \le 2 \times \text{OPT}$.
- **Greedy Knapsack:** 0.5-approximation guarantee for resource allocation.
- **Fixed-Parameter Tractability (FPT):** Exact bounded search tree solving Vertex Cover in $O(2^k \cdot V)$ for small parameter $k$.

### 🔹 Module 6: Randomised & Parallel Algorithms
- **QuickSort Paradigm Analysis:** Deterministic vs. Randomized vs. Median-of-Three pivot selection on sorted, reverse-sorted, and randomized student lists.
- **Universal Hashing:** 2-independent hash family matrix testing load distribution and collision rates.
- **Reservoir Sampling (Algorithm R):** Uniform single-pass random sampling of streaming student records in $O(k)$ space.
- **Work-Efficient Parallel Prefix Scan (Blelloch):** Work $O(n)$, Span $O(\log n)$ up-sweep and down-sweep tree reductions.
- **Brent's Scheduling Theorem:** Work-depth analysis computing speedup and processor efficiency $T_p \le \frac{W - S}{p} + S$.

---

## 📊 Analytical Complexity Reference

| Algorithm | Category | Asymptotic Time Complexity | Role in EduTrack |
| :--- | :--- | :--- | :--- |
| **Naive String Search** | Strings | $O(nm)$ | Baseline comparison |
| **Knuth-Morris-Pratt (KMP)** | Strings | $O(n + m)$ | Attribute search with line/column coordinates |
| **Z-Algorithm** | Strings | $O(n + m)$ | Linear cross-validation |
| **Rabin-Karp** | Strings | Avg $O(n + m)$, Worst $O(nm)$ | Rolling hash with collision analysis |
| **Aho-Corasick** | Strings | $O(n + m + z)$ | Multi-keyword single-pass dictionary search |
| **Suffix Array (Doubling)** | Suffix Structures | $O(n \log^2 n)$ | Suffix indexing without pointers |
| **Kasai LCP** | Suffix Structures | $O(n)$ | Longest repeated & common substrings |
| **Levenshtein / Damerau** | Dynamic Programming | $O(nm)$ | Query typo tolerance and fuzzy matching |
| **Matrix-Chain Multiplication** | Dynamic Programming | $O(n^3)$ | Optimal evaluation ordering |
| **Bitmask TSP (Held-Karp)** | Dynamic Programming | $O(2^n \cdot n^2)$ | Campus inspection minimum tour |
| **Tree DP (Rerooting)** | Dynamic Programming | $O(n)$ | Diameter, centroid, max independent set |
| **SOS DP** | Dynamic Programming | $O(k \cdot 2^k)$ | Academic sub-profile bundle aggregation |
| **Edmonds-Karp** | Network Flow | $O(V \cdot E^2)$ | Augmenting path max-flow |
| **Dinic's Algorithm** | Network Flow | $O(V^2 \cdot E)$ | Level-graph blocking flow |
| **Vertex Cover 2-Approx** | Approximation | $O(V^2)$ | Maximal matching bound $|C| \le 2 \times \text{OPT}$ |
| **Blelloch Parallel Scan** | Parallel | Work $O(n)$, Span $O(\log n)$ | Work-efficient parallel prefix sums |

---

## 💻 How to Run the Project

### Option A: Using Eclipse IDE (Recommended)
1. Open Eclipse IDE.
2. Select **File > Import... > Existing Projects into Workspace**.
3. Browse and select this `EduTrack` folder as the root directory.
4. Click **Finish**.
5. Navigate to `src/edutrack/cli/EduTrackMain.java`.
6. Right-click and choose **Run As > Java Application**.

### Option B: Using Windows Command Prompt / Batch File
Double-click `run.bat` or execute in command line:
```powershell
# From EduTrack directory:
java -cp bin edutrack.cli.EduTrackMain
```

### Option C: Recompile from Source
```powershell
javac -d bin src/edutrack/core/*.java src/edutrack/module1/*.java src/edutrack/module2/*.java src/edutrack/module3/*.java src/edutrack/module4/*.java src/edutrack/module5/*.java src/edutrack/module6/*.java src/edutrack/cli/*.java
java -cp bin edutrack.cli.EduTrackMain
```

---

## 📂 Project Directory Structure

```
EduTrack/
├── .classpath                   # Eclipse classpath configuration
├── .project                     # Eclipse project configuration
├── .gitignore                   # Git ignore patterns (ignores compiled binaries)
├── README.md                    # Project documentation & architecture manual
├── run.bat                      # Windows launcher script
├── data/
│   ├── student-mat.csv          # Mathematics course dataset (649 records)
│   └── student-por.csv          # Portuguese course dataset (649 records)
├── docs/
│   └── EduTrack_DSA_Review1.pdf # Full Review-1 Presentation Deck
└── src/
    └── edutrack/
        ├── cli/
        │   └── EduTrackMain.java                 # Interactive terminal loop & benchmark
        ├── core/
        │   ├── CustomIntArray.java               # Custom dynamic int array
        │   ├── CustomIntQueue.java               # Custom circular queue
        │   ├── CustomMatchList.java              # Custom match coordinate container
        │   ├── CustomStringArray.java            # Custom dynamic string array
        │   ├── DataLoader.java                   # Custom CSV reader & parser
        │   ├── SortSearch.java                   # Custom sorting and binary search
        │   ├── Student.java                      # Student record model
        │   └── TextMatch.java                    # Text coordinate match model
        ├── module1/
        │   └── Module1Engine.java                # Miller-Rabin, QR, Asymptotic Matrix
        ├── module2/
        │   └── Module2StringEngine.java          # KMP, Z, RK, Aho-Corasick, SA, LCP
        ├── module3/
        │   └── Module3DPEngine.java              # Edit Dist, Alignment, MCM, TSP, Tree, SOS
        ├── module4/
        │   └── Module4FlowEngine.java            # Edmonds-Karp, Dinic, Matching, MCMF
        ├── module5/
        │   └── Module5NPEngine.java               # 3SAT-Clique-IS Reductions, 2-Approx, FPT
        └── module6/
            └── Module6RandomParallelEngine.java  # QuickSort, Hashing, Reservoir, Blelloch
```

---

## 📜 Review Presentation Slides
The complete presentation deck prepared for the review is stored in [`docs/EduTrack_DSA_Review1.pdf`](docs/EduTrack_DSA_Review1.pdf).

---
*KL University (Koneru Lakshmaiah Education Foundation) — Department of Computer Science & Engineering*
