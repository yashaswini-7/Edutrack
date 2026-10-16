package edutrack.core;

public class SortSearch {

    public static long comparisons = 0;

    public static void bubbleSortDesc(int[] idx, int[] key) {
        comparisons = 0;
        int n = idx.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                comparisons++;
                if (key[idx[j]] < key[idx[j + 1]]) {
                    int t = idx[j];
                    idx[j] = idx[j + 1];
                    idx[j + 1] = t;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    public static void mergeSortDesc(int[] idx, int[] key) {
        comparisons = 0;
        sort(idx, new int[idx.length], 0, idx.length - 1, key);
    }

    private static void sort(int[] a, int[] tmp, int lo, int hi, int[] key) {
        if (lo >= hi) return;
        int mid = (lo + hi) >>> 1;
        sort(a, tmp, lo, mid, key);
        sort(a, tmp, mid + 1, hi, key);
        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) {
            comparisons++;
            if (key[a[i]] >= key[a[j]]) tmp[k++] = a[i++];
            else tmp[k++] = a[j++];
        }
        while (i <= mid) tmp[k++] = a[i++];
        while (j <= hi) tmp[k++] = a[j++];
        for (k = lo; k <= hi; k++) a[k] = tmp[k];
    }

    public static int linearSearch(Student[] a, String id) {
        comparisons = 0;
        for (int i = 0; i < a.length; i++) {
            comparisons++;
            if (a[i].id.equals(id)) return i;
        }
        return -1;
    }

    public static int binarySearch(Student[] a, String id) {
        comparisons = 0;
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            comparisons++;
            int c = a[mid].id.compareTo(id);
            if (c == 0) return mid;
            if (c < 0) lo = mid + 1;
            else hi = mid - 1;
        }
        return -1;
    }
}
