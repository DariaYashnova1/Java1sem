package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public class LinkedQueue extends AbstractQueue {
    private static class Node {
        Node next;
        Node prev;
        Object value;

        public Node(Object value) {
            this.value = value;
        }
    }

    private Node head = null;
    private Node tail = null;

    public void enqueue(Object elem) {// добавить элемент в очередь;
        assert elem != null;
        Node newElem = new Node(elem);
        if (!isEmpty()) {
            tail.next = newElem;
            tail = newElem;
        } else {
            tail = head = newElem;
        }
        ++size;
        tail.next = head;
        head.prev = tail;
    }

    public Object element() {
        assert !isEmpty();
        return head.value;
    }

    public Object peek() {//вернуть последний элемент в очереди;
        assert !isEmpty();
        return tail.value;
    }


    public int count(Object elem) {
        assert elem != null;
        int count = 0;
        Node temp = head;
        for (int i = 0; i < size; i++) {
            if (temp.value == elem) {
                count++;
            }
            temp = temp.next;
        }
        return count;
    }

    public LinkedQueue filter(Predicate<Object> cond) {
        LinkedQueue res = new LinkedQueue();
        Node temp = head;
        for (int i = 0; i < size; i++) {
            if (cond.test(temp.value)) {
                res.enqueue(temp.value);
            }
            temp = temp.next;
        }
        return res;
    }

    public LinkedQueue map(Function<Object, Object> func) {
        LinkedQueue res = new LinkedQueue();
        Node temp = head;
        for (int i = 0; i < size; i++) {
            res.enqueue(func.apply(temp.value));
            temp = temp.next;
        }
        return res;
    }


    public int indexOf(Object elem) {
        assert elem != null;
        Node temp = head;
        for (int i = 0; i < size; i++) {
            if (temp.value.equals(elem)) {
                return i;
            }
            temp = temp.next;
        }
        return -1;
    }

    public int lastIndexOf(Object elem) {
        assert elem != null;
        Node temp = head;
        int ind = -1;
        for (int i = 0; i < size; i++) {
            if (temp.value.equals(elem)) {
                ind = i;
            }
            temp = temp.next;
        }
        return ind;
    }

    @Override
    protected void deleteFirst() {
        assert !isEmpty();
        if (size > 1) {
            head = head.next;
            tail.next = head;
            head.prev = tail;
        } else {
            head = null;
            tail = null;
        }
        size--;
    }

    @Override
    protected void deleteLast() {
        assert !isEmpty();
        if (size > 1) {
            tail = tail.prev;
            tail.next = head;
            head.prev = tail;
        } else {
            head = null;
            tail = null;
        }
        size--;
    }

    @Override
    protected void pushImpl(Object elem) {
        Node newElem = new Node(elem);
        newElem.next = head;
        newElem.prev = tail;
        head.prev = newElem;
        head = newElem;
        tail.next = newElem;
    }

}
