package Chapter11;

import java.util.concurrent.Future;

public class AsyncTester {

    public static void doSomethingElse() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) { }
    }

    public static void main(String[] args) {
        Shop shop = new Shop("BestShop");
        Future<Double> futurePrice = shop.gerPriceAsync("sports car");

        doSomethingElse();

        try {
            double price = futurePrice.get();
            System.out.println(price);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
