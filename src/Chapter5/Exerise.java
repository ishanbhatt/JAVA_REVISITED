package Chapter5;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Exerise {


//    Add custom comparator here and see if that works
//    https://stackoverflow.com/questions/45550934/sort-a-list-of-objects-based-on-a-parameterized-attribute-of-the-object
    /*public static Comparator<MyObject> getComparator(String s) {
    switch(s) {
        case "attr1": return Comparator.comparing(MyObject::getAttr1);
        case "attr2": return Comparator.comparing(MyObject::getAttr2);
    }
    throw new IllegalArgumentException(s);
}
    * */
    public static void main(String[] args) {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");

        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );

        System.out.println("1.Find all transactions in the year 2011 and sort them by value (small to high).");
        List<Transaction> ans1 = transactions.stream()
                .filter(transaction -> transaction.getYear()==2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .collect(Collectors.toList());
        System.out.println(ans1);
        System.out.println();

        System.out.println("2. What are all the unique cities where the traders work?");
        List<String> ans2 = transactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getCity)
                .distinct()
                .collect(Collectors.toList());
        ans2.forEach(System.out::println);
        System.out.println();

        System.out.println("3. Find all traders from Cambridge and sort them by name");
        Stream<Trader> ans3 = transactions.stream()
                .map(Transaction::getTrader)
                .filter(trader -> trader.getCity() == "Cambridge")
                .distinct()
                .sorted(Comparator.comparing(Trader::getName));
        ans3.forEach(System.out::println);
        System.out.println();

        System.out.println("4. Return a string of all traders’ names sorted alphabetically");
        String ans4 = transactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getName)
                .sorted()
                .reduce("", (s, s2) -> s + s2);
        System.out.println(ans4);
        System.out.println();

        System.out.println("5. Are any traders based in Milan?");
        boolean ans5 = transactions.stream()
                .map(Transaction::getTrader)
                .anyMatch(trader -> trader.getCity().equals("Milan"));
        System.out.println(ans5);
        System.out.println();

        System.out.println("6. Print all transactions’ values from the traders living in Cambridge");
        Stream<Integer> ans6 = transactions.stream()
                .filter(transaction -> "Cambridge".equals(transaction.getTrader().getCity()))
                .map(Transaction::getValue);
        ans6.forEach(System.out::println);
        System.out.println();

        System.out.println("7. What’s the highest value of all the transactions?");
        // You can do it using reduce(Integer::max), but we'll do something nasty
        int ans7 = transactions.stream()
                .map(Transaction::getValue)
                .max(Integer::compareTo).get(); // Unboxing gets done
//              .max(Comparator.comparing(Integer::valueOf))
        System.out.println(ans7);
        System.out.println();

        System.out.println();
        System.out.println("8. Find the transaction with the smallest value");
        Transaction transaction = transactions.stream()
                .min(Comparator.comparing(Transaction::getValue)).get();
        System.out.println(transaction);

        System.out.println("9. Sum of all trades by mario");
        int ans8 = transactions.stream()
                .filter(transaction1 -> transaction1.getTrader().getName().equals("mario"))
                .mapToInt(Transaction::getValue)
                .sum();

    }
}
