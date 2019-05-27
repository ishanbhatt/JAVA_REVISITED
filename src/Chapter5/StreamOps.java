package Chapter5;

import Chapter4.Dish;
import Chapter4.Type;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamOps {

    public static void main(String[] args) {
        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Type.MEAT),
                new Dish("beef", false, 700, Type.MEAT),
                new Dish("chicken", false, 400, Type.MEAT),
                new Dish("french fries", true, 530, Type.OTHER),
                new Dish("rice", true, 350, Type.OTHER),
                new Dish("season fruit", true, 120, Type.OTHER),
                new Dish("pizza", true, 550, Type.OTHER),
                new Dish("prawns", false, 300, Type.FISH),
                new Dish("salmon", false, 450, Type.FISH)
        );

        System.out.println("----------------VEG DISHES-------------------------");

        List<Dish> vegMenu = menu.stream()
                                .filter(Dish::isVegetarian)
                                .collect(Collectors.toList());
        System.out.println(vegMenu);

        System.out.println("---------------DISTINCT VALUES-----------------------");
        List<Integer> numbaers = Arrays.asList(1,2,1,3,3,2,4,6,6,7,8);
        numbaers.stream()
                .filter(i -> i%2 ==0)
                .distinct()
                .forEach(System.out::println);

        System.out.println("--------------LIMIT STREAM---------------------------");
        List<Dish> dishes = menu.stream()
                                .filter(dish -> dish.getCalories() > 300)
                                .limit(3)
                                .collect(Collectors.toList());
        System.out.println(dishes);

        System.out.println("--------------SKIPPING ELEMENTS-----------------------");
        dishes = menu.stream()
                    .filter(Dish::isVegetarian)
                    .skip(2)
                    .collect(Collectors.toList());
        System.out.println(dishes);

        System.out.println("----------------FLATTEN STREAMS------------------------");
//        List out all the unique characters from the list of string
        List<String> words = Arrays.asList("Hello", "World");
        List<String[]> collect = words.stream()
                .map(word -> word.split(""))
                .distinct()
                .collect(Collectors.toList());
//        The above gives you list of string array, we want a list of string, Use flatMap
        /*
        The issue above is that the map method returns an array String[]
        So, the stream returned by map is of type Stream<String[]>, but we want Stream<String>
        So, the last collect will be collecting String[] not string
        * */

        List<String> uniqueChars = words.stream()
                .map(w -> w.split(""))
                .flatMap(Arrays::stream)  // Flattens each generated stream into a single stream
                .distinct()
                .collect(Collectors.toList());
        /*
        Stream<String[]> gets converted into a single stream Stream<String> then the distinct works properly
        As distinct has only one stream to look for not a Stream<Stream>
        So, in a way you can say that flatMap has following effect
        It flattens out the String[] into one long String.
        All the separate streams that were generated when using map(Arrays::stream) get amalgamated—flattened into a single stream.
        NEED MORE EXAMPLE FOR FLATMAP
        * */
        System.out.println(uniqueChars);

        System.out.println("-------------All Any None MATCH-------------------");
//        They are short circuit operations
        if(menu.stream().anyMatch(Dish::isVegetarian))
        {
            System.out.println("Well There's something for VEG people");
        }

        if(menu.stream().allMatch(d -> d.getCalories() < 1000))
        {
            System.out.println("It's not so UNHEALTHY restaurant");
        }

        if(menu.stream().noneMatch(d-> d.getCalories() > 1000))
        {
            System.out.println("LALALALALALA");
        }

        System.out.println("------------FIND ONE - FIND MANY------------------");
        menu.stream()
                .filter(Dish::isVegetarian)
                .findAny().ifPresent(dish -> System.out.println(dish.getName()));

        List<Integer> nums = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        Optional<Integer> firstSqDivThree = nums.stream()
                                                .map(x -> x*x)
                                                .filter(x -> x%3==0)
                                                .findFirst();
        System.out.println(firstSqDivThree.orElse(null));
//        findAny is more parallelism friendly as finding 1st element is more constraining than any element

        System.out.println("------------------REDUCE---------------------------");
//        Combine elements of the collection to sum,find max,min etc

        int sum = nums.stream().reduce(0, Integer::sum);
        System.out.println("SUM OF NUMBERS "+sum);  //unboxing happens automatically

//        Press ctrl + alt + v after you type in the expression
//        IntilliJ will suggest you the correct return type
        Optional<Integer> reduce = nums.stream().reduce((a, b) -> a + b);
        System.out.println("WITHOUT DEFAULT "+reduce);

        Optional<Integer> maxVal = nums.stream().reduce(Integer::max);
        System.out.println("MAXIMUM "+maxVal.orElse(-1));

//        Keep an eye on which operations are stateful and stateless
//        map. filter take one element from dish at a time and do something on them, thus stateless
//        reduce, sum,max operate on multiple values keeping track of previous ones, thus stateful
//        See Table 5.1 in Java In Action for exhaustive lising

        int calories = menu.stream()
                            .map(Dish::getCalories)
                            .reduce(0,Integer::sum);
        // Above is a nasty one on performance side
        // As before doing sum every boxed calorie needs to be unboxed

        // Also map returns Stream<T>, it does not have sum method
        // As it is a generic thing, so to solve it you need to create
        // IntStream, FloatStream, LongStream etc they have arithmetic operations
        // Conversions from IntStream to Stream<Integer> can be done using boxed(), unboxed()
        calories = menu.stream()
                        .mapToInt(Dish::getCalories)
                        .sum();
        System.out.println(calories);

//        In case of max/min on int stream, it doesn't make sense to give a default value
//        As it's possible that Stream's actual max value is 0(default)
//        So, for max,min the result type is  OptionalInt,Double,FLoat
        OptionalInt max = menu.stream().mapToInt(Dish::getCalories).max();
        System.out.println("MAX is "+max.orElse(-1));

        System.out.println("-----------------RANGE BASED METHODS------------------------");
        IntStream evenNumbers = IntStream.rangeClosed(1,100).filter(value -> value%2==0);
        System.out.println(evenNumbers.count());


    }
}
