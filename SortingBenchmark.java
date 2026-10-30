import java.util.Arrays;
import java.util.Random;

/**
 * نشاط عملي: التحليل والمقاطعة التطبيقية لأداء خوارزميات الترتيب
 * Data Structures & Algorithms - Sorting Benchmark
 *
 * يقيس زمن تنفيذ 5 خوارزميات ترتيب (بالميكروثانية) على 4 حالات من البيانات.
 */
public class SortingBenchmark {

    // حجم البيانات الثابت المطلوب
    static final int N = 100_000;

    // بذرة ثابتة لتكون النتائج قابلة للتكرار
    static final long SEED = 12345L;

    // =====================================================================
    //  1) Selection Sort  -  O(N^2) في جميع الحالات
    // =====================================================================
    static void selectionSort(int[] a) {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (a[j] < a[minIdx]) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                int tmp = a[i];
                a[i] = a[minIdx];
                a[minIdx] = tmp;
            }
        }
    }

    // =====================================================================
    //  2) Insertion Sort  -  O(N) في أفضل حالة، O(N^2) في الأسوأ
    // =====================================================================
    static void insertionSort(int[] a) {
        int n = a.length;
        for (int i = 1; i < n; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= 0 && a[j] > key) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }

    // =====================================================================
    //  3) Bubble Sort (محسّن بعلامة swapped) - O(N) أفضل حالة، O(N^2) أسوأ
    // =====================================================================
    
    static void bubbleSort(int[] a) {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    int tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                    swapped = true;
                }
            }
            if (!swapped) break; // المصفوفة مرتبة بالفعل
        }
    }

    // =====================================================================
    //  4) Merge Sort  -  O(N log N) في جميع الحالات
    // =====================================================================


    static void mergeSort(int[] a) {
        int[] temp = new int[a.length]; // مصفوفة مساعدة تُنشأ مرة واحدة
        mergeSort(a, temp, 0, a.length - 1);
    }

    private static void mergeSort(int[] a, int[] temp, int left, int right) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSort(a, temp, left, mid);
        mergeSort(a, temp, mid + 1, right);
        merge(a, temp, left, mid, right);
    }

    private static void merge(int[] a, int[] temp, int left, int mid, int right) {
        System.arraycopy(a, left, temp, left, right - left + 1);
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) a[k++] = temp[i++];
            else                    a[k++] = temp[j++];
        }
        while (i <= mid) a[k++] = temp[i++];
        while (j <= right) a[k++] = temp[j++];
    }

    // =====================================================================
    //  5) Quick Sort  -  O(N log N) متوسط، O(N^2) أسوأ حالة
    //     نستخدم "median-of-three" لاختيار المحور لتجنب الأسوأ حالة
    //     على البيانات المرتبة/المعكوسة، ونعيد الاستدعاء على الجزء الأصغر
    //     أولاً لتجنب StackOverflowError.
    // =====================================================================


    static void quickSort(int[] a) {
        quickSort(a, 0, a.length - 1);
    }

    private static void quickSort(int[] a, int low, int high) {
        while (low < high) {
            int p = partition(a, low, high);
            // نعالج الجزء الأصغر بالاستدعاء الذاتي، والأكبر بالحلقة
            if (p - low < high - p) {
                quickSort(a, low, p - 1);
                low = p + 1;
            } else {
                quickSort(a, p + 1, high);
                high = p - 1;
            }
        }
    }

    private static int partition(int[] a, int low, int high) {
        // اختيار المحور بطريقة median-of-three
        int mid = low + (high - low) / 2;
        if (a[mid] < a[low])  swap(a, mid, low);
        if (a[high] < a[low]) swap(a, high, low);
        if (a[high] < a[mid]) swap(a, high, mid);
        swap(a, mid, high); // نضع المحور في النهاية
        int pivot = a[high];

        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (a[j] <= pivot) {
                i++;
                swap(a, i, j);
            }
        }
        swap(a, i + 1, high);
        return i + 1;
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    // =====================================================================
    //  توليد حالات الاختبار الأربع
    // =====================================================================

    /** غير مرتبة: أعداد عشوائية تماماً */
    static int[] Random(Random rnd) {
        int[] a = new int[N];
        for (int i = 0; i < N; i++) a[i] = rnd.nextInt(1_000_000);
        return a;
    }

    /** مرتبة تماماً: من الأصغر إلى الأكبر */
    static int[] Sorted(Random rnd) {
        int[] a = generateRandom(rnd);
        Arrays.sort(a);
        return a;
    }

    /** مرتبة عكسياً: من الأكبر إلى الأصغر */
    static int[] Reversed(Random rnd) {
        int[] a = Sorted(rnd);
        for (int i = 0, j = a.length - 1; i < j; i++, j--) {
            swap(a, i, j);
        }
        return a;
    }

    /** مرتبة جزئياً بنسبة 80%: نرتب المصفوفة ثم نبعثر 20% من العناصر عشوائياً */
    static int[] PartiallySorted(Random rnd) {
        int[] a = Sorted(rnd);
        int scrambleCount = (int) (N * 0.20);       // 20,000 عنصر
        int[] positions = new int[scrambleCount];
        // اختيار مواضع عشوائية متفرقة
        for (int i = 0; i < scrambleCount; i++) positions[i] = rnd.nextInt(N);
        // خلط القيم الموجودة في هذه المواضع فيما بينها
        for (int i = scrambleCount - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            swap(a, positions[i], positions[j]);
        }
        return a;
    }

    // =====================================================================
    //  أدوات القياس
    // =====================================================================

    /** هل المصفوفة مرتبة تصاعدياً؟ (للتحقق من صحة الخوارزمية فقط) */
    static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1] > a[i]) return false;
        }
        return true;
    }

    /** تنفيذ الخوارزمية على نسخة مستقلة وقياس زمن الترتيب فقط (بالميكروثانية) */
    static double measure(String name, int[] original) {
        int[] copy = original.clone();             // نسخة مستقلة ومطابقة
        long start = System.nanoTime();            // بداية القياس
        runSort(name, copy);
        long end = System.nanoTime();              // نهاية القياس
        if (!isSorted(copy)) {                     // خارج وقت القياس
            throw new IllegalStateException(name + " failed to sort correctly!");
        }
        return (end - start) / 1000.0;             // نانو ثانية -> ميكروثانية
    }

    static void runSort(String name, int[] a) {
        switch (name) {
            case "Selection Sort": selectionSort(a); break;
            case "Insertion Sort": insertionSort(a); break;
            case "Bubble Sort":    bubbleSort(a);    break;
            case "Merge Sort":     mergeSort(a);     break;
            case "Quick Sort":     quickSort(a);     break;
            default: throw new IllegalArgumentException(name);
        }
    }

    /** تسخين JIT على مصفوفة صغيرة حتى لا تتأثر أول قراءة */
    static void warmUp() {
        Random rnd = new Random(1);
        String[] algos = {"Selection Sort", "Insertion Sort", "Bubble Sort",
                          "Merge Sort", "Quick Sort"};
        for (int round = 0; round < 20; round++) {
            int[] small = new int[1000];
            for (int i = 0; i < small.length; i++) small[i] = rnd.nextInt(10_000);
            for (String algo : algos) runSort(algo, small.clone());
        }
    }

    // =====================================================================
    //  main
    // =====================================================================
    public static void main(String[] args) {
        System.out.println("Running benchmark with N = " + N + " ... please wait.\n");
        warmUp();

        Random rnd = new Random(SEED);

        // توليد بيانات الحالات الأربع مرة واحدة (وقت التوليد لا يُقاس)
        int[][] testCases = {
            Random(rnd),
            Reversed(rnd),
            PartiallySorted(rnd),
            Sorted(rnd)
        };

        String[] algorithms = {
            "Selection Sort", "Insertion Sort", "Bubble Sort",
            "Merge Sort", "Quick Sort"
        };

        // نتائج القياس [الخوارزمية][الحالة]
        double[][] results = new double[algorithms.length][testCases.length];

        for (int a = 0; a < algorithms.length; a++) {
            for (int t = 0; t < testCases.length; t++) {
                results[a][t] = measure(algorithms[a], testCases[t]);
            }
        }

        // ------------------- طباعة الجدول المنظم -------------------
        String line = "+------------------+----------------+----------------+----------------+----------------+";
        System.out.println(line);
        System.out.printf("| %-16s | %14s | %14s | %14s | %14s |%n",
                "Algorithm", "Random", "Reversed", "Partially", "Fully Sorted");
        System.out.println(line);
        for (int a = 0; a < algorithms.length; a++) {
            System.out.printf("| %-16s | %11.0f \u00b5s | %11.0f \u00b5s | %11.0f \u00b5s | %11.0f \u00b5s |%n",
                    algorithms[a], results[a][0], results[a][1], results[a][2], results[a][3]);
        }
        System.out.println(line);
        System.out.println("Time is measured in microseconds (\u00b5s), N = " + N);
    }
}
