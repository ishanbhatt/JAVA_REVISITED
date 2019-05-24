package Chapter5;

import Chapter4.Dish;
import Chapter4.Type;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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



    }
}
