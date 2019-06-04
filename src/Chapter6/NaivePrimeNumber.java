package Chapter6;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/*
* This is a fairly nice solution but we can optimize it more
* The issue is that we are checking every element from 2 to sqrt in % check
* In, theory we can just check all the prime numbers in that list, for that we need a stream with those prime numbers.
* We can make isPrime recursive but that defeats the purpose
* */

public class NaivePrimeNumber {

    public static boolean isPrime(int candidate) {
        int sqrt = (int)Math.sqrt(candidate);
        return IntStream.rangeClosed(2, sqrt).noneMatch(value -> candidate%value == 0);
    }

    public static Map<Boolean, List<Integer>> partitionPrimes(int n) {
        return IntStream.rangeClosed(2,n).boxed().collect(Collectors.partitioningBy(o -> isPrime(o)));
    }

    public static void main(String[] args) {
        System.out.println(partitionPrimes(17));
    }

}
