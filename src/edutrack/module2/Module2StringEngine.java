package edutrack.module2;

import edutrack.core.CustomIntArray;
import edutrack.core.CustomIntQueue;
import edutrack.core.CustomMatchList;
import edutrack.core.TextMatch;

public class Module2StringEngine {

    public static long lastComparisons = 0;
    public static int lastSpuriousCollisions = 0;

public static CustomMatchList mapOffsetsToCoordinates(String text, CustomIntArray offsets, String pattern) {
        CustomMatchList matches = new CustomMatchList(offsets.size());
        if (offsets.isEmpty()) return matches;

        CustomIntArray lineStarts = new CustomIntArray(text.length() / 40 + 16);
        lineStarts.add(0);
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                lineStarts.add(i + 1);
            }
        }

        for (int m = 0; m < offsets.size(); m++) {
            int off = offsets.get(m);
            int low = 0, high = lineStarts.size() - 1, lineIdx = 0;
            while (low <= high) {
                int mid = (low + high) >>> 1;
                if (lineStarts.get(mid) <= off) {
                    lineIdx = mid;
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
            int lineNum = lineIdx + 1;
            int colNum = (off - lineStarts.get(lineIdx)) + 1;
            matches.add(new TextMatch(lineNum, colNum, off, pattern));
        }
        return matches;
    }

public static CustomIntArray searchNaive(String text, String pattern) {
        lastComparisons = 0;
        CustomIntArray occurrences = new CustomIntArray();
        int n = text.length(), m = pattern.length();
        if (m == 0 || n < m) return occurrences;

        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            while (j < m) {
                lastComparisons++;
                if (text.charAt(i + j) != pattern.charAt(j)) break;
                j++;
            }
            if (j == m) {
                occurrences.add(i);
            }
        }
        return occurrences;
    }

public static int[] computeKmpPi(String pat) {
        int m = pat.length();
        int[] pi = new int[m];
        int k = 0;
        for (int i = 1; i < m; i++) {
            while (k > 0 && pat.charAt(k) != pat.charAt(i)) {
                k = pi[k - 1];
            }
            if (pat.charAt(k) == pat.charAt(i)) {
                k++;
            }
            pi[i] = k;
        }
        return pi;
    }

    public static CustomIntArray searchKMP(String text, String pattern) {
        lastComparisons = 0;
        CustomIntArray occurrences = new CustomIntArray();
        int n = text.length(), m = pattern.length();
        if (m == 0 || n < m) return occurrences;

        int[] pi = computeKmpPi(pattern);
        int q = 0;
        for (int i = 0; i < n; i++) {
            while (q > 0 && pattern.charAt(q) != text.charAt(i)) {
                lastComparisons++;
                q = pi[q - 1];
            }
            lastComparisons++;
            if (pattern.charAt(q) == text.charAt(i)) {
                q++;
            }
            if (q == m) {
                occurrences.add(i - m + 1);
                q = pi[q - 1]; 
            }
        }
        return occurrences;
    }

public static int[] computeZArray(String s) {
        int n = s.length();
        int[] z = new int[n];
        if (n == 0) return z;
        z[0] = n;
        int l = 0, r = 0; 

        for (int i = 1; i < n; i++) {
            if (i < r) {
                int k = i - l;
                z[i] = (r - i < z[k]) ? (r - i) : z[k];
            }
            while (i + z[i] < n) {
                lastComparisons++;
                if (s.charAt(z[i]) == s.charAt(i + z[i])) {
                    z[i]++;
                } else {
                    break;
                }
            }
            if (i + z[i] > r) {
                l = i;
                r = i + z[i];
            }
        }
        return z;
    }

    public static CustomIntArray searchZ(String text, String pattern) {
        lastComparisons = 0;
        CustomIntArray occurrences = new CustomIntArray();
        int m = pattern.length();
        if (m == 0 || text.length() < m) return occurrences;

String concat = pattern + "\u0001" + text;
        int[] z = computeZArray(concat);

        for (int i = m + 1; i < z.length; i++) {
            if (z[i] == m) {
                occurrences.add(i - m - 1);
            }
        }
        return occurrences;
    }

