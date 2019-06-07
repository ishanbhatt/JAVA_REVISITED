package Chapter8;

import java.util.function.Consumer;


class OnlineBanking {

    public void processCustomer(int id, Consumer<Integer> makeCustomerHappy) {
        int fakeId = id +50;
        makeCustomerHappy.accept(fakeId);
    }

//    abstract void makeCustomerHappy(int id);
}

public class TemplateDP {

//    We have given the template of processing a customer. But we want different impl of makeCustomerHappy
//    Here we want to supply different implementations of makeCustomerHappy
//    In , pre JAVA8 world, you need to create different anon class/actual class for different makeCustomerHappy
//    Previously there was another abstract method makeCustomerHappy, see commented part.

    public static void main(String[] args) {
        new OnlineBanking().processCustomer(35, integer -> System.out.println(integer));
        new OnlineBanking().processCustomer(35, integer -> {integer+=30;
            System.out.println(integer);});
    }


}
