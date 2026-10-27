import java.util.Arrays;
import java.util.Random;

public class BubbleSortSimple {

    // دالة تحول من نانو ثانية الى ميكرو ثانية
    public static long nanoToMicro(long nano) {
        return nano / 1000;
    }

    // خوارزمية bubble sort
    public static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    // نبدل مكان العنصرين
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            // اذا ما صار اي تبديل معناها المصفوفة مرتبة
            if (swapped == false) {
                break;
            }
        }
    }

    public static void main(String[] args) {
        int n = 100000;

        // السيناريو 1: مصفوفة عشوائية
        int[] randomArray = new int[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            randomArray[i] = rand.nextInt(100000);
        }

        // السيناريو 2: مصفوفة مرتبة تماما
        int[] sortedArray = new int[n];
        for (int i = 0; i < n; i++) {
            sortedArray[i] = i;
        }

        // نسخة من المصفوفة العشوائية عشان ما نغير الاصلية
        int[] test1 = Arrays.copyOf(randomArray, randomArray.length);
        long start1 = System.nanoTime();
        bubbleSort(test1);
        long end1 = System.nanoTime();
        long time1 = nanoToMicro(end1 - start1);

        // نسخة من المصفوفة المرتبة
        int[] test2 = Arrays.copyOf(sortedArray, sortedArray.length);
        long start2 = System.nanoTime();
        bubbleSort(test2);
        long end2 = System.nanoTime();
        long time2 = nanoToMicro(end2 - start2);

        // طباعة النتائج
        System.out.println("Algorithm    | Random        | Sorted");
        System.out.println("-----------------------------------------");
        System.out.println("Bubble Sort  | " + time1 + " µs | " + time2 + " µs");
    }
}