public static CustomIntArray searchRabinKarp(String text, String pattern, long[] moduli, long[] bases) {
        lastSpuriousCollisions = 0;
        CustomIntArray occurrences = new CustomIntArray();
        int n = text.length(), m = pattern.length(), k = moduli.length;
        if (m == 0 || n < m) return occurrences;

        long[] h = new long[k];
        long[] pHash = new long[k];
        long[] tHash = new long[k];

        for (int q = 0; q < k; q++) {
            h[q] = 1;
            for (int i = 0; i < m - 1; i++) {
                h[q] = (h[q] * bases[q]) % moduli[q];
            }
            for (int i = 0; i < m; i++) {
                pHash[q] = (bases[q] * pHash[q] + textChar(pattern, i)) % moduli[q];
                tHash[q] = (bases[q] * tHash[q] + textChar(text, i)) % moduli[q];
            }
        }

        for (int i = 0; i <= n - m; i++) {
            boolean hashMatch = true;
            for (int q = 0; q < k; q++) {
                if (pHash[q] != tHash[q]) {
                    hashMatch = false;
                    break;
                }
            }
            if (hashMatch) {
                boolean verified = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        verified = false;
                        break;
                    }
                }
                if (verified) {
                    occurrences.add(i);
                } else {
                    lastSpuriousCollisions++;
                }
            }
            if (i < n - m) {
                for (int q = 0; q < k; q++) {
                    long leading = (textChar(text, i) * h[q]) % moduli[q];
                    long next = textChar(text, i + m);
                    tHash[q] = (bases[q] * (tHash[q] - leading + moduli[q]) + next) % moduli[q];
                }
            }
        }
        return occurrences;
    }

    private static int textChar(String s, int idx) {
        return (int) s.charAt(idx);
    }

    public static CustomIntArray searchRabinKarpSingle(String text, String pattern) {
        return searchRabinKarp(text, pattern, new long[]{1000000007L}, new long[]{256});
    }

    public static CustomIntArray searchRabinKarpSmallMod(String text, String pattern) {
        
        return searchRabinKarp(text, pattern, new long[]{101L}, new long[]{256});
    }

    public static CustomIntArray searchRabinKarpDouble(String text, String pattern) {
        
        return searchRabinKarp(text, pattern, new long[]{1000000007L, 1000000009L}, new long[]{256, 257});
    }

public static class AhoCorasickNode {
        public int[] next = new int[128];
        public int fail = 0;
        public int dictLink = -1; 
        public CustomIntArray matchIndices = new CustomIntArray();

        public AhoCorasickNode() {
            for (int i = 0; i < 128; i++) next[i] = -1;
        }
    }

    public static class AhoCorasick {
        private AhoCorasickNode[] nodes;
        private int nodeCount;
        private String[] patterns;

        public AhoCorasick(String[] dictionary) {
            this.patterns = dictionary;
            int maxNodes = 2;
            for (int i = 0; i < dictionary.length; i++) maxNodes += dictionary[i].length() + 1;
            nodes = new AhoCorasickNode[maxNodes];
            nodeCount = 0;
            createNode(); 

for (int p = 0; p < dictionary.length; p++) {
                String pat = dictionary[p];
                int curr = 0;
                for (int i = 0; i < pat.length(); i++) {
                    int c = charIdx(pat.charAt(i));
                    if (nodes[curr].next[c] == -1) {
                        int nextNode = createNode();
                        nodes[curr].next[c] = nextNode;
                    }
                    curr = nodes[curr].next[c];
                }
                nodes[curr].matchIndices.add(p);
            }

CustomIntQueue q = new CustomIntQueue(nodeCount);
            for (int c = 0; c < 128; c++) {
                if (nodes[0].next[c] != -1) {
                    q.enqueue(nodes[0].next[c]);
                } else {
                    nodes[0].next[c] = 0;
                }
            }

            while (!q.isEmpty()) {
                int u = q.dequeue();
                int f = nodes[u].fail;

if (!nodes[f].matchIndices.isEmpty()) {
                    nodes[u].dictLink = f;
                } else {
                    nodes[u].dictLink = nodes[f].dictLink;
                }

                for (int c = 0; c < 128; c++) {
                    int v = nodes[u].next[c];
                    if (v != -1) {
                        nodes[v].fail = nodes[f].next[c];
                        q.enqueue(v);
                    } else {
                        nodes[u].next[c] = nodes[f].next[c];
                    }
                }
            }
        }

        private int charIdx(char c) {
            return (c < 128) ? c : 127;
        }

        private int createNode() {
            if (nodeCount == nodes.length) {
                AhoCorasickNode[] next = new AhoCorasickNode[nodes.length * 2];
                for (int i = 0; i < nodeCount; i++) next[i] = nodes[i];
                nodes = next;
            }
            nodes[nodeCount] = new AhoCorasickNode();
            return nodeCount++;
        }

        public CustomMatchList searchAll(String text) {
            CustomMatchList results = new CustomMatchList(64);
            int curr = 0;
            for (int i = 0; i < text.length(); i++) {
                int c = charIdx(text.charAt(i));
                curr = nodes[curr].next[c];

for (int m = 0; m < nodes[curr].matchIndices.size(); m++) {
                    int patIdx = nodes[curr].matchIndices.get(m);
                    String p = patterns[patIdx];
                    int startOffset = i - p.length() + 1;
                    results.add(new TextMatch(0, 0, startOffset, p));
                }

int d = nodes[curr].dictLink;
                while (d != -1) {
                    for (int m = 0; m < nodes[d].matchIndices.size(); m++) {
                        int patIdx = nodes[d].matchIndices.get(m);
                        String p = patterns[patIdx];
                        int startOffset = i - p.length() + 1;
                        results.add(new TextMatch(0, 0, startOffset, p));
                    }
                    d = nodes[d].dictLink;
                }
            }
            return results;
        }

        public int[] countOccurrences(String text) {
            int[] counts = new int[patterns.length];
            CustomMatchList list = searchAll(text);
            for (int i = 0; i < list.size(); i++) {
                String pat = list.get(i).pattern;
                for (int p = 0; p < patterns.length; p++) {
                    if (patterns[p].equals(pat)) {
                        counts[p]++;
                        break;
                    }
                }
            }
            return counts;
        }
    }

