package Chapter6;

import Chapter4.Dish;
import Chapter4.Type;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class CollectStream {

    /*Collectors class
    1) Reducing and summarizing stream elements to a single value
    2) Grouping elements
    3) Partitioning elements
    All the arguments to  collect method are toList, reducing, groupingBy, joining here.
    All those methods return type is collector interface (Or Some implementation of Collector Interface)
    All of them are defined in  collectors class
    collect method is in Stream class, that can take any argument of type collector interface
    * */

    public enum CaloricLevel {DIET, NORMAL, FAT}

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

        System.out.println("--------------------TOTAL DISHES----------------------");
        long totalDishes = menu.stream().count();
        System.out.println("TOTAL DISHES ARE" +totalDishes);
        System.out.println("--------------------HIGHEST CALORIE DISH-----------------");
        Optional<Dish> maxCalorieDish = menu.stream().max(Comparator.comparingInt(Dish::getCalories));
        System.out.println(maxCalorieDish.get());

        Optional<Dish> maxCalorieDish1 = menu.stream().collect(maxBy(Comparator.comparingInt(Dish::getCalories)));
        System.out.println(maxCalorieDish1.get());

        System.out.println("----------------------SUMMING INT-----------------------");
        //Note that we did not map it to int using map(Dish::getCalories)
        // Previously we used to do that
        int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
        System.out.println("TOTAL CALORIES ARE "+totalCalories);

        System.out.println("------------AVERAGING CALORIES---------------------------");
        double avgCalories = menu.stream().collect(averagingInt(Dish::getCalories));
        System.out.println("AVERAGE CALORIES ARE "+avgCalories);

        System.out.println("-----------------COLLECTING STATS------------------------");
        IntSummaryStatistics summaryStatistics = menu.stream().collect(summarizingInt(Dish::getCalories));
        System.out.println(summaryStatistics.getMax()+" "+ summaryStatistics.getAverage());

        System.out.println("-----------------JOINING STRINGS--------------------------");
        String shortMenu = menu.stream().map(Dish::getName).collect(joining(","));
        System.out.println(shortMenu);

//        menu.stream().collect(reducing(((o, o2) ->  o.getName()+ o2.getName()))).get();
//        The above line will not work as one argument that reducing accepts is a  BinaryOperator<T> that’s a BiFunction<T,T,T>
//        That is 2 arguments and 1 return type all should be same
//        reducing can take and returns a collector of type Collector<T, ?, U>
//          1) BinaryOperator<T> op -> BiFunction<T,T,T> ---------------- on line 81 RETURNS Collector<T, ?, Optional<T>>
//          2) T identity, BinaryOperator<T> op ----------- on line 67 RETURNS Collector<T,?,T>
//          3) U identity, Function<? extends T, ? extends U> mapper, BinaryOperator<U> op --------- on line 77 RETURNS Collector<T, ?, U>

        System.out.println("--------------GENERAL REDUCING----------------------------");
        int reduceTotCal = menu.stream().collect(reducing(0, Dish::getCalories, (i, j) -> i + j));
        System.out.println("USING GENERAL REDUCING "+reduceTotCal);

        Optional<Dish> mostCalorieDish = menu.stream()
                .collect(reducing((o, o2) -> o.getCalories() > o2.getCalories()? o:o2));
        //Perl style please don't do it

        int reduceTotCal3 = menu.stream()
                                .collect(reducing(0, Dish::getCalories, (c1, c2) -> c1+c2));
        System.out.println("STYLE 3 reducing "+ reduceTotCal3);

        /*groupingBy

        groupingBy(Function<? super T, ? extends K>) classifier) RETURN TYPE Collector<T,  ? , Map<K, List<T>>>
        Eg Line X
        Dish - T
        Dish::getType - Gives a Type which is K (By this time you should understand that this is Function<Dish, Type>)
        So, return type is surely Map<Type, List<Dish>>

        groupingBy(Function<? super T,? extends K> classifier, Collector<? super T,A,D> downstream) RETURN TYPE Collector<T,?,Map<K,D>>
        Eg Line X
        Dish - T
        Dish:: getType - Gives a type which is K
        D - RETURN type of second collector , which recursively falls to 1st groupingBy and the return type (D) is derived by that.
        * */
        System.out.println("-----------------------GROUPING-----------------------------");
        System.out.println("-------------------GROUP BY DISH TYPE-----------------------");
        Map<Type, List<Dish>> dishByType = menu.stream().collect(groupingBy(Dish::getType));
        System.out.println(dishByType);

        System.out.println("-------------------COMPLEX GROUPING-------------------------");
        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream()
                .collect(groupingBy(dish -> {
                    if (dish.getCalories() < 400) return CaloricLevel.DIET;
                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                    else return CaloricLevel.FAT;
                }));

        System.out.println(dishesByCaloricLevel);
        System.out.println("---------------------MULTI LEVEL GROUPING-------------------");
        Map<Type, Map<CaloricLevel, List<Dish>>> dishByTypeCaloricLevel =
                menu.stream()
                .collect(groupingBy(Dish::getType,
                        groupingBy(dish -> {
                            if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                            else return CaloricLevel.FAT;
                        })
                ));
        System.out.println(dishByTypeCaloricLevel);

        System.out.println("-------------------COLLECT DATA IN GROUPING-------------------");
        Map<Type, Long> typesCount = menu.stream()
                .collect(groupingBy(Dish::getType, counting()));
        System.out.println("Count GROUP BY"+typesCount);

        Map<Type, Optional<Dish>> maxCaloricByType = menu.stream()
                .collect(groupingBy(Dish::getType, maxBy(Comparator.comparingInt(Dish::getCalories))));
        System.out.println("Max GROUP BY"+ maxCaloricByType);

        System.out.println("-------------HIGHEST CALORIE DISH GROUPWISE WO OPTIONAL--------------");
        /*
        This factory method takes two arguments, the collector to be adapted and a transformation
        function, and returns another collector.
        */
        Map<Type, Dish> mostCaloricByPtype = menu.stream()
                .collect(groupingBy(Dish::getType,
                        collectingAndThen(
                                maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get
                        )));

        mostCaloricByPtype.forEach((type, dish) -> System.out.println(type +"->" +dish));

        System.out.println("--------------------SUM ALL CALORIES GROUPWISE---------------------");
        Map<Type, Integer> totalCaloriesByType = menu.stream()
                .collect(groupingBy(Dish::getType,
                        summingInt(Dish::getCalories)));
        System.out.println(totalCaloriesByType);


        System.out.println("-------------------------MAPPING----------------------------------");
        Map<Type, Set<CaloricLevel>> caloricLevelsByType = menu.stream()
                .collect(groupingBy(Dish::getType,
                        mapping(dish -> {
                            if (dish.getCalories() < 400) return CaloricLevel.DIET;
                            else if (dish.getCalories() < 700) return CaloricLevel.NORMAL;
                            else return CaloricLevel.FAT;
                        }, toSet())));
        System.out.println(caloricLevelsByType);

        System.out.println("---------------------PARTITIONING----------------------------------");
        /*
        Partitioning is a special kind of grouping.
        It has a Predicate in partitioningBy, remember groupBy had Function<T,R>
        And IntelliJ is smart enough to suggest you from your Dish, only isVeg reference can go there
        * */

        Map<Boolean, List<Dish>> partitionedMenu = menu.stream()
                .collect(partitioningBy(Dish::isVegetarian));
        System.out.println(partitionedMenu);

//        List<Dish> vegitarianDishes = menu.stream().filter(Dish::isVegetarian).collect(toList());
        System.out.println("-----------PARTITION VEG----------- GROUP BY TYPE--------------");
        Map<Boolean, Map<Type, List<Dish>>> vegitarianDishesByType = menu.stream()
                .collect(partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType)));
        System.out.println(vegitarianDishesByType);

        Map<Boolean, Dish> mostCaloricPartitionedByVegetarian = menu.stream()
                .collect(partitioningBy(Dish::isVegetarian,
                        collectingAndThen(maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get)));
        System.out.println(mostCaloricPartitionedByVegetarian);

