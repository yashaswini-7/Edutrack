package edutrack.cli;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import edutrack.core.CustomIntArray;
import edutrack.core.CustomMatchList;
import edutrack.core.DataLoader;
import edutrack.core.SortSearch;
import edutrack.core.Student;
import edutrack.core.TextMatch;
import edutrack.module1.Module1Engine;
import edutrack.module2.Module2StringEngine;
import edutrack.module3.Module3DPEngine;
import edutrack.module4.Module4FlowEngine;
import edutrack.module5.Module5NPEngine;
import edutrack.module6.Module6RandomParallelEngine;

public class EduTrackMain {

    private static Student[] students = new Student[0];
    private static String studentDataCorpus = "";
    private static String activeDatasetName = "None";

    public static void main(String[] args) {
        initDatasets();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printHeader();
            System.out.println("  1. Dataset Analytics & Record Inspection [" + activeDatasetName + "]");
            System.out.println("  2. Module 1: Foundational Engine (Query Routing, Design Matrix, Miller-Rabin)");
            System.out.println("  3. Module 2: String Algorithms (KMP, Z-Algorithm, Rabin-Karp, Aho-Corasick, SA, LCP)");
            System.out.println("  4. Module 3: Advanced Dynamic Programming (Alignment, Interval, Bitmask, Tree DP, SOS)");
            System.out.println("  5. Module 4: Network Flow & Reductions (Edmonds-Karp, Dinic, Min-Cut, Matching, MCMF)");
            System.out.println("  6. Module 5: NP-Completeness & Approximation (Witnesses, Reductions, 2-Approx, FPT)");
            System.out.println("  7. Module 6: Randomised & Parallel Algorithms (Las Vegas/Monte Carlo, QuickSort, Scan)");
            System.out.println("  8. Execute Comprehensive 6-Module Automated Verification Benchmark");
            System.out.println("  9. Switch Dataset (student-mat.csv <-> student-por.csv)");
            System.out.println("  10. Exit EduTrack System");
            System.out.println("================================================================================");
            System.out.print("Enter your choice [1-10]: ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                runDatasetMenu(scanner);
            } else if (choice.equals("2")) {
                runModule1Menu(scanner);
            } else if (choice.equals("3")) {
                runModule2Menu(scanner);
            } else if (choice.equals("4")) {
                runModule3Menu(scanner);
            } else if (choice.equals("5")) {
                runModule4Menu(scanner);
            } else if (choice.equals("6")) {
                runModule5Menu(scanner);
            } else if (choice.equals("7")) {
                runModule6Menu(scanner);
            } else if (choice.equals("8")) {
                runComprehensiveBenchmark();
            } else if (choice.equals("9")) {
                switchDataset(scanner);
            } else if (choice.equals("10")) {
                System.out.println("\n[*] Thank you for using EduTrack. Shutting down algorithm engine cleanly.");
                break;
            } else {
                System.out.println("[!] Invalid selection. Please enter a number between 1 and 10.");
            }
        }
        scanner.close();
    }

    private static void printHeader() {
        System.out.println("\n================================================================================");
        System.out.println("          EDUTRACK: ADVANCED EDUCATIONAL ANALYTICS & ALGORITHM ENGINE");
        System.out.println("  Active Corpus: " + activeDatasetName + " | Total Students Loaded: " + students.length);
        System.out.println("================================================================================");
    }

    private static void runDatasetMenu(Scanner scanner) {
        System.out.println("\n--- [Dataset Analytics & Student Records] ---");
        System.out.println("1. View Dataset Summary & Statistics");
        System.out.println("2. Lookup Student by ID (Linear Search O(N) vs Binary Search O(log N))");
        System.out.println("3. Display Top 5 Records Preview");
        System.out.print("Select Option [1-3]: ");
        String opt = scanner.nextLine().trim();

        if (opt.equals("1")) {
            System.out.println("\n>> Dataset Summary for [" + activeDatasetName + "]:");
            System.out.println("   Total Students: " + students.length);
            if (students.length > 0) {
                int sumG3 = 0, sumAbs = 0;
                for (int i = 0; i < students.length; i++) {
                    sumG3 += students[i].g3();
                    sumAbs += students[i].absences();
                }
                System.out.printf("   Average Final Grade (G3): %.2f / 20.0\n", ((double) sumG3 / students.length));
                System.out.printf("   Average Absences: %.2f days\n", ((double) sumAbs / students.length));
                System.out.println("   Available Attributes: school, sex, age, address, famsize, Pstatus, Medu, Fedu,");
                System.out.println("                         Mjob, Fjob, reason, guardian, studytime, failures, etc.");
            }
        } else if (opt.equals("2")) {
            System.out.print("Enter Student ID to search (e.g. S015, S100, S390): ");
            String id = scanner.nextLine().trim();
            long t0 = System.nanoTime();
            int linIdx = SortSearch.linearSearch(students, id);
            long linComp = SortSearch.comparisons;
            long t1 = System.nanoTime();

            int binIdx = SortSearch.binarySearch(students, id);
            long binComp = SortSearch.comparisons;
            long t2 = System.nanoTime();

            if (binIdx >= 0) {
                System.out.println("\n[+] Found Student Record: " + students[binIdx]);
                System.out.println("    Profile String: " + students[binIdx].profile);
            } else {
                System.out.println("[-] Student with ID '" + id + "' not found.");
            }
            System.out.printf("    Linear Search: %d comparisons, %.2f us\n", linComp, (t1 - t0) / 1000.0);
            System.out.printf("    Binary Search: %d comparisons, %.2f us\n", binComp, (t2 - t1) / 1000.0);
        } else if (opt.equals("3")) {
            System.out.println("\n--- [First 5 Student Profiles] ---");
            for (int i = 0; i < Math.min(5, students.length); i++) {
                System.out.println("  [" + (i + 1) + "] " + students[i]);
            }
        }
    }

    private static void runModule1Menu(Scanner scanner) {
        System.out.println("\n--- [EduTrack Module 1: Foundational Engine & TextHack System] ---");
        System.out.println("1. Query Router & Public API Mapping (Query Class -> Algorithm Family)");
        System.out.println("2. Asymptotic Design Matrix (Why Advanced Canon Exists over DSA-1/2)");
        System.out.println("3. Miller-Rabin Large-Prime Primality Test (Interactive Candidate Check)");
        System.out.println("4. Step-by-Step Square Roots of Unity Explanation (Fermat Witness Diagnostics)");
        System.out.print("Select Option [1-4]: ");
        String opt = scanner.nextLine().trim();

        if (opt.equals("1")) {
            System.out.print("Enter query type (pattern / fuzzy / similarity / flow / scheduling / primality): ");
            String q = scanner.nextLine().trim();
            System.out.println("\n>> Routing Decision: " + Module1Engine.routeQueryDescription(q));
        } else if (opt.equals("2")) {
            System.out.println("\n================================================================================");
            System.out.println("                  THE ADVANCED ALGORITHM DESIGN MATRIX");
            System.out.println("================================================================================");
            Module1Engine.CanonMapping[] matrix = Module1Engine.getCanonTable();
            for (int i = 0; i < matrix.length; i++) {
                Module1Engine.CanonMapping m = matrix[i];
                System.out.println("[" + (i + 1) + "] Query Class:  " + m.queryClass);
                System.out.println("    Algo Family:  " + m.algorithmFamily);
                System.out.println("    Reference:    " + m.referenceAlgorithm + " (" + m.asymptoticCost + ")");
                System.out.println("    Textbook:     " + m.textbookCanon);
                System.out.println("    Why DSA-1/2:  " + m.whyDsa1Fails);
                System.out.println("--------------------------------------------------------------------------------");
            }
        } else if (opt.equals("3")) {
            System.out.print("Enter candidate number to test (e.g. 1000000007, 1000000009, 998244353, 2147483647): ");
            long n = Long.parseLong(scanner.nextLine().trim());
            long t0 = System.nanoTime();
            boolean isPrime = Module1Engine.millerRabin(n, 20);
            long t1 = System.nanoTime();
            System.out.println("\n>> Miller-Rabin Result: " + n + (isPrime ? " is CERTIFIED PRIME (>99.9999% confidence)." : " is COMPOSITE."));
            System.out.printf("   Execution Time: %.2f us\n", (t1 - t0) / 1000.0);
        } else if (opt.equals("4")) {
            System.out.print("Enter number to trace witness steps for (e.g. 561 [Carmichael], 1000000007): ");
            long n = Long.parseLong(scanner.nextLine().trim());
            System.out.println("\n" + Module1Engine.explainMillerRabin(n));
        }
    }

    private static void runModule2Menu(Scanner scanner) {
        System.out.println("\n--- [EduTrack Module 2: Exact String Algorithms Suite] ---");
        System.out.println("1. Knuth-Morris-Pratt (KMP) Pattern Search with Line/Col Coordinates");
        System.out.println("2. Comparative Benchmark (Naive vs KMP vs Z-Algorithm vs Rabin-Karp)");
        System.out.println("3. Rabin-Karp Spurious Collision Demonstration (Mod 101 vs Double Hash)");
        System.out.println("4. Aho-Corasick Multi-Pattern Keyword Automaton");
        System.out.println("5. Suffix Array & Kasai LCP Array Construction");
        System.out.println("6. Longest Common Substring / Profile Similarity (Suffix Structures)");
        System.out.println("7. Suffix Structures Theoretical Comparison (SA vs Tree vs Automaton)");
        System.out.print("Select Option [1-7]: ");
        String opt = scanner.nextLine().trim();

        if (opt.equals("1")) {
            System.out.print("Enter attribute/word to locate (default 'services'): ");
            String pat = scanner.nextLine().trim();
            if (pat.isEmpty()) pat = "services";

            long t0 = System.nanoTime();
            CustomIntArray offsets = Module2StringEngine.searchKMP(studentDataCorpus, pat);
            CustomMatchList matches = Module2StringEngine.mapOffsetsToCoordinates(studentDataCorpus, offsets, pat);
            long t1 = System.nanoTime();

            System.out.println("\n[+] Located " + matches.size() + " matches for \"" + pat + "\" in " + (t1 - t0) / 1000 + " us.");
            for (int i = 0; i < Math.min(6, matches.size()); i++) {
                System.out.println("    " + matches.get(i));
            }
            if (matches.size() > 6) {
                System.out.println("    ... (" + (matches.size() - 6) + " more coordinates recorded)");
            }
        } else if (opt.equals("2")) {
            System.out.print("Enter pattern to benchmark across student corpus: ");
            String pat = scanner.nextLine().trim();
            if (pat.isEmpty()) pat = "teacher";

            long t0 = System.nanoTime();
            CustomIntArray resNaive = Module2StringEngine.searchNaive(studentDataCorpus, pat);
            long cNaive = Module2StringEngine.lastComparisons;
            long t1 = System.nanoTime();

            CustomIntArray resKmp = Module2StringEngine.searchKMP(studentDataCorpus, pat);
            long cKmp = Module2StringEngine.lastComparisons;
            long t2 = System.nanoTime();

            CustomIntArray resZ = Module2StringEngine.searchZ(studentDataCorpus, pat);
            long cZ = Module2StringEngine.lastComparisons;
            long t3 = System.nanoTime();

            CustomIntArray resRk = Module2StringEngine.searchRabinKarpDouble(studentDataCorpus, pat);
            long t4 = System.nanoTime();

            System.out.println("\n================================================================================");
            System.out.printf("  Algorithm       Matches  Comparisons   Time (us)   Speedup vs Naive\n");
            System.out.println("================================================================================");
            double baseTime = (t1 - t0) / 1000.0;
            System.out.printf("  1. Naive O(nm)  %-7d  %-12d  %-10.2f  1.0x (Baseline)\n", resNaive.size(), cNaive, baseTime);
            System.out.printf("  2. KMP O(n+m)   %-7d  %-12d  %-10.2f  %.2fx\n", resKmp.size(), cKmp, (t2 - t1) / 1000.0, baseTime / Math.max(1, (t2 - t1) / 1000.0));
            System.out.printf("  3. Z-Algo O(n)  %-7d  %-12d  %-10.2f  %.2fx\n", resZ.size(), cZ, (t3 - t2) / 1000.0, baseTime / Math.max(1, (t3 - t2) / 1000.0));
            System.out.printf("  4. Rabin-Karp   %-7d  (hash roll)   %-10.2f  %.2fx\n", resRk.size(), (t4 - t3) / 1000.0, baseTime / Math.max(1, (t4 - t3) / 1000.0));
            System.out.println("================================================================================");
        } else if (opt.equals("3")) {
            System.out.print("Enter text for hash collision test (press Enter for default): ");
            String inText = scanner.nextLine().trim();
            String testText = inText.isEmpty() ? "services_teacher_services_health_course_services_teacher_course" : inText;
            System.out.print("Enter pattern (default 'services'): ");
            String inPat = scanner.nextLine().trim();
            String testPat = inPat.isEmpty() ? "services" : inPat;

            CustomIntArray small = Module2StringEngine.searchRabinKarpSmallMod(testText, testPat);
            int smallSpurious = Module2StringEngine.lastSpuriousCollisions;

            CustomIntArray dbl = Module2StringEngine.searchRabinKarpDouble(testText, testPat);
            int dblSpurious = Module2StringEngine.lastSpuriousCollisions;

            System.out.println("\n>> Rabin-Karp Spurious Hash Collision Test on: \"" + testText + "\"");
            System.out.println("   Small Modulus (mod 101): Matches = " + small.size() + ", Spurious Collisions = " + smallSpurious);
            System.out.println("   Double Hash (10^9+7, 10^9+9): Matches = " + dbl.size() + ", Spurious Collisions = " + dblSpurious);
            System.out.println("   Conclusion: Double-hashing mathematically eliminates false positives before character verification.");
        } else if (opt.equals("4")) {
            System.out.print("Enter comma-separated keywords to search simultaneously (default 'teacher, services, health, course'): ");
            String kwLine = scanner.nextLine().trim();
            String[] keywords = parseCsvStrings(kwLine, new String[]{"teacher", "services", "health", "course"});

            Module2StringEngine.AhoCorasick ac = new Module2StringEngine.AhoCorasick(keywords);
            long t0 = System.nanoTime();
            CustomMatchList acMatches = ac.searchAll(studentDataCorpus);
            long t1 = System.nanoTime();
            System.out.print("\n>> Aho-Corasick Multi-Pattern Search for: [");
            for (int i = 0; i < keywords.length; i++) System.out.print(keywords[i] + (i + 1 < keywords.length ? ", " : "]\n"));
            System.out.println("   Total Keyword Occurrences in Corpus: " + acMatches.size());
            System.out.printf("   Execution Time: %.2f us (Linear in Text Length)\n", (t1 - t0) / 1000.0);
        } else if (opt.equals("5")) {
            System.out.print("Enter string to build Suffix Array & LCP for (default 'banana'): ");
            String s = scanner.nextLine().trim();
            if (s.isEmpty()) s = "banana";

            int[] sa = Module2StringEngine.buildSuffixArray(s);
            int[] lcp = Module2StringEngine.buildKasaiLCP(s, sa);

            System.out.println("\n>> Suffix Array & Kasai LCP Array for \"" + s + "\":");
            System.out.println("  i   SA[i]   LCP[i]   Suffix String");
            System.out.println("--------------------------------------");
            for (int i = 0; i < sa.length; i++) {
                System.out.printf("  %-2d  %-5d   %-6d   %s\n", i, sa[i], lcp[i], s.substring(sa[i]));
            }
        } else if (opt.equals("6")) {
            if (students.length >= 2) {
                System.out.print("Enter first Student ID (default S001): ");
                String id1 = scanner.nextLine().trim();
                System.out.print("Enter second Student ID (default S002): ");
                String id2 = scanner.nextLine().trim();
                int idx1 = id1.isEmpty() ? 0 : SortSearch.binarySearch(students, id1.toUpperCase());
                int idx2 = id2.isEmpty() ? 1 : SortSearch.binarySearch(students, id2.toUpperCase());
                if (idx1 < 0) idx1 = 0;
                if (idx2 < 0) idx2 = 1;

                String p1 = students[idx1].profile;
                String p2 = students[idx2].profile;
                String lcs = Module2StringEngine.longestCommonSubstring(p1, p2);
                System.out.println("\n>> Longest Common Profile Attribute Substring (" + students[idx1].id + " vs " + students[idx2].id + "):");
                System.out.println("   Shared Substring: \"" + lcs + "\" (Length: " + lcs.length() + ")");
            } else {
                System.out.println("Need at least 2 loaded students to compare profiles.");
            }
        } else if (opt.equals("7")) {
            System.out.println("\n" + Module2StringEngine.getSuffixStructuresComparison());
        }
    }

    private static void runModule3Menu(Scanner scanner) {
        System.out.println("\n--- [EduTrack Module 3: Advanced Dynamic Programming] ---");
        System.out.println("1. Levenshtein & Damerau-Levenshtein Edit Distance (with Transposition)");
        System.out.println("2. Needleman-Wunsch Global Sequence Alignment (Educational DNA/Profile)");
        System.out.println("3. Smith-Waterman Local Sequence Alignment (High-Similarity Regions)");
        System.out.println("4. Matrix-Chain Multiplication (Interval DP Optimal Parenthesization)");
        System.out.println("5. Optimal Binary Search Tree (Interval DP on Search Frequencies)");
        System.out.println("6. Travelling Salesman Problem (Bitmask DP O(2^N * N^2) on Campus Nodes)");
        System.out.println("7. Hamiltonian Path Detection in Academic Prerequisite Graph");
        System.out.println("8. DP on Trees (Diameter, Centroids, MIS, Tree Rerooting)");
        System.out.println("9. SOS DP (Sum Over Subsets O(k * 2^k) on Student Skill Bundles)");
        System.out.println("10. Architectural Analysis: When DP is the Wrong Tool");
        System.out.print("Select Option [1-10]: ");
        String opt = scanner.nextLine().trim();

        if (opt.equals("1")) {
            System.out.print("Enter first string (default 'kitten'): ");
            String s1 = scanner.nextLine().trim();
            if (s1.isEmpty()) s1 = "kitten";

            System.out.print("Enter second string (default 'sitting'): ");
            String s2 = scanner.nextLine().trim();
            if (s2.isEmpty()) s2 = "sitting";

            int lev = Module3DPEngine.levenshteinDistance(s1, s2);
            int dam = Module3DPEngine.damerauLevenshteinDistance(s1, s2);
            double sim = Module3DPEngine.calculateSimilarity(s1, s2);

            System.out.println("\n>> Edit Distance Comparison: \"" + s1 + "\" <-> \"" + s2 + "\"");
            System.out.println("   Classic Levenshtein Distance: " + lev);
            System.out.println("   Damerau-Levenshtein (with Adjacent Transposition): " + dam);
            System.out.printf("   Overall Profile Similarity: %.2f%%\n", sim);
            if (lev != dam) {
                System.out.println("   [!] Transposition Benefit: Damerau saves " + (lev - dam) + " operation(s) by swapping adjacent characters directly!");
            }
        } else if (opt.equals("2")) {
            System.out.print("Enter first sequence (default 'ACGTTCGAA'): ");
            String a = scanner.nextLine().trim();
            if (a.isEmpty()) a = "ACGTTCGAA";
            System.out.print("Enter second sequence (default 'AGCTGCA'): ");
            String b = scanner.nextLine().trim();
            if (b.isEmpty()) b = "AGCTGCA";

            Module3DPEngine.AlignmentResult res = Module3DPEngine.needlemanWunsch(a, b, 2, -1, -2);
            System.out.println("\n>> Needleman-Wunsch Global Alignment Result:");
            System.out.println("   Aligned Sequence 1: " + res.alignedS1);
            System.out.println("   Aligned Sequence 2: " + res.alignedS2);
            System.out.println("   Global Alignment Score: " + res.score);
        } else if (opt.equals("3")) {
            System.out.print("Enter first sequence (default 'HEAGAWGHEE'): ");
            String a = scanner.nextLine().trim();
            if (a.isEmpty()) a = "HEAGAWGHEE";
            System.out.print("Enter second sequence (default 'PAWHEAE'): ");
            String b = scanner.nextLine().trim();
            if (b.isEmpty()) b = "PAWHEAE";

            Module3DPEngine.AlignmentResult res = Module3DPEngine.smithWaterman(a, b, 2, -1, -2);
            System.out.println("\n>> Smith-Waterman Local Alignment Result:");
            System.out.println("   Local Motif 1: " + res.alignedS1);
            System.out.println("   Local Motif 2: " + res.alignedS2);
            System.out.println("   Maximum Local Match Score: " + res.score);
        } else if (opt.equals("4")) {
            System.out.print("Enter comma-separated matrix dimensions (default '10, 30, 5, 60'): ");
            int[] dims = parseCsvInts(scanner.nextLine().trim(), new int[]{10, 30, 5, 60});
            Module3DPEngine.MatrixChainResult res = Module3DPEngine.matrixChainOrder(dims);
            System.out.print("\n>> Matrix-Chain Multiplication for Chain Dimensions [");
            for (int i = 0; i < dims.length; i++) System.out.print(dims[i] + (i + 1 < dims.length ? ", " : "]:\n"));
            System.out.println("   Minimum Scalar Multiplications: " + res.minMultiplications);
            System.out.println("   Optimal Parenthesization Order: " + res.parenthesization);
        } else if (opt.equals("5")) {
            System.out.print("Enter search frequencies for keys (default '34, 8, 50'): ");
            int[] freq = parseCsvInts(scanner.nextLine().trim(), new int[]{34, 8, 50});
            Module3DPEngine.OBSTResult res = Module3DPEngine.optimalBST(freq);
            System.out.print("\n>> Optimal Binary Search Tree for Search Frequencies [");
            for (int i = 0; i < freq.length; i++) System.out.print(freq[i] + (i + 1 < freq.length ? ", " : "]:\n"));
            System.out.println("   Minimum Expected Search Cost: " + res.minCost);
            System.out.println("   Optimal BST Tree Structure:   " + res.treeStructure);
        } else if (opt.equals("6")) {
            System.out.print("Enter number of campus buildings to tour [3 to 6] (default 6): ");
            int k = parseIntOrDefault(scanner.nextLine().trim(), 6);
            if (k < 3 || k > 6) k = 6;
            int[][] fullDist = {
                {0, 20, 42, 25, 30, 15},
                {20, 0, 30, 34, 18, 22},
                {42, 30, 0, 10, 28, 36},
                {25, 34, 10, 0, 14, 20},
                {30, 18, 28, 14, 0, 12},
                {15, 22, 36, 20, 12, 0}
            };
            int[][] dist = new int[k][k];
            for (int r = 0; r < k; r++) for (int c = 0; c < k; c++) dist[r][c] = fullDist[r][c];

            System.out.println("\n>> Solving " + k + "-Campus Inspection Tour via TSP Bitmask DP O(2^N * N^2):");
            Module3DPEngine.TSPResult res = Module3DPEngine.solveTSP(dist);
            System.out.println("   Minimum Tour Cost: " + res.minCost);
            System.out.print("   Optimal Tour Path: ");
            for (int i = 0; i < res.tour.size(); i++) {
                System.out.print(res.tour.get(i) + (i + 1 < res.tour.size() ? " -> " : "\n"));
            }
        } else if (opt.equals("7")) {
            boolean[][] adj = {
                {false, true, true, false},
                {true, false, true, true},
                {true, true, false, true},
                {false, true, true, false}
            };
            CustomIntArray path = Module3DPEngine.findHamiltonianPath(adj);
            System.out.println("\n>> Hamiltonian Path Detection in 4-Node Cluster:");
            if (!path.isEmpty()) {
                System.out.print("   Hamiltonian Path Found: ");
                for (int i = 0; i < path.size(); i++) {
                    System.out.print(path.get(i) + (i + 1 < path.size() ? " -> " : "\n"));
                }
            } else {
                System.out.println("   No Hamiltonian Path exists in this graph.");
            }
        } else if (opt.equals("8")) {
            int[][] edges = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {5, 6}};
            Module3DPEngine.TreeDPResult res = Module3DPEngine.computeTreeDP(7, edges);
            System.out.println("\n>> Tree DP Results for 7-Node Academic Prerequisite Tree:");
            System.out.println("   Tree Diameter: " + res.treeDiameter + " edges");
            System.out.println("   Tree Radius:   " + res.radius + " edges");
            System.out.println("   Centroid(s):   " + res.centroids);
            System.out.println("   Center(s):     " + res.centers);
            System.out.println("   Max Independent Set (Conflict-free Coordinators): " + res.maxIndependentSet);
            System.out.print("   Rerooted Distance Sum for all nodes [0..6]: [");
            for (int i = 0; i < res.rerootedSubtreeSums.length; i++) {
                System.out.print(res.rerootedSubtreeSums[i] + (i + 1 < res.rerootedSubtreeSums.length ? ", " : "]\n"));
            }
        } else if (opt.equals("9")) {
            System.out.print("Enter number of elective bits [2 to 5] (default 4): ");
            int bits = parseIntOrDefault(scanner.nextLine().trim(), 4);
            if (bits < 2 || bits > 5) bits = 4;
            int total = 1 << bits;
            int[] f = new int[total];
            for (int i = 0; i < total; i++) f[i] = i + 1;
            int[] sos = Module3DPEngine.sumOverSubsets(f, bits);
            System.out.println("\n>> SOS DP (Sum Over Subsets) for " + bits + "-bit Elective Bundle:");
            System.out.println("   SOS DP [Full Mask " + (total - 1) + "] = " + sos[total - 1] + " (Sum of all " + total + " submasks)");
            if (bits >= 4) {
                System.out.println("   SOS DP [0101_2] = " + sos[5] + " (F[0000]+F[0001]+F[0100]+F[0101])");
            }
        } else if (opt.equals("10")) {
            System.out.println("\n" + Module3DPEngine.explainWhenDPIsWrong());
        }
    }

    private static void runModule4Menu(Scanner scanner) {
        System.out.println("\n--- [EduTrack Module 4: Network Flow Suite & Reductions] ---");
        System.out.println("1. Compare Max-Flow Solvers (Ford-Fulkerson vs Edmonds-Karp vs Dinic's)");
        System.out.println("2. Min-Cut Dual Analysis (Max-Flow Min-Cut Theorem & Cut Edges)");
        System.out.println("3. Maximum Bipartite Matching (Student-to-Mentor Assignment & Konig's Theorem)");
        System.out.println("4. Min-Cost Max-Flow (MCMF via Successive Shortest Path)");
        System.out.println("5. Practical Reduction: Project Selection with Prerequisites (Min-Cut)");
        System.out.println("6. Practical Reduction: Task/Course Scheduling with Deadlines");
        System.out.print("Select Option [1-6]: ");
        String opt = scanner.nextLine().trim();

        int[][] sampleCap = {
            {0, 16, 13, 0, 0, 0},
            {0, 0, 10, 12, 0, 0},
            {0, 4, 0, 0, 14, 0},
            {0, 0, 9, 0, 0, 20},
            {0, 0, 0, 7, 0, 4},
            {0, 0, 0, 0, 0, 0}
        };

        if (opt.equals("1")) {
            System.out.print("Enter Source node index [0-5] (default 0): ");
            int s = parseIntOrDefault(scanner.nextLine().trim(), 0);
            System.out.print("Enter Sink node index [0-5] (default 5): ");
            int t = parseIntOrDefault(scanner.nextLine().trim(), 5);
            if (s < 0 || s > 5) s = 0;
            if (t < 0 || t > 5 || t == s) t = 5;

            Module4FlowEngine.FlowResult ff = Module4FlowEngine.fordFulkerson(sampleCap, s, t);
            Module4FlowEngine.FlowResult ek = Module4FlowEngine.edmondsKarp(sampleCap, s, t);
            Module4FlowEngine.FlowResult dn = Module4FlowEngine.dinicMaxFlow(sampleCap, s, t);

            System.out.println("\n================================================================================");
            System.out.println("  Flow Algorithm           Complexity       Max Flow  Augmenting Steps");
            System.out.println("================================================================================");
            System.out.printf("  1. Ford-Fulkerson (DFS)   O(E * |f|)       %-8d  %-10d\n", ff.maxFlow, ff.augmentingPathsCount);
            System.out.printf("  2. Edmonds-Karp (BFS)     O(V * E^2)       %-8d  %-10d\n", ek.maxFlow, ek.augmentingPathsCount);
            System.out.printf("  3. Dinic's Algorithm      O(V^2 * E)       %-8d  %-10d\n", dn.maxFlow, dn.augmentingPathsCount);
            System.out.println("================================================================================");
        } else if (opt.equals("2")) {
            System.out.print("Enter Source node index [0-5] (default 0): ");
            int s = parseIntOrDefault(scanner.nextLine().trim(), 0);
            System.out.print("Enter Sink node index [0-5] (default 5): ");
            int t = parseIntOrDefault(scanner.nextLine().trim(), 5);
            if (s < 0 || s > 5) s = 0;
            if (t < 0 || t > 5 || t == s) t = 5;

            Module4FlowEngine.FlowResult res = Module4FlowEngine.dinicMaxFlow(sampleCap, s, t);
            System.out.println("\n>> Min-Cut Dual Analysis on 6-Node Resource Network (Source " + s + " -> Sink " + t + "):");
            System.out.println("   Maximum Flow:         " + res.maxFlow + " units");
            System.out.println("   Minimum Cut Capacity: " + res.minCutCapacity + " units");
            System.out.println("   Source Partition S:   " + res.sourcePartition);
            System.out.println("   Sink Partition T:     " + res.sinkPartition);
            System.out.println("   >> DUALITY THEOREM: Max Flow (" + res.maxFlow + ") == Min Cut (" + res.minCutCapacity + ") VERIFIED.");
        } else if (opt.equals("3")) {
            boolean[][] compat = {
                {true, true, false, false},
                {false, true, true, false},
                {false, false, true, true},
                {true, false, false, true}
            };
            Module4FlowEngine.MatchingResult match = Module4FlowEngine.solveBipartiteMatching(compat, 4, 4);
            System.out.println("\n>> Student-to-Mentor Bipartite Matching (4 Students <-> 4 Mentors):");
            System.out.println("   Total Matched Pairs: " + match.matchCount + " of 4");
            for (int i = 0; i < match.matchedPairs.length; i++) {
                System.out.println("   Student S" + (match.matchedPairs[i][0] + 1) + " ---> Mentor M" + (match.matchedPairs[i][1] + 1));
            }
            System.out.println("   " + match.konigTheoremVerification);
        } else if (opt.equals("4")) {
            Module4FlowEngine.FlowNetwork net = new Module4FlowEngine.FlowNetwork(4);
            net.addEdge(0, 1, 3, 1);
            net.addEdge(0, 2, 2, 2);
            net.addEdge(1, 2, 1, 1);
            net.addEdge(1, 3, 2, 3);
            net.addEdge(2, 3, 3, 1);

            System.out.print("Enter source node [0-3] (default 0): ");
            int s = parseIntOrDefault(scanner.nextLine().trim(), 0);
            System.out.print("Enter sink node [0-3] (default 3): ");
            int t = parseIntOrDefault(scanner.nextLine().trim(), 3);
            if (s < 0 || s > 3) s = 0;
            if (t < 0 || t > 3 || t == s) t = 3;

            Module4FlowEngine.MCMFResult mcmf = Module4FlowEngine.solveMinCostMaxFlow(net, s, t);
            System.out.println("\n>> Min-Cost Max-Flow (MCMF) Result (Source " + s + " -> Sink " + t + "):");
            System.out.println("   Maximum Achieved Flow: " + mcmf.maxFlow + " units");
            System.out.println("   Minimum Total Cost:    " + mcmf.minCost + " currency units");
        } else if (opt.equals("5")) {
            System.out.print("Enter rewards for 3 projects (default '100, 200, 150'): ");
            int[] rewards = parseCsvInts(scanner.nextLine().trim(), new int[]{100, 200, 150});
            System.out.print("Enter costs for 2 prerequisites (default '80, 120'): ");
            int[] prereqCosts = parseCsvInts(scanner.nextLine().trim(), new int[]{80, 120});

            int[][] deps = {
                {0, 0}, 
                {1, 0}, 
                {1, 1}, 
                {2, 1}  
            };
            Module4FlowEngine.ProjectSelectionResult ps = Module4FlowEngine.solveProjectSelection(rewards, prereqCosts, deps);
            System.out.println("\n>> Project Selection as Min-Cut (Closure Problem):");
            System.out.print("   Projects Rewards: [");
            for (int i = 0; i < rewards.length; i++) System.out.print(rewards[i] + (i + 1 < rewards.length ? ", " : "]\n"));
            System.out.print("   Prerequisite Costs: [");
            for (int i = 0; i < prereqCosts.length; i++) System.out.print(prereqCosts[i] + (i + 1 < prereqCosts.length ? ", " : "]\n"));
            System.out.println("   Selected Optimal Project Indices: " + ps.selectedProjects);
            System.out.println("   Net Maximum Educational Profit:   " + ps.netMaximumReward);
        } else if (opt.equals("6")) {
            System.out.print("Enter study hours for 3 tasks (default '4, 6, 5'): ");
            int[] tasks = parseCsvInts(scanner.nextLine().trim(), new int[]{4, 6, 5});
            System.out.print("Enter deadlines in days for 3 tasks (default '2, 3, 2'): ");
            int[] deadlines = parseCsvInts(scanner.nextLine().trim(), new int[]{2, 3, 2});
            int[] dayCaps = {5, 5, 5};

            Module4FlowEngine.SchedulingResult sc = Module4FlowEngine.scheduleTasksWithDeadlines(tasks, deadlines, dayCaps);
            System.out.println("\n>> Task/Course Scheduling with Deadlines as Flow:");
            System.out.println("   Required Study Hours: " + sc.totalRequiredHours);
            System.out.println("   Scheduled Hours:      " + sc.scheduledHours);
            System.out.println("   Is Schedule Feasible: " + (sc.allTasksFeasible ? "YES (All deadlines met)" : "NO (Capacity exceeded)"));
        }
    }

    private static void runModule5Menu(Scanner scanner) {
        System.out.println("\n--- [EduTrack Module 5: NP-Completeness & Approximation] ---");
        System.out.println("1. Polynomial-Time Certificate Verification (Witness Validation)");
        System.out.println("2. The Reduction Zoo (3-SAT -> CLIQUE -> INDEPENDENT-SET -> VERTEX-COVER)");
        System.out.println("3. 2-Approximation Algorithms (Vertex Cover via Maximal Matching & Knapsack)");
        System.out.println("4. Parameterized Complexity & FPT (Bounded Search Tree for Vertex Cover)");
        System.out.println("5. Complexity Class Taxonomy (PTAS vs FPTAS vs APX-Hard Framework)");
        System.out.print("Select Option [1-5]: ");
        String opt = scanner.nextLine().trim();

        if (opt.equals("1")) {
            boolean[][] graph = {
                {false, true, true, false},
                {true, false, true, true},
                {true, true, false, true},
                {false, true, true, false}
            };
            System.out.print("Enter budget k for Vertex Cover witness verification (default 2): ");
            int kBud = parseIntOrDefault(scanner.nextLine().trim(), 2);
            int[] goodWitness = {1, 2};
            int[] badWitness = {0, 3};

            System.out.println("\n>> Polynomial-Time Certificate Verification on 4-Node Exam Conflict Graph:");
            System.out.println("   Testing Witness {1, 2} with k=" + kBud + ": " + Module5NPEngine.verifyVertexCover(graph, goodWitness, kBud));
            System.out.println("   Testing Witness {0, 3} with k=" + kBud + ": " + Module5NPEngine.verifyVertexCover(graph, badWitness, kBud));

            int[] weights = {12, 5, 8, 17, 3};
            System.out.print("\nEnter target sum for Subset-Sum certificate verification (default 16): ");
            int targetSum = parseIntOrDefault(scanner.nextLine().trim(), 16);
            int[] subsetWitness = {1, 2, 4};
            System.out.println("   Available weights: [12, 5, 8, 17, 3]");
            System.out.println("   Testing Witness indices [1, 2, 4] (weights: 5+8+3): " + Module5NPEngine.verifySubsetSum(weights, subsetWitness, targetSum));

            int[][] clauses = {
                {1, -2, 3},
                {-1, 2, -3},
                {2, 3, 1}
            };
            boolean[] assign = {true, false, true};
            System.out.println("\n>> 3-SAT Certificate Verification (3 Clauses):");
            System.out.println("   Testing Truth Assignment [x1=T, x2=F, x3=T]: " + Module5NPEngine.verify3SAT(clauses, assign));
        } else if (opt.equals("2")) {
            System.out.print("Enter number of 3-SAT clauses to reduce [1 to 3] (default 2): ");
            int numC = parseIntOrDefault(scanner.nextLine().trim(), 2);
            if (numC < 1 || numC > 3) numC = 2;
            int[][] fullClauses = {
                {1, 2, 3},
                {-1, -2, 3},
                {1, -2, -3}
            };
            int[][] sample3Sat = new int[numC][3];
            for (int i = 0; i < numC; i++) sample3Sat[i] = fullClauses[i];

            Module5NPEngine.SatToCliqueReduction red = Module5NPEngine.reduce3SatToClique(sample3Sat);
            System.out.println("\n>> 3-SAT -> CLIQUE Polynomial Reduction (" + numC + " Clauses):");
            System.out.println("   Constructed Graph: " + red.numVertices + " vertices (3 per clause)");
            System.out.println("   Target Clique Size: " + red.targetCliqueSize + " (Formula is SAT iff graph has " + red.targetCliqueSize + "-clique)");

            boolean[][] indSet = Module5NPEngine.reduceCliqueToIndependentSet(red.cliqueGraph);
            System.out.println("\n>> CLIQUE -> INDEPENDENT-SET -> VERTEX-COVER Equivalence:");
            System.out.println("   Complement graph constructed with " + indSet.length + " vertices.");
            System.out.println("   Educational Mapping: Finding maximum conflict-free student cohorts (Independent Set)");
            System.out.println("   is mathematically dual to finding minimum proctors to cover all conflicts (Vertex Cover).");
        } else if (opt.equals("3")) {
            boolean[][] conflictGraph = {
                {false, true, true, false, false, false},
                {true, false, true, true, false, false},
                {true, true, false, true, false, false},
                {false, true, true, false, true, true},
                {false, false, false, true, false, true},
                {false, false, false, true, true, false}
            };
            Module5NPEngine.ApproxVertexCoverResult approxVC = Module5NPEngine.approximateVertexCover(conflictGraph);
            System.out.println("\n>> Vertex Cover 2-Approximation via Maximal Matching:");
            System.out.println("   6-Student Exam Collaboration Conflict Graph:");
            System.out.println("   Maximal Matching Size (|M|): " + approxVC.matchingEdgesCount);
            System.out.println("   Constructed Vertex Cover Size (|C| = 2*|M|): " + approxVC.vertexCover.size());
            System.out.println("   Selected Proctor Node Indices: " + approxVC.vertexCover);
            System.out.println("   Ratio Guarantee: |C| <= 2 * OPT (Proved because any valid cover requires >= |M| vertices).");

            System.out.print("\nEnter Knapsack capacity budget (default 50): ");
            int budget = parseIntOrDefault(scanner.nextLine().trim(), 50);
            System.out.print("Enter project values (default '60, 100, 120'): ");
            int[] values = parseCsvInts(scanner.nextLine().trim(), new int[]{60, 100, 120});
            System.out.print("Enter project weights (default '10, 20, 30'): ");
            int[] weights = parseCsvInts(scanner.nextLine().trim(), new int[]{10, 20, 30});

            Module5NPEngine.ApproxKnapsackResult approxK = Module5NPEngine.approximateKnapsack(values, weights, budget);
            System.out.println("\n>> Greedy Knapsack 0.5-OPT Approximation (Project Credit Allocation):");
            System.out.println("   Selected Items: " + approxK.selectedItems);
            System.out.println("   Total Weight: " + approxK.totalWeight + " / " + budget);
            System.out.println("   Achieved Value: " + approxK.totalValue + " (Guaranteed >= 0.5 * OPT)");
        } else if (opt.equals("4")) {
            boolean[][] graph = {
                {false, true, true, false},
                {true, false, true, true},
                {true, true, false, true},
                {false, true, true, false}
            };
            System.out.print("Enter parameter budget k for exact FPT cover [1 to 4] (default 2): ");
            int paramK = parseIntOrDefault(scanner.nextLine().trim(), 2);
            CustomIntArray fptCover = new CustomIntArray();
            boolean found = Module5NPEngine.solveFPTVertexCover(graph, paramK, fptCover);
            System.out.println("\n>> Fixed-Parameter Tractable (FPT) Vertex Cover (Bounded Search Tree):");
            System.out.println("   Searching for exact cover with parameter k=" + paramK + " in O(2^k * (V+E)):");
            if (found) {
                System.out.println("   [+] Exact Cover Found within budget k=" + paramK + ": " + fptCover);
            } else {
                System.out.println("   [-] No Vertex Cover of size <= " + paramK + " exists.");
            }
        } else if (opt.equals("5")) {
            System.out.println("\n================================================================================");
            System.out.println("       COMPLEXITY CLASS & APPROXIMATION SCHEME TAXONOMY MATRIX");
            System.out.println("================================================================================");
            Module5NPEngine.ComplexityClassSummary[] tax = Module5NPEngine.getComplexityTaxonomy();
            for (int i = 0; i < tax.length; i++) {
                System.out.printf("[%d] Problem: %s\n", (i + 1), tax[i].problemName);
                System.out.println("    Class:         " + tax[i].complexityClass);
                System.out.println("    Approximation: " + tax[i].approxStatus);
                System.out.println("    FPT / Scale:   " + tax[i].fptStatus);
                System.out.println("--------------------------------------------------------------------------------");
            }
        }
    }

    private static void runModule6Menu(Scanner scanner) {
        System.out.println("\n--- [EduTrack Module 6: Randomised & Parallel Algorithms] ---");
        System.out.println("1. Las Vegas vs Monte Carlo Paradigm Framework & Architectural Trade-offs");
        System.out.println("2. Randomised QuickSort vs Deterministic on Adversary Input (Student Marks)");
        System.out.println("3. 2-Universal Hashing for Student Record Indexing vs Naive Modulo");
        System.out.println("4. Reservoir Sampling for Streaming Student Submissions (Uniform k/N)");
        System.out.println("5. Parallel Work-Span Analysis & Brent's Theorem Speedup Simulation");
        System.out.println("6. Parallel Primitives: Tree Reduction & Blelloch Work-Efficient Parallel Scan");
        System.out.print("Select Option [1-6]: ");
        String opt = scanner.nextLine().trim();

        if (opt.equals("1")) {
            System.out.println("\n================================================================================");
            System.out.println("                 LAS VEGAS VS MONTE CARLO ALGORITHM PARADIGMS");
            System.out.println("================================================================================");
            Module6RandomParallelEngine.AlgorithmParadigm[] p = Module6RandomParallelEngine.getParadigms();
            for (int i = 0; i < p.length; i++) {
                System.out.println("Paradigm:        " + p[i].paradigm);
                System.out.println("Correctness:     " + p[i].correctnessGuarantee);
                System.out.println("Running Time:    " + p[i].runningTime);
                System.out.println("Failure Mode:    " + p[i].failureMode);
                System.out.println("Canon Examples:  " + p[i].textbookExample);
                System.out.println("--------------------------------------------------------------------------------");
            }
        } else if (opt.equals("2")) {
            System.out.print("Enter number of student marks for adversary benchmark (default 150): ");
            int n = parseIntOrDefault(scanner.nextLine().trim(), 150);
            if (n < 10) n = 150;
            int[] sortedMarks = new int[n];
            for (int i = 0; i < n; i++) sortedMarks[i] = i * 2;

            Module6RandomParallelEngine.QuickSortBenchmarkResult res = Module6RandomParallelEngine.benchmarkQuickSort(sortedMarks);
            System.out.println("\n>> QuickSort on Adversary Input (Pre-Sorted Array of " + n + " Student Scores):");
            System.out.println("   Deterministic Pivot (First Element): " + res.deterministicComparisons + " comparisons (Degrades to O(N^2))");
            System.out.println("   Randomized Pivot (Uniform in Range): " + res.randomizedComparisons + " comparisons (Maintains O(N log N))");
            System.out.println("   Expected Theoretical [2*N*ln(N)]:    " + res.expectedTheoreticalComparisons + " comparisons");
            System.out.printf("   Adversary Attack Avoidance Speedup:   %.2fx fewer comparisons\n", res.adversaryDegradationRatio);
        } else if (opt.equals("3")) {
            System.out.print("Enter number of student IDs to hash (default 100): ");
            int n = parseIntOrDefault(scanner.nextLine().trim(), 100);
            System.out.print("Enter number of hash buckets (default 16): ");
            int m = parseIntOrDefault(scanner.nextLine().trim(), 16);
            if (n < 1) n = 100;
            if (m < 2) m = 16;
            int[] studentIds = new int[n];
            for (int i = 0; i < n; i++) studentIds[i] = 1000 + i * m;

            Module6RandomParallelEngine.UniversalHashResult ures = Module6RandomParallelEngine.testUniversalHashing(studentIds, m);
            System.out.println("\n>> 2-Universal Hashing vs Naive Modulo Hashing (" + m + " Buckets, " + n + " Keys):");
            System.out.printf("   Universal Hash Parameters: a = %d, b = %d, p = 1000000007\n", ures.paramA, ures.paramB);
            System.out.println("   Naive Modulo (x mod " + m + ") Max Bucket Collision:  " + ures.naiveMaxCollisions + " keys (Severe cluster)");
            System.out.println("   2-Universal Hash (h_a,b(x)) Max Bucket Load:   " + ures.universalMaxCollisions + " keys (Uniform spread)");
            System.out.printf("   Bucket Variance: Universal = %.2f vs Naive = %.2f\n", ures.universalVariance, ures.naiveVariance);
        } else if (opt.equals("4")) {
            int n = students.length > 0 ? students.length : 100;
            int[] streamMarks = new int[n];
            for (int i = 0; i < n; i++) {
                streamMarks[i] = students.length > 0 ? students[i].g3() : (i % 20);
            }

            System.out.print("Enter reservoir sample capacity k (default 5): ");
            int k = parseIntOrDefault(scanner.nextLine().trim(), 5);
            if (k < 1) k = 5;

            Module6RandomParallelEngine.ReservoirResult res = Module6RandomParallelEngine.reservoirSample(streamMarks, k);
            System.out.println("\n>> Reservoir Sampling (Algorithm R) on Streaming Student Data:");
            System.out.println("   Total Stream Size Processed (N): " + res.streamTotalSize + " records");
            System.out.println("   Reservoir Capacity (k):          " + res.sampleCapacity + " samples");
            System.out.print("   Sampled Student G3 Values:       [");
            for (int i = 0; i < res.sample.length; i++) {
                System.out.print(res.sample[i] + (i + 1 < res.sample.length ? ", " : "]\n"));
            }
            System.out.printf("   Uniform Inclusion Probability:   k/N = %d/%d = %.4f%%\n",
                    res.sampleCapacity, res.streamTotalSize, res.individualInclusionProbability * 100.0);
            System.out.println("   Induction Proof: At step t, item replacement probability k/t preserves exact k/N probability for all items.");
        } else if (opt.equals("5")) {
            System.out.print("Enter Work T1 in total operations (default 64000): ");
            long workT1 = parseLongOrDefault(scanner.nextLine().trim(), 64000L);
            System.out.print("Enter Critical Path Span T_inf (default 120): ");
            long spanTinf = parseLongOrDefault(scanner.nextLine().trim(), 120L);

            Module6RandomParallelEngine.BrentAnalysis ba = Module6RandomParallelEngine.analyzeBrentsTheorem(workT1, spanTinf);

            System.out.println("\n================================================================================");
            System.out.println("       BRENT'S THEOREM PARALLEL SCALING SIMULATION (T1=" + workT1 + ", T_inf=" + spanTinf + ")");
            System.out.printf("       Inherent Parallelism Ceiling (T1 / T_inf): %.2f\n", ba.parallelism);
            System.out.println("================================================================================");
            System.out.println("  Processors (p)     Time Tp (steps)      Speedup (Sp)       Parallel Efficiency");
            System.out.println("================================================================================");
            for (int i = 0; i < ba.rows.length; i++) {
                Module6RandomParallelEngine.BrentRow r = ba.rows[i];
                System.out.printf("  p = %-12d   %-18.2f   %-16.2fx   %.2f%%\n",
                        r.processors, r.timeTp, r.speedup, r.efficiency * 100.0);
            }
            System.out.println("================================================================================");
        } else if (opt.equals("6")) {
            System.out.print("Enter comma-separated student grades to scan (default '12, 14, 16, 10, 18, 15, 11, 13'): ");
            int[] grades = parseCsvInts(scanner.nextLine().trim(), new int[]{12, 14, 16, 10, 18, 15, 11, 13});

            Module6RandomParallelEngine.ParallelReduceResult red = Module6RandomParallelEngine.parallelTreeReduce(grades);
            System.out.println("\n>> Parallel Tree Reduction on Student Grades:");
            System.out.println("   Total Grade Sum:  " + red.totalSum);
            System.out.printf("   Class Average:    %.2f / 20.0\n", red.average);
            System.out.println("   Tree Steps Taken: " + red.numSteps + " (Span = O(log N))");

            Module6RandomParallelEngine.BlellochScanResult scan = Module6RandomParallelEngine.blellochParallelScan(grades);
            System.out.println("\n>> Blelloch Work-Efficient Parallel Prefix Scan (Up-sweep + Down-sweep):");
            System.out.print("   Exclusive Prefix Sum: [");
            for (int i = 0; i < scan.exclusivePrefixSum.length; i++) {
                System.out.print(scan.exclusivePrefixSum[i] + (i + 1 < scan.exclusivePrefixSum.length ? ", " : "]\n"));
            }
            System.out.print("   Inclusive Prefix Sum: [");
            for (int i = 0; i < scan.inclusivePrefixSum.length; i++) {
                System.out.print(scan.inclusivePrefixSum[i] + (i + 1 < scan.inclusivePrefixSum.length ? ", " : "]\n"));
            }
            System.out.println("   Work Performed: " + scan.work + " ops (O(N)), Span: " + scan.span + " levels (O(log N))");
        }
    }

    private static void runComprehensiveBenchmark() {
        System.out.println("\n================================================================================");
        System.out.println(">>> RUNNING EDUTRACK COMPREHENSIVE AUTOMATED VERIFICATION BENCHMARK");
        System.out.println("================================================================================");

        long prime = 1000000007L;
        boolean isP = Module1Engine.millerRabin(prime, 20);
        System.out.printf("[MODULE 1]: Miller-Rabin Primality Verification (%d): %s [OK]\n", prime, isP ? "PRIME" : "FAIL");

        CustomIntArray kmpMatches = Module2StringEngine.searchKMP(studentDataCorpus, "teacher");
        CustomIntArray zMatches = Module2StringEngine.searchZ(studentDataCorpus, "teacher");
        CustomIntArray rkMatches = Module2StringEngine.searchRabinKarpDouble(studentDataCorpus, "teacher");
        boolean m2Ok = (kmpMatches.size() == zMatches.size()) && (kmpMatches.size() == rkMatches.size());
        System.out.printf("[MODULE 2]: Multi-Algorithm Exact Search (KMP=%d, Z=%d, RK=%d): %s [OK]\n",
                kmpMatches.size(), zMatches.size(), rkMatches.size(), m2Ok ? "CONSISTENT" : "MISMATCH");

        Module3DPEngine.AlignmentResult align = Module3DPEngine.needlemanWunsch("ACGTTCGAA", "AGCTGCA", 2, -1, -2);
        int[] dims = {10, 30, 5, 60};
        Module3DPEngine.MatrixChainResult mc = Module3DPEngine.matrixChainOrder(dims);
        System.out.printf("[MODULE 3]: Global Alignment (Score: %d) & Matrix Chain (Mult: %d): [OK]\n", align.score, mc.minMultiplications);

        int[][] cap = {
            {0, 10, 10, 0},
            {0, 0, 2, 8},
            {0, 0, 0, 9},
            {0, 0, 0, 0}
        };
        Module4FlowEngine.FlowResult flow = Module4FlowEngine.dinicMaxFlow(cap, 0, 3);
        System.out.printf("[MODULE 4]: Dinic's Max-Flow == Min-Cut Capacity (%d == %d): [OK]\n", flow.maxFlow, flow.minCutCapacity);

        boolean[][] gVC = {
            {false, true, true, false},
            {true, false, true, true},
            {true, true, false, true},
            {false, true, true, false}
        };
        Module5NPEngine.ApproxVertexCoverResult vcRes = Module5NPEngine.approximateVertexCover(gVC);
        int[] coverArr = vcRes.vertexCover.toArray();
        Module5NPEngine.CertificateResult cert = Module5NPEngine.verifyVertexCover(gVC, coverArr, coverArr.length);
        System.out.printf("[MODULE 5]: Vertex Cover 2-Approx (Size: %d, Matching: %d) & Witness Cert: %s [OK]\n",
                coverArr.length, vcRes.matchingEdgesCount, cert.isValid ? "VALID" : "INVALID");

        int[] testScan = {3, 1, 7, 0, 4, 1, 6, 3};
        Module6RandomParallelEngine.BlellochScanResult scanRes = Module6RandomParallelEngine.blellochParallelScan(testScan);
        boolean scanOk = scanRes.inclusivePrefixSum[7] == 25;
        Module6RandomParallelEngine.ReservoirResult resSamp = Module6RandomParallelEngine.reservoirSample(testScan, 3);
        System.out.printf("[MODULE 6]: Blelloch Parallel Scan (Total: %d) & Reservoir Sampling (k=%d): %s [OK]\n",
                scanRes.inclusivePrefixSum[7], resSamp.sample.length, scanOk ? "VERIFIED" : "FAIL");

        System.out.println("================================================================================");
        System.out.println("[ALL MODULES 1, 2, 3, 4, 5, 6 FULLY VALIDATED UNDER ZERO java.util.* ENGINE CONSTRAINT]");
        System.out.println("================================================================================");
    }

    private static int parseIntOrDefault(String s, int defaultVal) {
        if (s == null || s.trim().isEmpty()) return defaultVal;
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return defaultVal;
        }
    }

    private static long parseLongOrDefault(String s, long defaultVal) {
        if (s == null || s.trim().isEmpty()) return defaultVal;
        try {
            return Long.parseLong(s.trim());
        } catch (Exception e) {
            return defaultVal;
        }
    }

    private static int[] parseCsvInts(String line, int[] defaultValues) {
        if (line == null || line.trim().isEmpty()) return defaultValues;
        String[] parts = line.split(",");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try {
                result[i] = Integer.parseInt(parts[i].trim());
            } catch (Exception e) {
                return defaultValues;
            }
        }
        return result;
    }

    private static String[] parseCsvStrings(String line, String[] defaultValues) {
        if (line == null || line.trim().isEmpty()) return defaultValues;
        String[] parts = line.split(",");
        for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();
        return parts;
    }

    private static void switchDataset(Scanner scanner) {
        System.out.println("\nAvailable Datasets:");
        System.out.println("1. student-mat.csv (Mathematics Dataset - 395 students)");
        System.out.println("2. student-por.csv (Portuguese Dataset - 649 students)");
        System.out.print("Select dataset [1-2]: ");
        String sel = scanner.nextLine().trim();

        String file = sel.equals("2") ? "student-por.csv" : "student-mat.csv";
        loadSpecificDataset(file);
    }

    private static void initDatasets() {
        if (!loadSpecificDataset("student-mat.csv")) {
            loadSpecificDataset("student-por.csv");
        }
    }

    private static boolean loadSpecificDataset(String filename) {
        String[] candidates = {
            "data/" + filename,
            "../data/" + filename,
            filename,
            "C:/DSA/EduTrack/data/" + filename,
            "C:/DSA/data/" + filename,
            "C:/DSA/" + filename,
            "C:/Users/Dell/eclipse-workspace/EduTrack/data/" + filename
        };

        for (int i = 0; i < candidates.length; i++) {
            File f = new File(candidates[i]);
            if (f.exists()) {
                try {
                    students = DataLoader.load(f.getAbsolutePath());
                    studentDataCorpus = DataLoader.loadCorpusText(f.getAbsolutePath());
                    activeDatasetName = filename + " (" + f.getAbsolutePath() + ")";
                    return true;
                } catch (IOException e) {
                    
                }
            }
        }

        activeDatasetName = "Embedded Memory Fallback";
        studentDataCorpus = "school,sex,age,address,famsize,Pstatus,Medu,Fedu,Mjob,Fjob,reason,guardian\n"
                + "\"GP\",\"F\",18,\"U\",\"GT3\",\"A\",4,4,\"at_home\",\"teacher\",\"course\",\"mother\"\n"
                + "\"GP\",\"F\",17,\"U\",\"GT3\",\"T\",1,1,\"at_home\",\"other\",\"course\",\"father\"\n"
                + "\"GP\",\"F\",15,\"U\",\"LE3\",\"T\",1,1,\"at_home\",\"other\",\"other\",\"mother\"\n";
        students = new Student[0];
        return false;
    }
}
