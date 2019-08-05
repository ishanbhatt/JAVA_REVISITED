package ChapterN;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Counter {

    /*
    * Contrary to popular belief(passing the lock object in constructor), https://www.geeksforgeeks.org/reentrant-lock-java/
    * In the above case the class would be a runnable, you will create multiple instance of the runnable passing the same lock object
    * 
    * Here, We create one lock object per counter.
    * One counter object will be created and that will be shared by different threads.
    *
    * */
    private Lock lock = new ReentrantLock();
    private int count = 0;

    public int inc() {
        lock.lock();
        int newCount = ++count;
        lock.unlock();
        return newCount;
    }

    public Lock getLock() {
        return lock;
    }
}
