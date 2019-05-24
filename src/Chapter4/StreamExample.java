package Chapter4;

import java.util.Arrays;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import java.util.List;
import java.util.function.Predicate;

public class StreamExample {

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

        List<String> lowCaloriesDishesName =
                menu.stream()
                    .filter(d -> d.getCalories() < 400)
                    .sorted(comparing(Dish::getCalories))
                    .map(Dish::getName)
                    .collect(toList());

        lowCaloriesDishesName.forEach(System.out::println);

        System.out.println("---------------------ONLY VEGEARIAN DISHES--------------------------");
        menu.stream()
                .filter(Dish::isVegetarian)
                .forEach(System.out::println);

        System.out.println("--------------HIGH CALORIE CODE IN FILTER----------------------------");
//        List<String> highCal = menu.stream()
//                .filter({System.out.println();
//                        return Dish::getCalories > 500;   DON'T BE SMART, THIS WON'T WORK
//                                                          NEED TO DO d -> {}
//                })
        List<String> highCal = menu.stream()
                                .filter(d -> {
                                    System.out.println("FILTERING "+ d.getName());
                                    return d.getCalories() > 500;
                                })
                                .map(d -> {
                                    System.out.println("MAPPING "+ d.getName());
                                    return d.getName();
                                })
                                .limit(3)
                                .collect(toList());
        /*
        * You can notice several optimizations due to the lazy nature of streams. First, despite the fact
        that many dishes have more than 300 calories, only the first three are selected! This is because
        of the limit operation and a technique called short-circuiting, as we’ll explain in the next chapter.
        Second, despite the fact that filter and map are two separate operations, they were merged into
        the same pass (we call this technique loop fusion).
        * */
    }
}
