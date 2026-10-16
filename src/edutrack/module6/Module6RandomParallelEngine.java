package edutrack.module6;

import edutrack.core.CustomIntArray;

public class Module6RandomParallelEngine {

    private static long rngSeed = 88172645463325252L;

    public static void setSeed(long seed) {
        rngSeed = (seed ^ 0x5DEECE66DL) & ((1L << 48) - 1);
    }

    public static int nextRandomInt(int bound) {
        if (bound <= 0) return 0;
        rngSeed = (rngSeed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
        int bits = (int) (rngSeed >>> 17);
        return Math.abs(bits % bound);
    }

    public static class AlgorithmParadigm {
        public String paradigm;
        public String correctnessGuarantee;
        public String runningTime;
        public String failureMode;
        public String textbookExample;

        public AlgorithmParadigm(String p, String cg, String rt, String fm, String ex) {
            this.paradigm = p;
            this.correctnessGuarantee = cg;
            this.runningTime = rt;
            this.failureMode = fm;
            this.textbookExample = ex;
        }
    }

    public static AlgorithmParadigm[] getParadigms() {
        return new AlgorithmParadigm[] {
            new AlgorithmParadigm(
                "Las Vegas",
                "Always produces the exact correct output (100% deterministic correctness)",
                "Random variable (expected polynomial time, e.g. O(n log n))",
                "May take longer than expected, but never outputs incorrect answer",
                "Randomised QuickSort, Randomised QuickSelect, Treaps"
            ),
            new AlgorithmParadigm(
                "Monte Carlo",
                "Produces correct output with probability >= 1 - epsilon (probabilistic correctness)",
                "Strictly bounded deterministic running time (e.g. O(k * log^3 n))",
                "May return false positive or false negative with provably bounded probability",
                "Miller-Rabin Primality Test, Karger's Min-Cut, Bloom Filters"
            )
        };
    }

    public static class QuickSortBenchmarkResult {
        public long deterministicComparisons;
        public long randomizedComparisons;
        public long expectedTheoreticalComparisons;
        public double adversaryDegradationRatio;

        public QuickSortBenchmarkResult(long det, long rand, long exp, double ratio) {
            this.deterministicComparisons = det;
            this.randomizedComparisons = rand;
            this.expectedTheoreticalComparisons = exp;
            this.adversaryDegradationRatio = ratio;
        }
    }

    private static long compCount = 0;

    public static QuickSortBenchmarkResult benchmarkQuickSort(int[] input) {
        int n = input.length;
        int[] arr1 = new int[n];
        int[] arr2 = new int[n];
        for (int i = 0; i < n; i++) {
            arr1[i] = input[i];
            arr2[i] = input[i];
        }

        compCount = 0;
        quickSortDeterministic(arr1, 0, n - 1);
        long detComp = compCount;

        compCount = 0;
        setSeed(System.nanoTime());
        quickSortRandomized(arr2, 0, n - 1);
        long randComp = compCount;

        long expected = (long) (2.0 * n * Math.log(Math.max(2, n)));
        double ratio = randComp > 0 ? (double) detComp / randComp : 1.0;

        return new QuickSortBenchmarkResult(detComp, randComp, expected, ratio);
    }

    private static void quickSortDeterministic(int[] a, int lo, int hi) {
        if (lo >= hi) return;
        int p = partition(a, lo, hi, lo);
        quickSortDeterministic(a, lo, p - 1);
        quickSortDeterministic(a, p + 1, hi);
    }

    private static void quickSortRandomized(int[] a, int lo, int hi) {
        if (lo >= hi) return;
        int randPivot = lo + nextRandomInt(hi - lo + 1);
        int p = partition(a, lo, hi, randPivot);
        quickSortRandomized(a, lo, p - 1);
        quickSortRandomized(a, p + 1, hi);
    }

    private static int partition(int[] a, int lo, int hi, int pivotIdx) {
        int pivotVal = a[pivotIdx];
        swap(a, pivotIdx, hi);
        int i = lo;
        for (int j = lo; j < hi; j++) {
            compCount++;
            if (a[j] <= pivotVal) {
                swap(a, i, j);
                i++;
            }
        }
        swap(a, i, hi);
        return i;
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static class UniversalHashResult {
        public int numKeys;
        public int numBuckets;
        public int universalMaxCollisions;
        public int naiveMaxCollisions;
        public double universalVariance;
        public double naiveVariance;
        public long paramA;
        public long paramB;

        public UniversalHashResult(int n, int m, int uMax, int nMax, double uVar, double nVar, long a, long b) {
            this.numKeys = n;
            this.numBuckets = m;
            this.universalMaxCollisions = uMax;
            this.naiveMaxCollisions = nMax;
            this.universalVariance = uVar;
            this.naiveVariance = nVar;
            this.paramA = a;
            this.paramB = b;
        }
    }

    public static UniversalHashResult testUniversalHashing(int[] keys, int numBuckets) {
        int n = keys.length;
        int m = numBuckets;
        long p = 1000000007L;

        setSeed(System.nanoTime());
        long a = 1 + nextRandomInt((int) Math.min(1000000, p - 1));
        long b = nextRandomInt((int) Math.min(1000000, p));

        int[] uBuckets = new int[m];
        int[] nBuckets = new int[m];

        for (int i = 0; i < n; i++) {
            long k = keys[i];
            int uHash = (int) (((a * k + b) % p) % m);
            if (uHash < 0) uHash += m;
            uBuckets[uHash]++;

            int nHash = (int) (Math.abs(k) % m);
            nBuckets[nHash]++;
        }

        int uMax = 0, nMax = 0;
        double expectedPerBucket = (double) n / m;
        double uVarSum = 0, nVarSum = 0;

        for (int i = 0; i < m; i++) {
            if (uBuckets[i] > uMax) uMax = uBuckets[i];
            if (nBuckets[i] > nMax) nMax = nBuckets[i];
            uVarSum += (uBuckets[i] - expectedPerBucket) * (uBuckets[i] - expectedPerBucket);
            nVarSum += (nBuckets[i] - expectedPerBucket) * (nBuckets[i] - expectedPerBucket);
        }

        return new UniversalHashResult(n, m, uMax, nMax, uVarSum / m, nVarSum / m, a, b);
    }

    public static class ReservoirResult {
        public int[] sample;
        public int streamTotalSize;
        public int sampleCapacity;
        public double individualInclusionProbability;

        public ReservoirResult(int[] s, int n, int k) {
            this.sample = s;
            this.streamTotalSize = n;
            this.sampleCapacity = k;
            this.individualInclusionProbability = (double) k / n;
        }
    }

    public static ReservoirResult reservoirSample(int[] stream, int k) {
        int n = stream.length;
        if (k > n) k = n;
        int[] reservoir = new int[k];
        for (int i = 0; i < k; i++) {
            reservoir[i] = stream[i];
        }

        setSeed(System.nanoTime());
        for (int i = k; i < n; i++) {
            int j = nextRandomInt(i + 1);
            if (j < k) {
                reservoir[j] = stream[i];
            }
        }

        return new ReservoirResult(reservoir, n, k);
    }

    public static class BrentRow {
        public int processors;
        public double timeTp;
        public double speedup;
        public double efficiency;

        public BrentRow(int p, double tp, double sp, double eff) {
            this.processors = p;
            this.timeTp = tp;
            this.speedup = sp;
            this.efficiency = eff;
        }
    }

    public static class BrentAnalysis {
        public long workT1;
        public long spanTinf;
        public double parallelism;
        public BrentRow[] rows;

        public BrentAnalysis(long w, long s, BrentRow[] r) {
            this.workT1 = w;
            this.spanTinf = s;
            this.parallelism = (double) w / s;
            this.rows = r;
        }
    }

    public static BrentAnalysis analyzeBrentsTheorem(long workT1, long spanTinf) {
        int[] processors = {1, 2, 4, 8, 16, 32, 64};
        BrentRow[] rows = new BrentRow[processors.length];

        for (int i = 0; i < processors.length; i++) {
            int p = processors[i];
            double tp = ((double) (workT1 - spanTinf) / p) + spanTinf;
            double sp = (double) workT1 / tp;
            double eff = sp / p;
            rows[i] = new BrentRow(p, tp, sp, eff);
        }

        return new BrentAnalysis(workT1, spanTinf, rows);
    }

    public static class ParallelReduceResult {
        public int totalSum;
        public double average;
        public int numSteps;
        public CustomIntArray levelSums;

        public ParallelReduceResult(int sum, double avg, int steps, CustomIntArray levels) {
            this.totalSum = sum;
            this.average = avg;
            this.numSteps = steps;
            this.levelSums = levels;
        }
    }

    public static ParallelReduceResult parallelTreeReduce(int[] data) {
        int n = data.length;
        if (n == 0) return new ParallelReduceResult(0, 0.0, 0, new CustomIntArray());

        int[] cur = new int[n];
        for (int i = 0; i < n; i++) cur[i] = data[i];

        CustomIntArray levels = new CustomIntArray();
        int steps = 0;
        int active = n;

        while (active > 1) {
            steps++;
            int half = (active + 1) / 2;
            for (int i = 0; i < active / 2; i++) {
                cur[i] = cur[2 * i] + cur[2 * i + 1];
            }
            if (active % 2 != 0) {
                cur[half - 1] = cur[active - 1];
            }
            active = half;
            levels.add(cur[0]);
        }

        int totalSum = cur[0];
        double avg = (double) totalSum / n;
        return new ParallelReduceResult(totalSum, avg, steps, levels);
    }

    public static class BlellochScanResult {
        public int[] exclusivePrefixSum;
        public int[] inclusivePrefixSum;
        public int work;
        public int span;

        public BlellochScanResult(int[] excl, int[] incl, int w, int s) {
            this.exclusivePrefixSum = excl;
            this.inclusivePrefixSum = incl;
            this.work = w;
            this.span = s;
        }
    }

    public static BlellochScanResult blellochParallelScan(int[] input) {
        int n = input.length;
        int pow2 = 1;
        while (pow2 < n) pow2 <<= 1;

        int[] tree = new int[2 * pow2];
        for (int i = 0; i < n; i++) {
            tree[pow2 + i] = input[i];
        }

        int span = 0;
        int work = 0;

        for (int d = pow2 >> 1; d > 0; d >>= 1) {
            span++;
            for (int k = 0; k < d; k++) {
                int node = d + k;
                tree[node] = tree[2 * node] + tree[2 * node + 1];
                work++;
            }
        }

        tree[1] = 0;

        for (int d = 1; d < pow2; d <<= 1) {
            span++;
            for (int k = 0; k < d; k++) {
                int node = d + k;
                int leftVal = tree[2 * node];
                tree[2 * node] = tree[node];
                tree[2 * node + 1] = tree[node] + leftVal;
                work += 2;
            }
        }

        int[] excl = new int[n];
        int[] incl = new int[n];
        for (int i = 0; i < n; i++) {
            excl[i] = tree[pow2 + i];
            incl[i] = excl[i] + input[i];
        }

        return new BlellochScanResult(excl, incl, work, span);
    }
}
