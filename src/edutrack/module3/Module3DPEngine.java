package edutrack.module3;

import edutrack.core.CustomIntArray;

public class Module3DPEngine {

public static int[][] levenshteinMatrix(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int sub = dp[i - 1][j - 1] + 1;
                    int del = dp[i - 1][j] + 1;
                    int ins = dp[i][j - 1] + 1;
                    int min = (sub < del) ? sub : del;
                    if (ins < min) min = ins;
                    dp[i][j] = min;
                }
            }
        }
        return dp;
    }

    public static int levenshteinDistance(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[] prev = new int[m + 1];
        int[] cur = new int[m + 1];
        for (int j = 0; j <= m; j++) prev[j] = j;

        for (int i = 1; i <= n; i++) {
            cur[0] = i;
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    cur[j] = prev[j - 1];
                } else {
                    int sub = prev[j - 1] + 1;
                    int del = prev[j] + 1;
                    int ins = cur[j - 1] + 1;
                    int min = (sub < del) ? sub : del;
                    if (ins < min) min = ins;
                    cur[j] = min;
                }
            }
            int[] temp = prev; prev = cur; cur = temp;
        }
        return prev[m];
    }

    public static int damerauLevenshteinDistance(String s1, String s2) {
        return weightedEditDistance(s1, s2, 1, 1, 1, 1);
    }

    public static int weightedEditDistance(String s1, String s2, int insCost, int delCost, int subCost, int transCost) {
        int n = s1.length(), m = s2.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i * delCost;
        for (int j = 0; j <= m; j++) dp[0][j] = j * insCost;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : subCost;
                int del = dp[i - 1][j] + delCost;
                int ins = dp[i][j - 1] + insCost;
                int sub = dp[i - 1][j - 1] + cost;

                int min = (del < ins) ? del : ins;
                if (sub < min) min = sub;

                if (transCost >= 0 && i > 1 && j > 1
                        && s1.charAt(i - 1) == s2.charAt(j - 2)
                        && s1.charAt(i - 2) == s2.charAt(j - 1)) {
                    int trans = dp[i - 2][j - 2] + transCost;
                    if (trans < min) min = trans;
                }
                dp[i][j] = min;
            }
        }
        return dp[n][m];
    }

    public static double calculateSimilarity(String s1, String s2) {
        int maxLen = Math.max(s1.length(), s2.length());
        if (maxLen == 0) return 100.0;
        int dist = levenshteinDistance(s1, s2);
        return (1.0 - ((double) dist / maxLen)) * 100.0;
    }

public static class AlignmentResult {
        public int score;
        public String alignedS1;
        public String alignedS2;
        public boolean isLocal;
        public int startS1, endS1;
        public int startS2, endS2;
    }

    public static AlignmentResult needlemanWunsch(String s1, String s2, int match, int mismatch, int gap) {
        int n = s1.length(), m = s2.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i * gap;
        for (int j = 0; j <= m; j++) dp[0][j] = j * gap;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int matchScore = dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? match : mismatch);
                int delScore = dp[i - 1][j] + gap;
                int insScore = dp[i][j - 1] + gap;

                int max = (matchScore > delScore) ? matchScore : delScore;
                if (insScore > max) max = insScore;
                dp[i][j] = max;
            }
        }

