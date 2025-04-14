package queue;


import java.util.function.Function;
import java.util.function.Predicate;

//a[1]...a[n]
//Inv: a[i] != null for i = 1..n && n>0
public class ArrayQueue extends AbstractQueue {
    private Object[] elements = new Object[100];
    private int head = 0;
    private int tail = 0;

    private int nextIndex(int ind) {
        return (ind + 1) % elements.length;
    }

    private int prevIndex(int ind) {
        --ind;
        if(ind == -1) {
            ind += elements.length;
        }
        return ind;
    }

    private int modBySize(int a) {
        return a < 0 ? a + elements.length : a;
    }

    private void ensureCapacity() {
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

    // Pre: elem != null
    //Post: a[prevIndex(tail)]=elem && for any others a[i] nothing changed && n'=n+1,
    public void enqueue(Object elem) {
        assert elem != null;
        ++size;
        ensureCapacity();
        elements[tail] = elem;
        tail = nextIndex(tail);
    }

    //Pred: cond is not null
    //Post: for all cond(a[i](i: 1...n))=true, all a[i] is in res &&
    // if a[i] and a[j] is in res, i<j :a[i]->res[l]   a[j]->res[m] => l<m
    public ArrayQueue filter(Predicate<Object> cond) {
        ArrayQueue res = new ArrayQueue();
        for (int i = head; i != tail; i = nextIndex(i)) {
            if (cond.test(elements[i])) {
                res.enqueue(elements[i]);
            }
        }
        return res;
    }

    //Pred: func is not null
    //Post: func[i]=f(a[i]), for i:1..n a'[i]=a[i] && n'=n
    public ArrayQueue map(Function<Object, Object> func) {
        ArrayQueue res = new ArrayQueue();
        res.elements = new Object[elements.length + 1];
        for (int i = head; i != tail; i = nextIndex(i)) {
            res.enqueue(func.apply(elements[i]));
        }
        return res;
    }

    // Pre: n > 0
    //Post: Res=a[1] && n'=n && for all i: 0<i<=n a'[i]=a[i]
    public Object element() {
        assert !isEmpty();
        return elements[head];
    }

    // Pre: n > 0
    //Post: Res=a[n] && n'=n && for all i: 0<i<=n a'[i]=a[i]
    public Object peek() {
        assert !isEmpty();
        return elements[prevIndex(tail)];
    }

    //Pre:elem!=null
    //Post: for i: 1..n a'[i] = a[i] && res = the number of occurrences of the element && n'=n
    public int count(Object elem) {
        assert elem != null;
        int count = 0;
        for (int i = head; i != tail; i = nextIndex(i)) {
            if (elements[i].equals(elem)) {
                ++count;
            }
        }
        return count;
    }

    //Pre:elem!=null
    //Post: for all i < res a[i]!=elem && n'=n && for i: 1..n a'[i]=a[i]
    public int indexOf(Object elem) {
        assert elem != null;
        for (int i = head; i != tail; i = nextIndex(i)) {
            if (elements[i].equals(elem)) {
                return modBySize(i - head);
            }
        }
        return -1;
    }

    //Pre:elem!=null
    //Post: for all i < res a[i]!=elem && n'=n && for i: 1..n a'[i]=a[i]
    public int lastIndexOf(Object elem) {
        assert elem != null;
        for (int i = prevIndex(tail); i != prevIndex(head); i = prevIndex(i)) {
            if (elements[i].equals(elem)) {
                return modBySize(i - head);
            }
        }
        return -1;
    }

    //Pre: n>0
    //Post: for i: 1..n-1 a'[i]=a[i+1] && n'=n-1
    @Override
    protected void deleteFirst() {
        assert !isEmpty();
        head = nextIndex(head);

        --size;
    }

    //Pre: n>0
    //Post: for i: 1..n-1 a'[i]=a[i] && n'=n-1
    @Override
    protected void deleteLast() {
        assert !isEmpty();
        tail = prevIndex(tail);
        --size;
    }


    @Override
    protected void pushImpl(Object elem) {
        ensureCapacity();
        head = prevIndex(head);
        elements[head] = elem;
    }

}
