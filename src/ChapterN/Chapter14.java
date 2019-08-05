package ChapterN;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

public class Chapter14 {

    public static DoubleUnaryOperator curriedConverter(double factor, double base) {
        /*
        * As a result, your code is more flexible and it reuses the existing conversion logic! Let’s reflect on
        * what you’re doing here. Instead of passing all the arguments x, f, and b all at once to the
        * converter method, you only ask for the arguments f and b and return another function, which
        * when given an argument x returns x * f + b. This enables you to reuse the conversion logic and
        * create different functions with different conversion factors.
        * */
        return operand -> operand * factor + base;  // Returns a function which takes double and returns a double
    }

    public static DoubleBinaryOperator curriedBinary(double f) {
        return (left, right) -> left*f + right*f;
    }

    public static void main(String[] args) {

//      You just give 2 changing arguments here, and the last, actual temp to convert in applyAsDouble
//      Currying is a technique where a function f of two arguments (x and y, say) is seen instead as a
//      function g of one argument that returns a function also of one argument.
        DoubleUnaryOperator convertCtoF = curriedConverter(9.0/5, 32);
        System.out.println(convertCtoF.applyAsDouble(40));


        DoubleBinaryOperator another = curriedBinary(5);
        System.out.println(another.applyAsDouble(10,20));
    }
}
