package Chapter8;

interface ValidateStrategy {
    boolean execute(String s);
}

class IsAllLower implements ValidateStrategy {
    @Override
    public boolean execute(String s) {
        return s.matches("[a-z]+");
    }
}

class Validator {
    private final ValidateStrategy strategy;  // This comes at runtime

    public Validator(ValidateStrategy v) {
        this.strategy = v;
    }

    public boolean validate(String s) {
        return strategy.execute(s);
    }
}


public class StrategyDP {

    public static void main(String[] args) {

        Validator allLowerValidator = new Validator(new IsAllLower());
        System.out.println(allLowerValidator.validate("aaaa"));

        /*
        * lambda expression here is the functional interface so it's an anon class with the execute method defined as an expression
        * lambda expressions encapsulate a piece of code (or strategy),
        * which is what the strategy design pattern was created for
        * */
        Validator allNumValidator = new Validator(s -> s.matches("\\d+"));
        System.out.println(allNumValidator.validate("123"));

    }


}
