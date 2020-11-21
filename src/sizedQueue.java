import java.util.concurrent.Semaphore;

public class sizedQueue <E> extends queue<E> {
    private final Semaphore empty;

    public sizedQueue(int size){
        super();
        empty = new Semaphore(size);
    }

    @Override
    public E remove() throws InterruptedException {
        E value = super.remove();
        empty.release();
        return value;
    }

    @Override
    public void add(E value) throws InterruptedException {
        empty.acquire();
        super.add(value);
    }
}