public static int[] buildSuffixArray(String s) {
        int n = s.length();
        int[] sa = new int[n];
        int[] rank = new int[n];
        int[] tmp = new int[n];
        int[] buf = new int[n];
        if (n == 0) return sa;

        for (int i = 0; i < n; i++) {
            sa[i] = i;
            rank[i] = s.charAt(i);
        }

        for (int k = 1; ; k <<= 1) {
            mergeSortSA(sa, buf, 0, n - 1, rank, k, n);
            tmp[sa[0]] = 0;
            for (int i = 1; i < n; i++) {
                tmp[sa[i]] = tmp[sa[i - 1]] + (isSALess(sa[i - 1], sa[i], rank, k, n) ? 1 : 0);
            }
            for (int i = 0; i < n; i++) rank[i] = tmp[i];
            if (rank[sa[n - 1]] == n - 1 || k >= n) break; 
        }
        return sa;
    }

    private static boolean isSALess(int a, int b, int[] rank, int k, int n) {
        if (rank[a] != rank[b]) return rank[a] < rank[b];
        int ra = (a + k < n) ? rank[a + k] : -1;
        int rb = (b + k < n) ? rank[b + k] : -1;
        return ra < rb;
    }

    private static void mergeSortSA(int[] a, int[] buf, int lo, int hi, int[] rank, int k, int n) {
        if (lo >= hi) return;
        int mid = (lo + hi) >>> 1;
        mergeSortSA(a, buf, lo, mid, rank, k, n);
        mergeSortSA(a, buf, mid + 1, hi, rank, k, n);

        int i = lo, j = mid + 1, p = lo;
        while (i <= mid && j <= hi) {
            if (!isSALess(a[j], a[i], rank, k, n)) buf[p++] = a[i++];
            else buf[p++] = a[j++];
        }
        while (i <= mid) buf[p++] = a[i++];
        while (j <= hi) buf[p++] = a[j++];
        for (p = lo; p <= hi; p++) a[p] = buf[p];
    }

public static int[] buildKasaiLCP(String s, int[] sa) {
        int n = s.length();
        int[] lcp = new int[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) rank[sa[i]] = i;

        int h = 0;
        for (int i = 0; i < n; i++) {
            if (rank[i] > 0) {
                int j = sa[rank[i] - 1];
                while (i + h < n && j + h < n && s.charAt(i + h) == s.charAt(j + h)) {
                    h++;
                }
                lcp[rank[i]] = h;
                if (h > 0) h--; 
            } else {
                h = 0;
            }
        }
        return lcp;
    }

public static String longestRepeatedSubstring(String s) {
        if (s.length() < 2) return "";
        int[] sa = buildSuffixArray(s);
        int[] lcp = buildKasaiLCP(s, sa);

        int maxLen = 0;
        int startPos = 0;
        for (int i = 1; i < s.length(); i++) {
            if (lcp[i] > maxLen) {
                maxLen = lcp[i];
                startPos = sa[i];
            }
        }
        return s.substring(startPos, startPos + maxLen);
    }

    public static String longestCommonSubstring(String a, String b) {
        String concat = a + "\u0001" + b;
        int[] sa = buildSuffixArray(concat);
        int[] lcp = buildKasaiLCP(concat, sa);

        int lenA = a.length();
        int maxLen = 0;
        int startPos = 0;

        for (int i = 1; i < concat.length(); i++) {
            boolean fromA1 = sa[i - 1] < lenA;
            boolean fromA2 = sa[i] < lenA;
            if (fromA1 != fromA2 && lcp[i] > maxLen) {
                maxLen = lcp[i];
                startPos = sa[i];
            }
        }
        return concat.substring(startPos, startPos + maxLen);
    }

    public static String getSuffixStructuresComparison() {
        return "================================================================================\n"
             + "THEORETICAL COMPARISON: Suffix Arrays vs. Suffix Trees vs. Suffix Automata\n"
             + "================================================================================\n"
             + "1. Suffix Tree (Ukkonen O(n)):\n"
             + "   - Explicit trie of all suffixes with compacted edge labels.\n"
             + "   - High memory overhead (~20-40 bytes per character due to pointer nodes).\n"
             + "2. Suffix Array (Manber-Myers O(n log n) / SA-IS O(n)):\n"
             + "   - Pure integer array sa[i] storing sorted suffix start positions.\n"
             + "   - Extremely cache-friendly (only 4 bytes per character in int[]).\n"
             + "   - Combined with Kasai LCP array O(n), answers substring queries in O(m + log n).\n"
             + "3. Suffix Automaton (DAWG - Directed Acyclic Word Graph O(n)):\n"
             + "   - Minimal DFA recognizing all substrings of a string.\n"
             + "   - Exactly <= 2n-1 states and <= 3n-4 transitions.\n"
             + "   - Enables O(m) exact substring matching without binary search.\n"
             + "================================================================================";
    }
}
