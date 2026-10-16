package edutrack.module5;

import edutrack.core.CustomIntArray;

public class Module5NPEngine {

    public static class CertificateResult {
        public boolean isValid;
        public String message;
        public long verificationTimeNs;

        public CertificateResult(boolean isValid, String message, long verificationTimeNs) {
            this.isValid = isValid;
            this.message = message;
            this.verificationTimeNs = verificationTimeNs;
        }

        public String toString() {
            return String.format("[%s] %s (Verified in %.2f us)",
                    isValid ? "VALID CERTIFICATE" : "INVALID CERTIFICATE",
                    message, verificationTimeNs / 1000.0);
        }
    }

    public static CertificateResult verifyVertexCover(boolean[][] graph, int[] candidateCover, int k) {
        long t0 = System.nanoTime();
        int n = graph.length;
        if (candidateCover.length > k) {
            long t1 = System.nanoTime();
            return new CertificateResult(false, "Candidate cover size (" + candidateCover.length + ") exceeds budget k=" + k, t1 - t0);
        }

        boolean[] inCover = new boolean[n];
        for (int i = 0; i < candidateCover.length; i++) {
            int v = candidateCover[i];
            if (v >= 0 && v < n) {
                inCover[v] = true;
            }
        }

        for (int u = 0; u < n; u++) {
            for (int v = u + 1; v < n; v++) {
                if (graph[u][v]) {
                    if (!inCover[u] && !inCover[v]) {
                        long t1 = System.nanoTime();
                        return new CertificateResult(false, "Edge (" + u + ", " + v + ") is uncovered by candidate set", t1 - t0);
                    }
                }
            }
        }
        long t1 = System.nanoTime();
        return new CertificateResult(true, "All edges covered using " + candidateCover.length + " vertices <= k=" + k, t1 - t0);
    }

    public static CertificateResult verifySubsetSum(int[] set, int[] candidateIndices, int targetSum) {
        long t0 = System.nanoTime();
        int sum = 0;
        boolean[] used = new boolean[set.length];
        for (int i = 0; i < candidateIndices.length; i++) {
            int idx = candidateIndices[i];
            if (idx < 0 || idx >= set.length) {
                long t1 = System.nanoTime();
                return new CertificateResult(false, "Index " + idx + " is out of bounds", t1 - t0);
            }
            if (used[idx]) {
                long t1 = System.nanoTime();
                return new CertificateResult(false, "Index " + idx + " used multiple times in witness", t1 - t0);
            }
            used[idx] = true;
            sum += set[idx];
        }
        long t1 = System.nanoTime();
        if (sum == targetSum) {
            return new CertificateResult(true, "Witness sum matches exact target " + targetSum, t1 - t0);
        } else {
            return new CertificateResult(false, "Witness sum (" + sum + ") != target (" + targetSum + ")", t1 - t0);
        }
    }

    public static CertificateResult verify3SAT(int[][] clauses, boolean[] assignment) {
        long t0 = System.nanoTime();
        for (int i = 0; i < clauses.length; i++) {
            boolean clauseSatisfied = false;
            for (int j = 0; j < clauses[i].length; j++) {
                int lit = clauses[i][j];
                int var = Math.abs(lit) - 1;
                boolean val = assignment[var];
                if (lit < 0) val = !val;
                if (val) {
                    clauseSatisfied = true;
                    break;
                }
            }
            if (!clauseSatisfied) {
                long t1 = System.nanoTime();
                return new CertificateResult(false, "Clause " + (i + 1) + " evaluated to FALSE under assignment", t1 - t0);
            }
        }
        long t1 = System.nanoTime();
        return new CertificateResult(true, "All " + clauses.length + " 3-CNF clauses satisfied", t1 - t0);
    }

    public static class SatToCliqueReduction {
        public boolean[][] cliqueGraph;
        public int numVertices;
        public int targetCliqueSize;
        public String[] vertexLabels;

