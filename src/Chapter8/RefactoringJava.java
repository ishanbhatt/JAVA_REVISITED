package Chapter8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RefactoringJava {

    interface Task {
        void execute();
    }

    public static void doSomething(Runnable r) {r.run();}
    public static void doSomething(Task t) {t.execute();}

    public static void main(String[] args) {

//        doSomething(() -> System.out.println("PROBLAMTIC LAMBDA"));
//        The above line will not compile as both Runnable and Task are functional interface
//        So, compiler will not know whether to call doSomething with Task/Runnable, but there's a way out
        doSomething((Task)() -> System.out.println("CASTED LAMBDA"));

//        Introduce more functional interfaces in the codebase
//        2 common code patterns that can be refactored to leverage lambda expression
//        1. Conditional Deferred Execution
//        2. Execute around
        System.out.println("----------PEEKING INTO STREAM----------------");
//          Let's you peek into the stream without ever consuming it
        List<Integer> numbers = Arrays.asList(1,2,3,4,5);

        numbers.stream()
                .peek(x -> System.out.println("FROM STREAM "+x))
                .map(x -> x + 17)
                .peek(x -> System.out.println("AFTER MAP" +x))
                .filter(x ->x%2==0)
                .limit(3)
                .collect(Collectors.toList());



    }
}