char[] a1 = new char[n + m];
        char[] a2 = new char[n + m];
        int pos = 0;
        int i = n, j = m;

        while (i > 0 || j > 0) {
            int currentScore = dp[i][j];
            if (i > 0 && j > 0 && currentScore == dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? match : mismatch)) {
                a1[pos] = s1.charAt(i - 1);
                a2[pos] = s2.charAt(j - 1);
                i--; j--;
            } else if (i > 0 && currentScore == dp[i - 1][j] + gap) {
                a1[pos] = s1.charAt(i - 1);
                a2[pos] = '-';
                i--;
            } else {
                a1[pos] = '-';
                a2[pos] = s2.charAt(j - 1);
                j--;
            }
            pos++;
        }

        char[] r1 = new char[pos], r2 = new char[pos];
        for (int k = 0; k < pos; k++) {
            r1[k] = a1[pos - 1 - k];
            r2[k] = a2[pos - 1 - k];
        }

        AlignmentResult res = new AlignmentResult();
        res.score = dp[n][m];
        res.alignedS1 = new String(r1);
        res.alignedS2 = new String(r2);
        res.isLocal = false;
        return res;
    }

    public static AlignmentResult smithWaterman(String s1, String s2, int match, int mismatch, int gap) {
        int n = s1.length(), m = s2.length();
        int[][] dp = new int[n + 1][m + 1];
        int maxScore = 0;
        int maxI = 0, maxJ = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int matchScore = dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? match : mismatch);
                int delScore = dp[i - 1][j] + gap;
                int insScore = dp[i][j - 1] + gap;

                int max = (matchScore > delScore) ? matchScore : delScore;
                if (insScore > max) max = insScore;
                if (max < 0) max = 0; 

                dp[i][j] = max;
                if (max > maxScore) {
                    maxScore = max;
                    maxI = i;
                    maxJ = j;
                }
            }
        }

        char[] a1 = new char[n + m];
        char[] a2 = new char[n + m];
        int pos = 0;
        int i = maxI, j = maxJ;

        while (i > 0 && j > 0 && dp[i][j] > 0) {
            int currentScore = dp[i][j];
            if (currentScore == dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? match : mismatch)) {
                a1[pos] = s1.charAt(i - 1);
                a2[pos] = s2.charAt(j - 1);
                i--; j--;
            } else if (currentScore == dp[i - 1][j] + gap) {
                a1[pos] = s1.charAt(i - 1);
                a2[pos] = '-';
                i--;
            } else {
                a1[pos] = '-';
                a2[pos] = s2.charAt(j - 1);
                j--;
            }
            pos++;
        }

        char[] r1 = new char[pos], r2 = new char[pos];
        for (int k = 0; k < pos; k++) {
            r1[k] = a1[pos - 1 - k];
            r2[k] = a2[pos - 1 - k];
        }

        AlignmentResult res = new AlignmentResult();
        res.score = maxScore;
        res.alignedS1 = new String(r1);
        res.alignedS2 = new String(r2);
        res.isLocal = true;
        res.startS1 = i; res.endS1 = maxI;
        res.startS2 = j; res.endS2 = maxJ;
        return res;
    }