        public SatToCliqueReduction(boolean[][] graph, int n, int k, String[] labels) {
            this.cliqueGraph = graph;
            this.numVertices = n;
            this.targetCliqueSize = k;
            this.vertexLabels = labels;
        }
    }

    public static SatToCliqueReduction reduce3SatToClique(int[][] clauses) {
        int m = clauses.length;
        int totalVertices = m * 3;
        boolean[][] graph = new boolean[totalVertices][totalVertices];
        String[] labels = new String[totalVertices];

        for (int c = 0; c < m; c++) {
            for (int l = 0; l < 3; l++) {
                int vIdx = c * 3 + l;
                int lit = clauses[c][l];
                labels[vIdx] = (lit > 0 ? "x" + lit : "~x" + (-lit)) + "[C" + (c + 1) + "]";
            }
        }

        for (int c1 = 0; c1 < m; c1++) {
            for (int l1 = 0; l1 < 3; l1++) {
                int u = c1 * 3 + l1;
                int lit1 = clauses[c1][l1];

                for (int c2 = c1 + 1; c2 < m; c2++) {
                    for (int l2 = 0; l2 < 3; l2++) {
                        int v = c2 * 3 + l2;
                        int lit2 = clauses[c2][l2];

                        if (lit1 != -lit2) {
                            graph[u][v] = true;
                            graph[v][u] = true;
                        }
                    }
                }
            }
        }

        return new SatToCliqueReduction(graph, totalVertices, m, labels);
    }

