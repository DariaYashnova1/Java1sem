package queue;

public class ArrayQueueModule {
    private static Object[] elements = new Object[100];
    private static int size = 0;
    private static int head = 0;
    private static int tail = 0;

    private static int nextIndex(int ind) {
        return (ind + 1) % elements.length;
    }

    private static int prevIndex(int ind) {
        --ind;
        if(ind == -1) {
            ind += elements.length;
        }
        return ind;
    }

    //Pre: sz >= 0
    private static void ensureCapacity() {
        if (elements.length <= size) {
            Object[] temp = new Object[size * 2 + 1];
            int ind;
            if (head <= tail) {
                System.arraycopy(elements, head, temp, 0,tail - head);
                ind = tail - head;
            } else {
                ind = elements.length - head;
                System.arraycopy(elements, head, temp, 0, ind);
                System.arraycopy(elements, 0, temp, ind, tail);
                ind += tail;
            }
            elements = temp;
            head = 0;
            tail = ind;
        }
    }
    // Post: (sz < elem'.length <= sz * 4) && (n' == n) && (a'[i] == a[i] for i = 0...n - 1)

    public static void enqueue(Object elem) {
        assert elem != null;
        ++size;
        ensureCapacity();
        elements[tail] = elem;
        tail = nextIndex(tail);
    }


    public static Object element() {
        assert !isEmpty();
        return elements[head];
    }

    public static Object dequeue() {
        assert !isEmpty();
        --size;
        Object res = elements[head];
        head = nextIndex(head);
        return res;
    }

    public static Object peek() {
        assert !isEmpty();
        return elements[prevIndex(tail)];
    }

    public static void push(Object elem) {
        assert elem != null;
        ++size;
        ensureCapacity();
        head = prevIndex(head);
        elements[head] = elem;
    }

    public static Object remove() {
        assert !isEmpty();
        Object res = peek();
        tail = prevIndex(tail);
        --size;
        return res;
    }

    public static int count(Object elem) {
        assert elem != null;
        int count = 0;
        for (int i = head; i != tail; i = nextIndex(i)) {
            if (elements[i].equals(elem)) {
                ++count;
            }
        }
        return count;
    }

    private static int modBySize(int a) {
        return a < 0 ? a + elements.length : a;
    }

    public static int indexOf(Object elem) {
        assert elem != null;
        for (int i = head; i != tail; i = nextIndex(i)) {
            if (elements[i].equals(elem)) {
                return modBySize(i - head);
            }
        }
        return -1;
    }

    public static int lastIndexOf(Object elem) {
        assert elem != null;
        for (int i = prevIndex(tail); i != prevIndex(head); i = prevIndex(i)) {
            if (elements[i].equals(elem)) {
                return modBySize(i - head);
            }
        }
        return -1;
    }

    public static int size() {
        return size;
    }

    public static boolean isEmpty() {
        return size() <= 0;
    }

    public static void clear() {
        elements = new Object[100];
        size = 0;
        head = 0;
        tail = 0;
    }
}
