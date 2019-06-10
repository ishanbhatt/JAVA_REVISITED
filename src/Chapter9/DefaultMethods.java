package Chapter9;

import java.util.Optional;

interface A {
    default void hello() {
        System.out.println("Hello From A");
    }
}

interface B extends A {

    @Override
    default void hello() {
        System.out.println("Hello From B");
    }
}

class D implements A { }


public class DefaultMethods implements B, A {

    public static void main(String[] args) {
        new DefaultMethods().hello();  // B is more specific so it gets printed
    }
}

/*
* If we had public class DefaultMethods extends D implements B, A
* Then again Hello From B would be printed. As , B is more specific, Even though rule 1 applies,
* If D had implemented hello, then Hello from D would come
*
* GOTCHA
* public abstract class D implements A {
    public abstract void hello();
  }
  In this case, because of Rule 1 (class wins), D's hello would be called but as it is abstract
  DefaultMethods will have to give implementation hello()
* */