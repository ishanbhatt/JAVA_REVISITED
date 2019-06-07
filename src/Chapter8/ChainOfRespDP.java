package Chapter8;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

abstract class ProcessingObject<T> {
//    See how the parameter type is provided in class and function defination

    protected ProcessingObject<T> successor;

    public void setSuccessor(ProcessingObject<T> successor) {
        this.successor = successor;
    }
//    See how return type and arguments are defined
    public T handle(T input) {
        T r = handleWork(input);
        if(successor != null)
        {
            return successor.handle(r);
        }
        return r;
    }

    abstract T handleWork(T input);
}

class Header extends ProcessingObject<String> {

    @Override
    public String handleWork(String input) {
        return "FROM RAUL MARIO "+input;
    }
}

// How types are defined in extends, implements
class SpellCheck extends ProcessingObject<String> {

    @Override
    String handleWork(String input) {
        return input.replaceAll("labda", "lambda");
    }
}

public class ChainOfRespDP {
//    The chain of responsibility pattern is a common solution to create a chain of processing objects
//    (such as a chain of operations).
//    First we will do it with abstract classes

    public static void main(String[] args) {
        ProcessingObject<String> p1 = new Header();
        ProcessingObject<String> p2 = new SpellCheck();

        p1.setSuccessor(p2);

        System.out.println(p1.handle("Are labda sexy"));

        System.out.println("USING LAMBDA");

        UnaryOperator<String> header = s -> "FROM RAUL MARIO "+s;
        UnaryOperator<String> spellChecker = s -> s.replaceAll("labda", "lambda");

        // This can be a unary operator but if creates confusion to do so
        Function<String, String> pipe = header.andThen(spellChecker);

        System.out.println(pipe.apply("ARE labda cool"));
    }


}
