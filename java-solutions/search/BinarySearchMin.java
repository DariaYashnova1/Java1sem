package search;

public class BinarySearchMin {

    /*Pred:
        Exists d: 0<=d<arr.length
        i,j<=d: i<j arr[i]>arr[j]
        i,j>=d: i>j arr[i]>arr[j]
     */
    public static void main(String[] args) {
        int[] arr = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            arr[i] = Integer.parseInt(args[i]);
        }

        System.out.println(recur(arr, -1, arr.length));
        //System.out.println(iter(arr));
    }
    /*Post:
        Exists d: 0<=d<arr.length
        i,j<=d: i<j arr[i]>arr[j]
        i,j>=d: i>j arr[i]>arr[j]

        res=arr[d]:
        (res==min(arr[0], arr[1], ... ,arr[arr.length-1])
     */


    /*Pred:
        Exists d: 0<=d<arr.length
        i,j<=d: i<j arr[i]>arr[j]
        i,j>=d: i>j arr[i]>arr[j]

       -1 <= l < arr.length &&
       -1 < r <= arr.length &&
       r - l >= 1
     */
    private static int recur(int[] arr, int l, int r){
        /*  -1 <= l < arr.length &&
           -1 < r <= arr.length &&
           l < r && r - l >= 1
         */

        if (r - l == 1) {
            /*  r - l == 1 &&
               -1 < r <= arr.length &&
               -1 <= l < arr.length &&

               ((r=0 && arr.length!=1 => arr[r]<=arr[r+1] -> return arr[0]) ||
               (r=arr.length-1 && arr.length!=1=> arr[r-1] => arr[r] -> return arr[arr.length - 1]) ||
                (0<r<arr.length -1 => arr[r-1]>= arr[r] <= arr[r+1] -> return arr[r]))
             */
            return arr[r];
        }
        /*  -1 <= l < arr.length &&
           -1 < r <= arr.length &&
            r - l > 1
         */
        int middle = (r + l) / 2;
       /*  -1 <= l < arr.length &&
               -1 < r <= arr.length &&
               r - l > 1 &&
               -1 < middle < arr.length && middle = (l + r) / 2 (down)
          */
        if(middle+1>=arr.length){
            /*middle<arr.length, but middle+1>=arr.length ->
              middle = arr.length - 1
             */
            return arr[middle];
        }
        /*
              -1<middle<arr.length-1 && 0<middle+1<arr.length
         */

        if (arr[middle]<arr[middle+1]) {
            /*
             -1 <= l < arr.length &&
               rnew = middle &&
               -1 < rnew < arr.length &&
               1 <= rnew - l && rnew < r
               in the part of array, which we will explore next will be condition:
                Exists d: 0<=d<arr.length
                i,j<=d: i<j arr[i]>arr[j]
                i,j>=d: i>j arr[i]>arr[j]
             */
            return recur(arr, l, middle);
        } else {
            /*  lnew == middle &&
               -1 < lnew < arr.length &&
               -1 < r <= arr.length &&
               1 <= r - lnew && lnew > l &&
               in the part of array, which we will explore next will be condition:
                Exists d: 0<=d<arr.length
                i,j<=d: i<j arr[i]>arr[j]
                i,j>=d: i>j arr[i]>arr[j]
             */
            return recur(arr, middle, r);
        }
    }
    /*Post:
         Exists d: 0<=d<arr.length
         i,j<=d: i<j arr[i]>arr[j]
         i,j>=d: i>j arr[i]>arr[j]

        res=arr[d]:
        (res=min(arr[0], arr[1], ... ,arr[arr.length-1])
     */

    /*Pred:
            Exists d: 0<=d<arr.length
            i,j<=d: i<j arr[i]>arr[j]
            i,j>=d: i>j arr[i]>arr[j]
     */
    private static int iter(int[] arr) {
        int l = -1;
        int r = arr.length;
        /* -1 == l < arr.length &&
           -1 < r == arr.length &&
           r - l >= 1
         */
        while (r - l > 1) {
            /* -1 <= l < arr.length &&
               -1 < r <= arr.length &&
                r - l > 1
             */
            int middle =  (r + l) / 2;
            /*  -1 <= l < arr.length &&
               -1 < r <= arr.length &&
               r - l > 1 &&
               -1 < middle < arr.length && middle = (l + r) / 2 (down)
             */
            if(middle+1>=arr.length){
                /*middle<arr.length, but middle+1>=arr.length ->
                middle = arr.length - 1
                 */
                return arr[middle];
            }
            /*
              -1<middle<arr.length-1 && 0<middle+1<arr.length
             */
            if (arr[middle]<arr[middle+1]) {
                /*
               -1 <= l < arr.length &&
               rnew = middle &&
               -1 < rnew < arr.length &&
               1 <= rnew - l && rnew < r
               in the part of array, which we will explore next will be condition:
                Exists d: 0<=d<arr.length
                i,j<=d: i<j arr[i]>arr[j]
                i,j>=d: i>j arr[i]>arr[j]
             */
                r = middle;
            } else {
                /*  lnew == middle &&
               -1 < lnew < arr.length &&
               -1 < r <= arr.length &&
               1 <= r - lnew && lnew > l &&
               in the part of array, which we will explore next will be condition:
                Exists d: 0<=d<arr.length
                i,j<=d: i<j arr[i]>arr[j]
                i,j>=d: i>j arr[i]>arr[j]
             */
                l = middle;
            }
        }
        /*-1 == l < arr.length &&
           -1 < r == arr.length &&
           r - l = 1, because of the "while" condition*/
        return arr[r];
    }
/*Post:
        res=arr[d]:
        (res=min(arr[0], arr[1], ... ,arr[arr.length-1])
     */
}
