import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class shuttle <E> implements Runnable{
    private class shuttleWorker implements Runnable{

        private final queue<E> inputQueue;

        public shuttleWorker(queue<E> input){
            inputQueue = new queue<>();
        }

        @Override
        public void run() {
            while (true){
                try {
                    destinationQueue.remove().add(inputQueue.remove());
                } catch (InterruptedException ignored) {

                }
            }
        }
    }

    private final ArrayList<shuttleWorker> workers;

    private final ArrayList<queue<E>> inQueues;
    private final ArrayList<queue<E>> outQueues;

    queue<E> destination;

    private final ReentrantLock inQueueLock;
    private final ReentrantLock outQueueLock;

    @Override
    public void run() {
        while (true) {
            int min = -1;
            for (queue<E> q:
                 outQueues) {
                int len = q.length();
                if (min == -1) {
                    min = q.length();
                } else if (len < min){
                    min = len;
                    destination = q;
                }
                if (len == 0) {
                    destination = q;
                    break;
                }

            }
        }
    }

    public shuttle() {
        inQueueLock = new ReentrantLock();
        outQueueLock = new ReentrantLock();
        inQueues = new ArrayList<>();
        outQueues = new ArrayList<>();
        workers = new ArrayList<>();
    }

    public queue<E> getInputQueue() {
        queue<E> q = new queue<>();
        initInQueue(q);
        return q;
    }

    public sizedQueue<E> getInputQueue(int size) {
        sizedQueue<E> q = new sizedQueue<>(size);
        initInQueue(q);
        return q;
    }

    public queue<E> getOutputQueue() {
        queue<E> q = new queue<>();
        initOutQueue(q);
        return q;
    }

    public sizedQueue<E> getOutputQueue(int size){
        sizedQueue<E> q = new sizedQueue<>(size);
        initOutQueue(q);
        return q;
    }

    private void initInQueue(queue<E> q){
        inQueueLock.lock();
        inQueues.add(q);
        inQueueLock.unlock();

        new Thread(new shuttleWorker(q)).start();
    }

    private void initOutQueue(queue<E> q){
        outQueueLock.lock();
        outQueues.add(q);
        outQueueLock.unlock();
    }
}