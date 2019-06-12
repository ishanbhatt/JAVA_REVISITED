package Chapter11;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Shop {

    private final String name;

    public Shop(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private void delay()
    {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private double calculatePrice(String product) {
        delay();
        return new Random().nextDouble() * product.charAt(0) + product.charAt(1);
    }

    /* NORMAL Version
    public double getPrice(String product) {
        return calculatePrice(product);
    }
    */

    public Future<Double> gerPriceAsync(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        Runnable r = () -> {
            try {
                double price = calculatePrice(product);
                futurePrice.complete(price);  // Complete it with the price
            } catch (Exception e) {
                futurePrice.completeExceptionally(e);  // Save the exception so that it is visible to outer thread too
            }

        };

        new Thread(r).start();  // Start the thread with this runnable
        /* CAN USE THIS AS WELL
        new Thread(() -> {
            double price = calculatePrice(product);
            futurePrice.complete(price);
        }).start();
        */
        return futurePrice;

//        Comparator<Integer> x = (o1, o2) ->  THAT'S HOW YOU DO MULTIPLE ARG LAMBDA in case if you forgot
    }

    public Future<Double> getAsyncPriceLessVerbose(String product) {
        return CompletableFuture.supplyAsync(
                () -> calculatePrice(product));
//        This is as good as the method above with all exception handling and all
    }
}
