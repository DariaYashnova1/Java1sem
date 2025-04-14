package queue;

public abstract class AbstractQueue implements Queue {
    public int size = 0;


    protected void deleteFirst() {
    }

    protected void deleteLast() {
    }

    protected void pushImpl(Object elem) {
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void push(Object elem) {
        assert elem != null;

        ++size;
        pushImpl(elem);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public Object remove() {
        assert !isEmpty();
        Object res = peek();
        deleteLast();
        return res;
    }

    @Override
    public void clear() {
        while(!isEmpty()){
            dequeue();
        }
    }

    @Override
    public Object dequeue() {
        assert !isEmpty();
        Object res = element();
        deleteFirst();
        return res;
    }
}
