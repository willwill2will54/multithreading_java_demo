import java.util.ArrayDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class queue <E> {

    private final ArrayDeque<E> array;
    private final Semaphore available;
    private final ReentrantLock lock;

    public queue() {
        available = new Semaphore(0);
        array = new ArrayDeque<>();
        lock = new  ReentrantLock();
    }

    public E remove() throws InterruptedException {
        available.acquire();
        lock.lock();
        E value = array.remove();
        lock.unlock();
        return value;
    }

    public void add(E value) throws InterruptedException {
        available.release();
        lock.lock();
        array.add(value);
        lock.unlock();
    }

    public int length(){
        lock.lock();
        int len = array.size();
        lock.unlock();
        return len;
    }

}
