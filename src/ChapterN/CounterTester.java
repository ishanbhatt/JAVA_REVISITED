package ChapterN;

public class CounterTester {

    public static void main(String[] args) {
        Counter c1 = new Counter();

        Thread t1 = new Thread(() -> {
            c1.inc();
        });

        Thread t2 = new Thread(() -> {
            c1.inc();
        });

        Thread t3 = new Thread(() -> {
            c1.inc();
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {}

        System.out.println(c1.inc());


    }
}
