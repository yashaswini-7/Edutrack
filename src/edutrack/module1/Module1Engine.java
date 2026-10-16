package edutrack.module1;

import edutrack.core.CustomStringArray;

public class Module1Engine {

public static class CanonMapping {
        public final String queryClass;
        public final String algorithmFamily;
        public final String referenceAlgorithm;
        public final String textbookCanon;
        public final String asymptoticCost;
        public final String whyDsa1Fails;

        public CanonMapping(String q, String fam, String ref, String canon, String cost, String fails) {
            this.queryClass = q;
            this.algorithmFamily = fam;
            this.referenceAlgorithm = ref;
            this.textbookCanon = canon;
            this.asymptoticCost = cost;
            this.whyDsa1Fails = fails;
        }
    }

    public static CanonMapping[] getCanonTable() {
        return new CanonMapping[] {
            new CanonMapping(
                "Pattern Search",
                "String Algorithms (Module 2)",
                "Knuth-Morris-Pratt (KMP), Z-Algorithm, Rabin-Karp",
                "CLRS Ch 32, Erickson Ch 8",
                "O(n + m) preprocessing & search",
                "Naive search is O(n*m); quadratic worst case on repetitive patterns."
            ),
            new CanonMapping(
                "Fuzzy Matching / Typo Tolerance",
                "Dynamic Programming (Module 3)",
                "Levenshtein, Damerau-Levenshtein, Wagner-Fischer",
                "Kleinberg-Tardos Ch 6, Erickson Ch 3",
                "O(n * m) DP Matrix, O(m) space",
                "Greedy string comparison fails on non-local insertions/deletions."
            ),
            new CanonMapping(
                "Profile & Document Similarity",
                "Suffix Structures (Module 2/3)",
                "Suffix Array + Kasai LCP, Smith-Waterman Local Alignment",
                "CLRS Part VII, Kleinberg-Tardos Ch 6",
                "O(n log^2 n) / O(n) LCP",
                "Pairwise brute-force comparison requires O(n^3) time."
            ),
            new CanonMapping(
                "Citation & Resource Flow Analysis",
                "Network Flow (Module 4)",
                "Edmonds-Karp O(VE^2), Dinic's Algorithm O(V^2 E)",
                "CLRS Ch 26, Kleinberg-Tardos Ch 7",
                "O(V^2 E) blocking flow",
                "Standard BFS/DFS/Dijkstra cannot handle conservation & residual capacities."
            ),
            new CanonMapping(
                "Project & Mentor Scheduling",
                "Network Flow & Bipartite Matching (Module 4)",
                "Max Flow Reduction, Konig's Theorem, MCMF",
                "Kleinberg-Tardos Ch 7, Erickson Ch 10-11",
                "O(V^2 E) / O(E * flow)",
                "NP-hard or multi-constraint matching unsolvable by greedy heuristics."
            ),
            new CanonMapping(
                "ID Cryptographic Primality Testing",
                "Randomised Algorithms (Module 1)",
                "Miller-Rabin Probabilistic Primality",
                "CLRS Ch 31, Erickson Ch 12",
                "O(k * log^3 n) rounds",
                "Deterministic trial division takes O(sqrt(n)), intractable for 64-bit primes."
            )
        };
    }

public static long mulMod(long a, long b, long mod) {
        long res = 0;
        a %= mod;
        b %= mod;
        while (b > 0) {
            if ((b & 1) == 1) {
                res = (res + a) % mod;
            }
            a = (a * 2) % mod;
            b >>= 1;
        }
        return res;
    }

public static long powerMod(long base, long exp, long mod) {
        long res = 1 % mod;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                res = mulMod(res, base, mod);
            }
            base = mulMod(base, base, mod);
            exp >>= 1;
        }
        return res;
    }

public static boolean millerRabin(long n, int k) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if ((n & 1) == 0) return false;

long d = n - 1;
        int s = 0;
        while ((d & 1) == 0) {
            d >>= 1;
            s++;
        }

long[] bases = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37};
        int rounds = (k < bases.length) ? k : bases.length;

        for (int i = 0; i < rounds; i++) {
            long a = bases[i];
            if (n <= a) break;

            long x = powerMod(a, d, n);
            if (x == 1 || x == n - 1) continue;

            boolean composite = true;
            for (int r = 1; r < s; r++) {
                x = mulMod(x, x, n);
                if (x == n - 1) {
                    composite = false;
                    break;
                }
            }
            if (composite) return false; 
        }
        return true;
    }

public static String explainMillerRabin(long n) {
        if (n < 2) return n + " is less than 2 (Not Prime).";
        if (n == 2 || n == 3) return n + " is Prime (Base prime).";
        if ((n & 1) == 0) return n + " is Even and > 2 (Composite).";

        long d = n - 1;
        int s = 0;
        while ((d & 1) == 0) {
            d >>= 1;
            s++;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Decomposition: %d - 1 = 2^%d * %d\n", n, s, d));

        long[] bases = {2, 3, 5, 7, 11};
        for (int i = 0; i < bases.length; i++) {
            long a = bases[i];
            if (n <= a) break;
            long x = powerMod(a, d, n);
            sb.append(String.format("  Base a=%d: a^d mod n = %d", a, x));
            if (x == 1 || x == n - 1) {
                sb.append(" [Passed Fermat/Root check]\n");
                continue;
            }
            boolean passed = false;
            for (int r = 1; r < s; r++) {
                x = mulMod(x, x, n);
                if (x == n - 1) {
                    sb.append(String.format(" -> Squaring step r=%d gives n-1 (%d) [Passed]\n", r, x));
                    passed = true;
                    break;
                }
            }
            if (!passed) {
                sb.append(" -> Failed all square-root checks. Witness found: COMPOSITE.\n");
                return sb.toString();
            }
        }
        sb.append(String.format("Result: %d is a CERTIFIED PRIME (>99.9999%% certainty).\n", n));
        return sb.toString();
    }

public static String routeQueryDescription(String queryType) {
        if (queryType.equalsIgnoreCase("pattern")) {
            return "Mapped to String Algorithms: KMP / Z-Function / Rabin-Karp polynomial rolling hash.";
        } else if (queryType.equalsIgnoreCase("fuzzy")) {
            return "Mapped to Dynamic Programming: Levenshtein / Damerau-Levenshtein edit distance.";
        } else if (queryType.equalsIgnoreCase("similarity")) {
            return "Mapped to Suffix Structures: Suffix Array + Kasai LCP longest common substring.";
        } else if (queryType.equalsIgnoreCase("flow")) {
            return "Mapped to Network Flow: Edmonds-Karp / Dinic's blocking flow on level graph.";
        } else if (queryType.equalsIgnoreCase("scheduling")) {
            return "Mapped to Capacity-Constrained Flow Reduction / Bipartite Matching (Konig's Theorem).";
        } else if (queryType.equalsIgnoreCase("primality")) {
            return "Mapped to Randomised Algorithms: Miller-Rabin non-trivial square roots of unity.";
        }
        return "Unknown query class. Supported: pattern, fuzzy, similarity, flow, scheduling, primality.";
    }
}
