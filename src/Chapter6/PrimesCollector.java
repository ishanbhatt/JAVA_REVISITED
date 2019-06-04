package Chapter6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;




public class PrimesCollector implements Collector<Integer,
        Map<Boolean, List<Integer>>,
        Map<Boolean, List<Integer>> {

    public static <A> List<A> takeWhile(List<A> list, Predicate<A> p) {
        int i = 0;

        for(A item: list)
        {
            if (! p.test(item)) return list.subList(0,i);
            i++;
        }
        return list;
    }

    public static boolean isPrime(List<Integer> primes, int candidate) {
        int sqrt = (int) Math.sqrt((double) candidate);
        return takeWhile(primes, i -> i < sqrt).stream().noneMatch(p -> candidate%p == 0);
    }

    @Override
    public Supplier<Map<Boolean, List<Integer>>> supplier() {
//        Supplier returns the basic structure in which the result would be accumulated
//        We had to put initial key,val in it as we do not have something like defaultdict here
        return () -> new HashMap<>() {{
           put(true, new ArrayList<>());
           put(false, new ArrayList<>());
        }};
    }

    @Override
    public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
        /*
        * Using inner acc.get(true) we get a list of primes collected so far
        * We pass that list to isPrime with Candidate for which we want to check
        * isPrime can return true/false, and for that key we actually add the candidate
        * This by this method being called for every element the Map<> returned by supplier will keep on growing
        * */
        return (acc, candidate) -> acc.get(isPrime(acc.get(true), candidate)).add(candidate);
    }


}
