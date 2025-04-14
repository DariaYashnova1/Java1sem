package queue;

/*Inv:
    size >= 0
    0<=<
*/
public class ArrayQueueADT {
    private Object[] elements = new Object[100];
    private int size = 0;
    private int head = 0;
    private int tail = 0;

    private static int nextIndex(ArrayQueueADT que, int ind) {
        return (ind + 1) % que.elements.length;
    }

    private static int prevIndex(ArrayQueueADT que, int ind) {
        --ind;
        if(ind == -1) {
            ind += que.elements.length;
        }
        return ind;
    }

    //Pre: size >= 0 && que is not null
    private static void ensureCapacity(ArrayQueueADT que) {
        if (que.elements.length <= que.size) {
            Object[] temp = new Object[que.size * 2 + 1];
            int ind, head = que.head, tail = que.tail;
            if (head <= tail) {
                System.arraycopy(que.elements, head, temp, 0,tail - head);
                ind = tail - head;
            } else {
                ind = que.elements.length - head;
                System.arraycopy(que.elements, head, temp, 0, ind);
                System.arraycopy(que.elements, 0, temp, ind, tail);
                ind += tail;
            }
            que.elements = temp;
            que.head = 0;
            que.tail = ind;
        }
    }
    //if there is no place for elements (que.tail + 1 >= que.elements.length) or (que.head - 1 < 0)
    // we will doEnsureCapacity:
    // elements.length = (10/3)(size+!) &&
    // there is place for adding elements : 2/3(size+1) from the both sides
    // queue is unchanged


    /*Pred: queue is not null && elem != null
     */
    public static void enqueue(ArrayQueueADT que, Object elem) {
        assert elem != null;
        ++que.size;
        ensureCapacity(que);
        que.elements[que.tail] = elem;
        que.tail = nextIndex(que, que.tail);
    }
    /* Post: sizenew = size + 1 &&
            for all elements in queue: newqueue[i]=queue[i] &&
            que[prevsize] = elem
     */

    /*Pred: elem != null && queue is not null
     */
    public static void push(ArrayQueueADT que, Object elem) {
        assert elem != null;
        ++que.size;
        ensureCapacity(que);
        que.head = prevIndex(que, que.head);
        que.elements[que.head] = elem;
    }
    /* Post: sizenew = size + 1 &&
            for all elements in queue: newqueue[i]=queue[i+1] &&
            que[prevsize+1] = elem
     */

    // Pre: size > 0 && queue is not null
    public static Object element(ArrayQueueADT que) {
        assert !isEmpty(que);
        return que.elements[que.head];
    }
    //Post: res=que[0]

    /*Pred: queue is not null && size > 0*/
    public static Object dequeue(ArrayQueueADT que) {
        assert !isEmpty(que);
        --que.size;
        Object res = que.elements[que.head];
        que.head = nextIndex(que, que.head);
        return res;
    }
    /*Post: res = que[0] && size_new = size_old - 1 &&
            newqueue[i] = queue[i-1], for all element without a[0];*/


    public static Object peek(ArrayQueueADT que) {
        assert !isEmpty(que);
        return que.elements[prevIndex(que, que.tail)];
    }

    //Pred:  queue is not null && size > 0
    public static Object remove(ArrayQueueADT que) {
        assert !isEmpty(que);
        Object res = peek(que);
        que.tail = prevIndex(que, que.tail);
        --que.size;
        return res;
    }
    //Post: res= elements[que.tail] && sizenew = size - 1 &&
    //      for all element without elements[que.tail]: elementnew == elementprev

    //Pred: elem is not null && que is not null
    public static int count(ArrayQueueADT que, Object elem) {
        assert elem != null;
        int count = 0;
        for (int i = que.head; i != que.tail; i = nextIndex(que, i)) {
            if (que.elements[i].equals(elem)) {
                ++count;
            }
        }
        return count;
    }
    //Post: que is unchanged, res >0 if que contains elem, otherwise res=0

    private static int modBySize(ArrayQueueADT que, int a) {
        return a < 0 ? a + que.elements.length : a;
    }

    //Pred: elem is not null && que is not null
    public static int indexOf(ArrayQueueADT que, Object elem) {
        assert elem != null;
        for (int i = que.head; i != que.tail; i = nextIndex(que, i)) {
            if (que.elements[i].equals(elem)) {
                return modBySize(que, i - que.head);
            }
        }
        return -1;
    }
    //Post: que is unchanged, res>=0 if que contains elem: for all 0<i<res+que.head elements[i]!=elem
    //otherwise res=-1

    //Pred: elem is not null && que is not null
    public static int lastIndexOf(ArrayQueueADT que, Object elem) {
        assert elem != null;
        for (int i = prevIndex(que, que.tail); i != prevIndex(que, que.head); i = prevIndex(que, i)) {
            if (que.elements[i].equals(elem)) {
                return modBySize(que, i - que.head);
            }
        }
        return -1;
    }
    //Post: que is unchanged, res>=0 if que contains elem: for all res+que.head<i<res+size elements[i]!=elem
    //otherwise res=-1


    /*Pre: queue is not null*/
    public static int size(ArrayQueueADT que) {
        return que.size;
    }
    /*Post: res = size && queue is unchanged*/


    /* Pre: queue != null*/
    public static boolean isEmpty(ArrayQueueADT que) {
        return size(que) <= 0;
    }
    /* Post: queue is unchanged && (res = false && size > 0 || size == 0 && res == true)  */


    /* Pre:queue is not null */
    public static void clear(ArrayQueueADT que) {
        que.elements = new Object[100];
        que.size = 0;
        que.head = 0;
        que.tail = 0;
    }
    /* Post: sizenew = 0 */
}
