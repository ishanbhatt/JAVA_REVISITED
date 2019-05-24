package Chapter3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LambdaExamples {

    public static List<Apple> filterApples(List<Apple> apples, Predicate<Apple> p) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple: apples)
        {
            if(p.test(apple)) result.add(apple);
        }
        return result;
    }

    public static void main(String[] args) {
        List<Apple> apples = new ArrayList<>();

        apples.add(new Apple(150, "Green"));
        apples.add(new Apple(250, "Red"));
        apples.add(new Apple(350, "Blue"));
        apples.add(new Apple(450, "Green"));
        apples.add(new Apple(170, "Red"));
        apples.add(new Apple(180, "Red"));
        apples.add(new Apple(190, "Red"));
        apples.add(new Apple(300, "Blue"));
        apples.add(new Apple(400, "Blue"));

        // Get only red apples
        System.out.println(filterApples(apples, apple -> "Red".equals(apple.getColor())));

        // list heavier apples
        System.out.println(filterApples(apples, apple -> apple.getWeight() > 300));

        // Heavier apples with stream
        apples.stream().filter(apple -> apple.getWeight() > 300).forEach(System.out::println);

        System.out.println("-------------------------STREAM FILTER---------------------------");
        // Blue heavier apples
        apples.stream().filter(apple -> "Blue".equals(apple.getColor()) && apple.getWeight()>300)
                                .forEach(System.out::println);

        System.out.println("----------------------SORT LAMBDA WEIGHT------------------------");
        // Sorting apples by Weight
        apples.sort(Comparator.comparingInt(Apple::getWeight));
        apples.forEach(System.out::println);

        // Creating lambda and using it separately
        System.out.println("----------------------SORT SEPERATE LAMBDA COLOR------------------------");
        Comparator<Apple> c = Comparator.comparing(Apple::getColor);
        apples.sort(c);
        apples.forEach(System.out::println);

        System.out.println("----------------------CONSUME LAMBDA ADD COLOR FUNCTIONAL WITH ARG-----------------------");
        Consumer<Apple> consumer = apple -> System.out.print("ADD "+apple.getColor());
        apples.forEach(consumer.andThen(apple -> System.out.println(" LALALA")));


        Supplier<Apple> s = () -> new Apple(5000, "BLACK");
        apples.add(s.get());

        System.out.println("----------------------FUNCTIONAL INTERFACE-----------------------");
        Function<Apple, Integer> divideByTen = apple -> apple.getWeight()/10;
        List<Integer> weights = apples.stream().map(a -> divideByTen.apply(a)).collect(Collectors.toList());
        weights.forEach(System.out::println);

    }
}
