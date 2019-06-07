package Chapter8;

import java.util.ArrayList;
import java.util.List;

interface Observer {
    void notify(String tweet);
}

class NYTimes implements Observer {

    @Override
    public void notify(String tweet) {
        if(tweet != null && tweet.contains("money")) {
            System.out.println("Breaking News in NY "+ tweet);
        }
    }
}

class Guardian implements Observer {

    @Override
    public void notify(String tweet) {
        if(tweet != null && tweet.contains("queen")) {
            System.out.println("Breaking News in LONDON "+ tweet);
        }
    }
}

class LeMonde implements Observer {

    @Override
    public void notify(String tweet) {
        if(tweet != null && tweet.contains("wine")) {
            System.out.println("CHEESE WINE NEWS "+ tweet);
        }
    }
}

interface Subject {
    void registerObserver(Observer o);
    void notifyObservers(String tweet);
}

class Feed implements Subject {

    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers(String tweet) {
        observers.forEach(observer -> observer.notify(tweet));
    }
}

public class ObserverDP {

    public static void main(String[] args) {
        Feed f = new Feed();
        f.registerObserver(new NYTimes());
        f.registerObserver(new Guardian());
        f.registerObserver(new LeMonde());

        f.registerObserver(tweet -> {
            if(tweet != null && tweet.contains("india")) System.out.println("NEWS IN DELHI "+tweet);
        });

        f.notifyObservers("The queen of the world");
        f.notifyObservers("india politics");
    }
}