    public static boolean[][] reduceCliqueToIndependentSet(boolean[][] cliqueGraph) {
        int n = cliqueGraph.length;
        boolean[][] complement = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    complement[i][j] = !cliqueGraph[i][j];
                }
            }
        }
        return complement;
    }

    public static class ApproxVertexCoverResult {
        public CustomIntArray vertexCover;
        public int matchingEdgesCount;
        public double theoreticalApproximationBound = 2.0;

        public ApproxVertexCoverResult(CustomIntArray cover, int matchCount) {
            this.vertexCover = cover;
            this.matchingEdgesCount = matchCount;
        }
    }

    public static ApproxVertexCoverResult approximateVertexCover(boolean[][] graph) {
        int n = graph.length;
        boolean[][] rem = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rem[i][j] = graph[i][j];
            }
        }

        CustomIntArray cover = new CustomIntArray();
        boolean[] inCover = new boolean[n];
        int matchingCount = 0;

        for (int u = 0; u < n; u++) {
            for (int v = u + 1; v < n; v++) {
                if (rem[u][v]) {
                    matchingCount++;
                    if (!inCover[u]) {
                        cover.add(u);
                        inCover[u] = true;
                    }
                    if (!inCover[v]) {
                        cover.add(v);
                        inCover[v] = true;
                    }

                    for (int w = 0; w < n; w++) {
                        rem[u][w] = false; rem[w][u] = false;
                        rem[v][w] = false; rem[w][v] = false;
                    }
                }
            }
        }

        return new ApproxVertexCoverResult(cover, matchingCount);
    }

    public static class ApproxKnapsackResult {
        public CustomIntArray selectedItems;
        public int totalWeight;
        public int totalValue;
        public double approxGuarantee = 0.5;

        public ApproxKnapsackResult(CustomIntArray items, int weight, int value) {
            this.selectedItems = items;
            this.totalWeight = weight;
            this.totalValue = value;
        }
    }

    public static ApproxKnapsackResult approximateKnapsack(int[] values, int[] weights, int capacity) {
        int n = values.length;
        int[] order = new int[n];
        for (int i = 0; i < n; i++) order[i] = i;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                double r1 = (double) values[order[j]] / weights[order[j]];
                double r2 = (double) values[order[j + 1]] / weights[order[j + 1]];
                if (r1 < r2) {
                    int tmp = order[j];
                    order[j] = order[j + 1];
                    order[j + 1] = tmp;
                }
            }
        }

        CustomIntArray greedyItems = new CustomIntArray();
        int greedyW = 0;
        int greedyV = 0;
        for (int i = 0; i < n; i++) {
            int idx = order[i];
            if (greedyW + weights[idx] <= capacity) {
                greedyItems.add(idx);
                greedyW += weights[idx];
                greedyV += values[idx];
            }
        }

        int maxSingleV = 0;
        int maxSingleIdx = -1;
        for (int i = 0; i < n; i++) {
            if (weights[i] <= capacity && values[i] > maxSingleV) {
                maxSingleV = values[i];
                maxSingleIdx = i;
            }
        }

        if (maxSingleV > greedyV && maxSingleIdx != -1) {
            CustomIntArray singleItem = new CustomIntArray();
            singleItem.add(maxSingleIdx);
            return new ApproxKnapsackResult(singleItem, weights[maxSingleIdx], maxSingleV);
        } else {
            return new ApproxKnapsackResult(greedyItems, greedyW, greedyV);
        }
    }

    public static boolean solveFPTVertexCover(boolean[][] graph, int k, CustomIntArray resultCover) {
        int n = graph.length;
        int edgeU = -1, edgeV = -1;
        for (int u = 0; u < n; u++) {
            for (int v = u + 1; v < n; v++) {
                if (graph[u][v]) {
                    edgeU = u;
                    edgeV = v;
                    break;
                }
            }
            if (edgeU != -1) break;
        }

        if (edgeU == -1) {
            return true;
        }

        if (k <= 0) {
            return false;
        }

        boolean[][] g1 = copyGraph(graph);
        for (int i = 0; i < n; i++) { g1[edgeU][i] = false; g1[i][edgeU] = false; }
        resultCover.add(edgeU);
        if (solveFPTVertexCover(g1, k - 1, resultCover)) {
            return true;
        }
        resultCover.removeLast();

        boolean[][] g2 = copyGraph(graph);
        for (int i = 0; i < n; i++) { g2[edgeV][i] = false; g2[i][edgeV] = false; }
        resultCover.add(edgeV);
        if (solveFPTVertexCover(g2, k - 1, resultCover)) {
            return true;
        }
        resultCover.removeLast();

        return false;
    }

    private static boolean[][] copyGraph(boolean[][] g) {
        int n = g.length;
        boolean[][] c = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = g[i][j];
            }
        }
        return c;
    }

    public static class ComplexityClassSummary {
        public String problemName;
        public String complexityClass;
        public String approxStatus;
        public String fptStatus;

        public ComplexityClassSummary(String name, String cc, String approx, String fpt) {
            this.problemName = name;
            this.complexityClass = cc;
            this.approxStatus = approx;
            this.fptStatus = fpt;
        }
    }

    public static ComplexityClassSummary[] getComplexityTaxonomy() {
        return new ComplexityClassSummary[] {
            new ComplexityClassSummary("Vertex Cover", "NP-Complete", "APX-Complete (2-Approximation via Maximal Matching)", "FPT: O(2^k * k^2 + n) via Search Tree"),
            new ComplexityClassSummary("0/1 Knapsack", "NP-Complete (Weak)", "FPTAS: (1 - eps) in O(n^3 / eps) via Scaling", "FPT / Pseudo-poly: O(n * W)"),
            new ComplexityClassSummary("Independent Set", "NP-Complete", "Cannot be approximated within n^(1-eps) unless P=NP", "W[1]-Hard (No FPT unless FPT=W[1])"),
            new ComplexityClassSummary("Metric TSP", "NP-Complete", "APX: 1.5-Approximation via Christofides-Serdyukov", "Exp in n: O(2^n * n^2)"),
            new ComplexityClassSummary("General TSP", "NP-Complete", "Non-APX: Cannot be approximated within any constant ratio", "No PTAS / No FPT"),
            new ComplexityClassSummary("Euclidean TSP", "NP-Complete", "PTAS: (1 + eps) approximation via Arora-Mitchell", "O(n * (log n)^O(1/eps))")
        };
    }
}
