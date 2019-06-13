package Chapter11;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class AsyncTester {

    public static void doSomethingElse() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) { }
    }

    public static List<String> findPricesStream(List<Shop> shops, String product) {
        return shops.stream()
                .map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(toList());

    }

    public static List<String> findPricesParStream(List<Shop> shops, String product) {
        return shops.parallelStream()
                .map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(toList());

    }

    public static List<String> findPricesAsync(List<Shop> shops, String product) {
        /*
        * There is a REASON why the operation is divided in two streams
        * We could have just added another map after with map(CompletableFuture::join)
        * But that would slow up the entire pipeline, as Streams intermediate operations are lazy by nature
        * if you had processed the stream in a single pipeline,
        * you would have succeeded only in executing all the requests to different shops synchronously and sequentially.
        * This is because the creation of each CompletableFuture to
        * interrogate a given shop would start only when the computation of the previous one had
        * completed, letting the join method return the result of that computation
        * */
        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getName() + " Price is " + shop.getPrice(product)
                ))
                .collect(toList());

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    public static List<String> findPriceWithExecutor(List<Shop> shops, String product, Executor executor) {

        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getName() + " Price is " + shop.getPrice(product), executor
                ))
                .collect(toList());

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }


    public static void main(String[] args) {
        Shop shop = new Shop("BestShop");
        Future<Double> futurePrice = shop.getAsyncPriceLessVerbose("sports car");

        doSomethingElse();

        try {
            double price = futurePrice.get();
            System.out.println(price);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        System.out.println("----------------ASSUMING NOW WE ONLY HAVE SYNC VERSION--------------------");
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
        System.out.println(findPricesStream(shops, "Iphone10S"));
        long duration = (System.nanoTime() - start)/ 1_000_000;
        System.out.println("Done in "+ duration + " msecs");

        System.out.println("------------------PARALLEL STREAM--------------------");
        start = System.nanoTime();
        System.out.println(findPricesParStream(shops, "Iphone10S"));
        duration = (System.nanoTime() - start)/ 1_000_000;
        System.out.println("Done in "+ duration + " msecs");

        System.out.println("------------------ASYNC COMPARABLE--------------------");
        start = System.nanoTime();
        System.out.println(findPricesAsync(shops, "Iphone10S"));
        duration = (System.nanoTime() - start)/ 1_000_000;
        System.out.println("Done in "+ duration + " msecs");

        System.out.println("-------------------CREATING CUSTOM EXECUTOR-------------------");

//        Create a custom executor that is to be used by supplyAsync
//        It works faster because default number of threads used by supplyAsync = Ncpu
//        Here we are giving it a chance to spawn a lot of more threads
        Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100),
                r -> { // The 2nd argument is an object of ThreadFactory, which is a functional interface
                       // It has a single newThread method that takes a runnable, so r is that Runnable here
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    return t;
                }
        );
        start = System.nanoTime();
        System.out.println(findPriceWithExecutor(shops, "Iphone10S", executor));
        duration = (System.nanoTime() - start)/ 1_000_000;
        System.out.println("Done in "+ duration + " msecs");

        System.out.println("USE PARALLEL STREAM FOR CPU BOUND TASKS AND COMPARABLEFUTURE FOR IO BOUND");


    }
}
