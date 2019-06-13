package Chapter11;

import java.util.List;
import java.util.stream.Collectors;

public class ChainFutures {


    public static List<String> findPrices(List<Shop> shops,String product) {

        List<String> discountedPrices = shops.stream()
                .map(shop -> shop.getPriceDiscount(product))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(Collectors.toList());
        return discountedPrices;

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
    }
}