public static class MatrixChainResult {
        public long minMultiplications;
        public String parenthesization;
    }

    public static MatrixChainResult matrixChainOrder(int[] p) {
        int n = p.length - 1;
        long[][] m = new long[n + 1][n + 1];
        int[][] s = new int[n + 1][n + 1];

        for (int len = 2; len <= n; len++) {
            for (int i = 1; i <= n - len + 1; i++) {
                int j = i + len - 1;
                m[i][j] = Long.MAX_VALUE;
                for (int k = i; k < j; k++) {
                    long cost = m[i][k] + m[k + 1][j] + (long) p[i - 1] * p[k] * p[j];
                    if (cost < m[i][j]) {
                        m[i][j] = cost;
                        s[i][j] = k;
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        buildParens(s, 1, n, sb);

        MatrixChainResult res = new MatrixChainResult();
        res.minMultiplications = (n <= 1) ? 0 : m[1][n];
        res.parenthesization = sb.toString();
        return res;
    }

    private static void buildParens(int[][] s, int i, int j, StringBuilder sb) {
        if (i == j) {
            sb.append("A").append(i);
            return;
        }
        sb.append("(");
        buildParens(s, i, s[i][j], sb);
        sb.append(" x ");
        buildParens(s, s[i][j] + 1, j, sb);
        sb.append(")");
    }

    public static class OBSTResult {
        public long minCost;
        public String treeStructure;
    }

    public static OBSTResult optimalBST(int[] freq) {
        int n = freq.length;
        long[][] c = new long[n + 2][n + 2];
        int[][] root = new int[n + 2][n + 2];
        long[] pre = new long[n + 1];
        for (int i = 1; i <= n; i++) pre[i] = pre[i - 1] + freq[i - 1];

        for (int len = 1; len <= n; len++) {
            for (int i = 1; i <= n - len + 1; i++) {
                int j = i + len - 1;
                c[i][j] = Long.MAX_VALUE;
                for (int r = i; r <= j; r++) {
                    long v = c[i][r - 1] + c[r + 1][j];
                    if (v < c[i][j]) {
                        c[i][j] = v;
                        root[i][j] = r;
                    }
                }
                c[i][j] += pre[j] - pre[i - 1];
            }
        }

        StringBuilder sb = new StringBuilder();
        buildOBSTTree(root, 1, n, sb);

        OBSTResult res = new OBSTResult();
        res.minCost = (n == 0) ? 0 : c[1][n];
        res.treeStructure = sb.toString();
        return res;
    }

    private static void buildOBSTTree(int[][] root, int i, int j, StringBuilder sb) {
        if (i > j) {
            sb.append("-");
            return;
        }
        int r = root[i][j];
        sb.append("K").append(r);
        if (i == j) return;
        sb.append("[");
        buildOBSTTree(root, i, r - 1, sb);
        sb.append(",");
        buildOBSTTree(root, r + 1, j, sb);
        sb.append("]");
    }

public static class TSPResult {
        public int minCost;
        public CustomIntArray tour;
    }

    public static TSPResult solveTSP(int[][] dist) {
        int n = dist.length;
        int totalMasks = 1 << n;
        final int INF = 100000000;
        int[][] dp = new int[totalMasks][n];
        int[][] parent = new int[totalMasks][n];

        for (int mask = 0; mask < totalMasks; mask++) {
            for (int i = 0; i < n; i++) {
                dp[mask][i] = INF;
                parent[mask][i] = -1;
            }
        }

        dp[1][0] = 0; 

        for (int mask = 1; mask < totalMasks; mask++) {
            if ((mask & 1) == 0) continue;
            for (int u = 0; u < n; u++) {
                if ((mask & (1 << u)) == 0 || dp[mask][u] >= INF) continue;

                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) != 0) continue;
                    int nextMask = mask | (1 << v);
                    int newCost = dp[mask][u] + dist[u][v];
                    if (newCost < dp[nextMask][v]) {
                        dp[nextMask][v] = newCost;
                        parent[nextMask][v] = u;
                    }
                }
            }
        }

        int finalMask = totalMasks - 1;
        int minCost = INF;
        int lastNode = -1;

        for (int i = 1; i < n; i++) {
            int tourCost = dp[finalMask][i] + dist[i][0];
            if (tourCost < minCost) {
                minCost = tourCost;
                lastNode = i;
            }
        }

        CustomIntArray path = new CustomIntArray(n + 1);
        int curr = lastNode;
        int currMask = finalMask;
        while (curr != -1) {
            path.add(curr);
            int prev = parent[currMask][curr];
            currMask ^= (1 << curr);
            curr = prev;
        }

        CustomIntArray tour = new CustomIntArray(n + 1);
        for (int k = path.size() - 1; k >= 0; k--) tour.add(path.get(k));
        tour.add(0);

        TSPResult res = new TSPResult();
        res.minCost = minCost;
        res.tour = tour;
        return res;
    }

    public static CustomIntArray findHamiltonianPath(boolean[][] adj) {
        int n = adj.length;
        int totalMasks = 1 << n;
        boolean[][] dp = new boolean[totalMasks][n];
        int[][] parent = new int[totalMasks][n];

        for (int mask = 0; mask < totalMasks; mask++) {
            for (int i = 0; i < n; i++) parent[mask][i] = -1;
        }

        for (int i = 0; i < n; i++) dp[1 << i][i] = true;

        for (int mask = 1; mask < totalMasks; mask++) {
            for (int i = 0; i < n; i++) {
                if (!dp[mask][i]) continue;
                for (int j = 0; j < n; j++) {
                    if ((mask & (1 << j)) == 0 && adj[i][j]) {
                        int nextMask = mask | (1 << j);
                        dp[nextMask][j] = true;
                        parent[nextMask][j] = i;
                    }
                }
            }
        }

        int fullMask = totalMasks - 1;
        int endNode = -1;
        for (int i = 0; i < n; i++) {
            if (dp[fullMask][i]) {
                endNode = i;
                break;
            }
        }

        if (endNode == -1) return new CustomIntArray(0); 

        CustomIntArray reversed = new CustomIntArray(n);
        int cur = endNode, curMask = fullMask;
        while (cur != -1) {
            reversed.add(cur);
            int p = parent[curMask][cur];
            curMask ^= (1 << cur);
            cur = p;
        }

        CustomIntArray path = new CustomIntArray(n);
        for (int i = reversed.size() - 1; i >= 0; i--) path.add(reversed.get(i));
        return path;
    }

public static class TreeDPResult {
        public int treeDiameter;
        public int radius;
        public CustomIntArray centroids;
        public CustomIntArray centers;
        public int maxIndependentSet;
        public int[] rerootedSubtreeSums;
    }

    public static TreeDPResult computeTreeDP(int n, int[][] edges) {
        CustomIntArray[] adj = new CustomIntArray[n];
        for (int i = 0; i < n; i++) adj[i] = new CustomIntArray();
        for (int i = 0; i < edges.length; i++) {
            int u = edges[i][0], v = edges[i][1];
            adj[u].add(v);
            adj[v].add(u);
        }

        int[] sz = new int[n];
        int[] d1 = new int[n];
        int[] d2 = new int[n];
        int[] c1 = new int[n];
        int[] up = new int[n];
        int[] ecc = new int[n];
        int[] dp0 = new int[n]; 
        int[] dp1 = new int[n]; 
        int[] maxChild = new int[n];
        int[] diam = new int[1];

dfsTree1(0, -1, adj, sz, d1, d2, c1, dp0, dp1, maxChild, diam);

dfsTree2(0, -1, adj, d1, d2, c1, up);

        CustomIntArray centroids = new CustomIntArray();
        CustomIntArray centers = new CustomIntArray();
        int radius = Integer.MAX_VALUE;

        for (int v = 0; v < n; v++) {
            ecc[v] = Math.max(d1[v], up[v]);
            if (ecc[v] < radius) radius = ecc[v];
            if (2 * Math.max(maxChild[v], n - sz[v]) <= n) centroids.add(v);
        }
        for (int v = 0; v < n; v++) {
            if (ecc[v] == radius) centers.add(v);
        }

int[] subTreeSize = new int[n];
        long[] sumDist = new long[n];
        dfsSubtreeSum(0, -1, adj, subTreeSize, sumDist);
        int[] rerootedSums = new int[n];
        dfsReroot(0, -1, adj, subTreeSize, sumDist, n, rerootedSums);

        TreeDPResult res = new TreeDPResult();
        res.treeDiameter = diam[0];
        res.radius = radius;
        res.centroids = centroids;
        res.centers = centers;
        res.maxIndependentSet = Math.max(dp0[0], dp1[0]);
        res.rerootedSubtreeSums = rerootedSums;
        return res;
    }

    private static void dfsTree1(int v, int p, CustomIntArray[] adj, int[] sz, int[] d1, int[] d2, int[] c1,
                                 int[] dp0, int[] dp1, int[] maxChild, int[] diam) {
        sz[v] = 1; d1[v] = 0; d2[v] = 0; c1[v] = -1; dp1[v] = 1; dp0[v] = 0; maxChild[v] = 0;
        for (int i = 0; i < adj[v].size(); i++) {
            int u = adj[v].get(i);
            if (u == p) continue;
            dfsTree1(u, v, adj, sz, d1, d2, c1, dp0, dp1, maxChild, diam);
            sz[v] += sz[u];
            maxChild[v] = Math.max(maxChild[v], sz[u]);

            int h = d1[u] + 1;
            if (h > d1[v]) {
                d2[v] = d1[v];
                d1[v] = h;
                c1[v] = u;
            } else if (h > d2[v]) {
                d2[v] = h;
            }
            dp1[v] += dp0[u];
            dp0[v] += Math.max(dp0[u], dp1[u]);
        }
        diam[0] = Math.max(diam[0], d1[v] + d2[v]);
    }

    private static void dfsTree2(int v, int p, CustomIntArray[] adj, int[] d1, int[] d2, int[] c1, int[] up) {
        for (int i = 0; i < adj[v].size(); i++) {
            int u = adj[v].get(i);
            if (u == p) continue;
            int other = (c1[v] == u) ? d2[v] : d1[v];
            up[u] = 1 + Math.max(up[v], other);
            dfsTree2(u, v, adj, d1, d2, c1, up);
        }
    }

    private static void dfsSubtreeSum(int u, int p, CustomIntArray[] adj, int[] sz, long[] sumD) {
        sz[u] = 1;
        sumD[u] = 0;
        for (int i = 0; i < adj[u].size(); i++) {
            int v = adj[u].get(i);
            if (v != p) {
                dfsSubtreeSum(v, u, adj, sz, sumD);
                sz[u] += sz[v];
                sumD[u] += sumD[v] + sz[v];
            }
        }
    }

    private static void dfsReroot(int u, int p, CustomIntArray[] adj, int[] sz, long[] sumD, int totalN, int[] result) {
        result[u] = (int) sumD[u];
        for (int i = 0; i < adj[u].size(); i++) {
            int v = adj[u].get(i);
            if (v != p) {
                long origU = sumD[u], origV = sumD[v];
                int szU = sz[u], szV = sz[v];

                sumD[u] -= (sumD[v] + sz[v]);
                sz[u] -= sz[v];
                sumD[v] += (sumD[u] + (totalN - szV));
                sz[v] = totalN;

                dfsReroot(v, u, adj, sz, sumD, totalN, result);

                sumD[u] = origU; sumD[v] = origV;
                sz[u] = szU; sz[v] = szV;
            }
        }
    }

public static int[] sumOverSubsets(int[] f, int bits) {
        int total = 1 << bits;
        int[] dp = new int[total];
        for (int mask = 0; mask < total; mask++) dp[mask] = f[mask];

        for (int i = 0; i < bits; i++) {
            for (int mask = 0; mask < total; mask++) {
                if ((mask & (1 << i)) != 0) {
                    dp[mask] += dp[mask ^ (1 << i)];
                }
            }
        }
        return dp;
    }

    public static int[] sumOverSupersets(int[] f, int bits) {
        int total = 1 << bits;
        int[] dp = new int[total];
        for (int mask = 0; mask < total; mask++) dp[mask] = f[mask];

        for (int i = 0; i < bits; i++) {
            for (int mask = 0; mask < total; mask++) {
                if ((mask & (1 << i)) == 0) {
                    dp[mask] += dp[mask | (1 << i)];
                }
            }
        }
        return dp;
    }

public static String explainWhenDPIsWrong() {
        return "================================================================================\n"
             + "THEORETICAL ANALYSIS: When is Dynamic Programming the WRONG Tool?\n"
             + "================================================================================\n"
             + "1. Problems with Greedy-Choice Property (No Need for DP):\n"
             + "   - Example: Fractional Knapsack, Activity Selection, Minimum Spanning Tree (Kruskal/Prim).\n"
             + "   - Reason: A locally optimal choice leads directly to a globally optimal solution.\n"
             + "   - Cost difference: Greedy O(n log n) vs DP O(n * W) pseudo-polynomial.\n\n"
             + "2. Problems with No Overlapping Subproblems:\n"
             + "   - Example: Merge Sort, Fast Fourier Transform (FFT).\n"
             + "   - Reason: Subproblems are completely disjoint; memoization yields 0% cache reuse.\n"
             + "   - Proper tool: Divide-and-Conquer.\n\n"
             + "3. Problems with Intractable State Space (No Subproblem Compression):\n"
             + "   - Example: Longest Simple Path in general graphs (NP-hard).\n"
             + "   - Reason: Unlike DAGs where topological order gives subproblems, cyclic dependencies\n"
             + "     force tracking every subset of visited nodes. For n > 25, O(2^n * n^2) is infeasible.\n"
             + "   - Proper tool: Approximation, Branch-and-Bound, or SAT solvers.\n"
             + "================================================================================";
    }
}
