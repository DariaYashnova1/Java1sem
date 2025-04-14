package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Queue {

    // Pre: elem != null
    //Post: a[prevIndex(tail)]=elem && for any others a[i]=a'[i] && n'=n+1,
    void enqueue(Object elem);

    // Pre: n > 0
    //Post: Res=a[1] && n'=n && for all i: 0<i<=n a'[i]=a[i]
    Object element();

    //Pre: n>0
    //Post: for i: 1..n-1 a'[i]=a[i+1] && n'=n-1 && res = a[1]
    Object dequeue();

    //Pred: always true
    //Post:n=0
    void clear();

    // Pre: n > 0
    //Post: Res=a[n] && n'=n && for all i: 0<i<=n a'[i]=a[i]
    Object peek();

    //Pre: elem!=null
    //Post: a[1]=elem, for all i: 2..n a'[i]=a[i-1] && n'=n+1
    void push(Object elem);

    //Pre:elem!=null
    //Post: for i: 1..n a'[i] = a[i] && res = |M| : M=que^E, E={elem} && n'=n
    int count(Object elem);

    //Pre:elem!=null
    //Post: for all i < res a[i]!=elem && n'=n && for i: 1..n a'[i]=a[i]
    int indexOf(Object elem);

    //Pre:elem!=null
    //Post: for all i < res a[i]!=elem && n'=n && for i: 1..n a'[i]=a[i]
    int lastIndexOf(Object elem);

    //Pred: func is not null
    //Post: func[i]=f(a[i]), for i:1..n a'[i]=a[i] && n'=n
    public Queue map(Function<Object, Object> func);

    //Pred: cond is not null
    //Post: for all a[i]: cond(a[i](i: 1...n))=true , all a[i] is in res &&
    // if a[i] and a[j] is in res, i<j :a[i]->res[l]   a[j]->res[m] => l<m
    public Queue filter(Predicate<Object> cond);

    //Pre: n>0
    //Post: for i: 1..n-1 a'[i]=a[i] && n'=n-1 && res=a[n]
    Object remove();

    //Pred: always true
    //Post:res=n, queue is immutable
    int size();

    //Pred: always true
    //Post:res= (n<=0), queue is immutable
    boolean isEmpty();

}