//        Map<Boolean, Optional<Dish>> collect = menu.stream().collect(groupingBy(Dish::isVegetarian,
//                maxBy(Comparator.comparingInt(Dish::getCalories)))); THE Optional version

        Map<Boolean, Long> vegNonVegCount = menu.stream().collect(partitioningBy(Dish::isVegetarian, counting()));
        System.out.println(vegNonVegCount);

        System.out.println("------------------------IDENTITY---------------------------------");
        List<Integer> integerList = Arrays.asList(1,2,3,4,5,3,4,2,1,3,6,7,8,7,6,8,6,5,4,2,3,4,7,9,5,4,1,2,1);

        Integer maxKey = integerList.stream()
                .collect(groupingBy(Function.identity(), counting()))  //{1=4, 2=4, 3=4, 4=5, 5=3, 6=3, 7=3, 8=2, 9=1}
                .entrySet()  // Convert it into set so that stream operation can be applied again
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get()
                .getKey();
        System.out.println("MOST RECURRING KEY IN LIST IS "+maxKey);

        Integer maxKey1 = integerList.stream()
                .collect(groupingBy(Function.identity(), counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
        System.out.println("MOST RECURRING KEY IN LIST IS "+maxKey1);

        System.out.println("--------------A LOT TO READ FOR THIS ONE AND MAKE NOTES TOO----------------");
        System.out.println("------------------------USING CUSTOM COLLECTOR-----------------------------");
        List<Dish> customCollect = menu.stream().collect(new ToListCollector<>());
        System.out.println(customCollect);


    }
}
