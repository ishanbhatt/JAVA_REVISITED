package Chapter11;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/*
* To simulate multiple waiting syncronous task. Here's how you make sense of it.
* findPrices remains the same
* getPriceDiscount -> Gives shopname, price, discountCode (BLOCKING)
* From the string above, we parse it and create Quote with those params
* And we applyDiscount on that quote (BLOCKING)
* So, if we just chain-up those operations it will be 2Seconds per shop
* */


public class ChainFutures {


    public static List<String> findPrices(List<Shop> shops,String product) {

        List<String> discountedPrices = shops.stream()
                .map(shop -> shop.getPriceDiscount(product))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(Collectors.toList());
        return discountedPrices;

    }


    public static List<String> findPricesAsync(List<Shop> shops, String product, Executor executor) {
        /*
        * A lot is going on here, let's break it down
        * the first map is the same as the previous we had used in AsyncTester's findPricesAsync
        * Now, that returns a Stream of Future<String>, but to get the discount we need to convert it into Quote
        * As, Quote::parse is non blocking immidiate returning function, it can be
        * performed almost instantaneously and can be done synchronously without introducing any
        * delay. that's why we use thenApply method Note that using the thenApply method doesn’t block your code
        * until the Completable-Future on which you’re invoking it is completed.
        * It will convert Stream<Future<String>> to Stream<Future<Quote>>
        * THAT'S HOW YOU ALWAYS DO WHEN YOU WANT TO CONVERT Future<X> to Future<Y>
        *
        * 2nd operation(Discount.applyDiscount(quote)) is blocking one , to cascade these 2 blocking ops, you need thenCompose
        * you can compose two CompletableFutures by invoking the thenCompose method on the first CompletableFuture
        * and passing to it a Function.
        * It uses the result(quote) returned by first CompletableFuture when ic completes, and it returns a second
        * CompletableFuture that uses the result of the first as input for its computation.
        *
        * So, Blocking task followed by non blocking -use thenApply
        * Blocking after blocking - use thenCompose
        * */

        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceDiscount(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(
                                () -> Discount.applyDiscount(quote), executor
                        )))
                .collect(Collectors.toList());

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {

        List<Shop> shops = List.of(new Shop("BestPrice"),
                new Shop("LetsSaveBig"),
                new Shop("MyFavouriteShop"),
                new Shop("BuyItAll"),
                new Shop("Amazon"),
                new Shop("EBay"),
                new Shop("FlipKart"),
                new Shop("Myntra"),
                new Shop("Koovs"),
                new Shop("Bewakoof"),
                new Shop("LocalShop")
        );

        System.out.println("-----------------NORMAL STREAM------------------------");
        long start = System.nanoTime();
        System.out.println(findPrices(shops, "Iphone10S"));
        long duration = (System.nanoTime() - start)/ 1_000_000;
        System.out.println("Done in "+ duration + " msecs");

        Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100),
                r -> { // The 2nd argument is an object of ThreadFactory, which is a functional interface
                    // It has a single newThread method that takes a runnable, so r is that Runnable here
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    return t;
                }
        );

        System.out.println("--------------WITH NEW EXECUTOR AND ASYNC---------------------");
        start = System.nanoTime();
        System.out.println(findPricesAsync(shops, "Iphone10S", executor));
        duration = (System.nanoTime() - start)/ 1_000_000;
        System.out.println("Done in "+ duration + " msecs");

    }
}
