package search;

public class BinarySearch {

    /*
    P: For all i<j arr[i]>=arr[j]
       x - integer
       exists i : arr[i] <= x
     */
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] arr = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            arr[i - 1] = Integer.parseInt(args[i]);
        }

        System.out.println(iter(arr, x));
    }
    /*
     Q:  (x < arr[arr.length - 1] && res == arr.length ||
        arr[res] <= x && (res - 1 < 0 || arr[res - 1] > x))
     */

    /*
    P:  For all i<j arr[i]>=arr[j] &&
       -1 < r <= arr.length &&
       -1 <= l < arr.length &&
       l < r && r - l >= 1
     */
    private static int recur(int[] arr, int x, int l, int r){
        /*
            -1 < r <= arr.length &&
            -1 <= l < arr.length &&
             r - l >= 1
        */
        if (r - l == 1) {
            /*
                r - l == 1 &&
               -1 < r <= arr.length &&
               (x < arr[arr.length - 1] && r == arr.length ||
               arr[r] <= x && (r - 1 < 0 || arr[r - 1] > x))
             */
            return r;
        }
        /*
           -1 <= l < arr.length &&
           -1 < r <= arr.length &&
            r - l > 1
         */

        int middle = (r + l) / 2;
        /*  -1 <= l < arr.length &&
            -1 < r <= arr.length &&
            l < r && r - l > 1 &&
            -1 < middle < arr.length && middle == (l + r) / 2 (down)
         */
        if (x >= arr[middle]) {
            /*  -1 <= l < arr.length &&
               rnew == middle &&
               -1 < rnew < arr.length &&
               1 <= rnew - l && rnew < r &&
             */
            return recur(arr, x, l, middle);
        } else {
            /*  lnew == middle &&
               -1 < lnew < arr.length &&
               -1 < r <= arr.length &&
               1 <= r - lnew && lnew > l &&
             */
            return recur(arr, x, middle, r);
        }
    }
    /*Q: For all i<j arr[i]>=arr[j]
       (x < arr[arr.length - 1] && res == arr.length ||
       arr[res] <= x && (res - 1 < 0 || arr[res - 1] > x))
     */

    /*
    P:  For all i<j arr[i]>=arr[j] &&
     */
    private static int iter(int[] arr, int x) {
        int l = -1;
        int r = arr.length;
        /* -1 == l < arr.length &&
           -1 < r == arr.length &&
           r - l >= 1
         */
        while (r - l > 1) {
            /* -1 <= l < arr.length &&
               -1 < r <= arr.length &&
               l < r && r - l > 1
             */
            int middle = l + (r - l) / 2;
            /* -1 <= l < arr.length &&
               -1 < r <= arr.length &&
                r - l > 1 &&
               -1 < middle < arr.length && middle == (l + r) / 2 (down)
             */
            if (x >= arr[middle]) {
                /*  -1 <= l < arr.length &&
                   -1 < middle < arr.length &&
                   1 <= middle - l && middle < r
                 */
                r = middle;
                /*  rnew == middle &&
                    1 < rnew < arr.length &&
                   -1 <= l < arr.length &&
                   rnew - l >= 1;
                 */
            } else {
                /*  -1 < middle < arr.length &&
                   -1 < r <= arr.length &&
                   1 <= r - middle && middle > l
                 */
                l = middle;
                /* lnew == middle &&
                   1 <lnew < arr.length &&
                   -1 < r <= arr.length &&
                   r -lnew >= 1
                 */
            }
            /*  -1 <= lnew < arr.length &&
              -1 < rnew <= arr.length &&
              rnew - lnew >= 1 && (lnew > l || rnew < r)
             */
        }
        /*  -1 <= l < arr.length &&
           -1 < r <= arr.length &&
           r - l == 1 &&
           (x < arr[arr.length - 1] && r == arr.length ||
           arr[r] <= x && (r - 1 < 0 || arr[r - 1] > x))
         */
        return r;
    }
    /*  Q: arr[i] >= arr[j], for any i < j &&
       (x < arr[arr.length - 1] && res == arr.length ||
       arr[res] <= x && (res - 1 < 0 || arr[res - 1] > x))
     */
}
