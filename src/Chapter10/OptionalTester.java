package Chapter10;

import java.util.Optional;

public class OptionalTester {

    public static String getCarInsuranceName(Optional<Person> person) {
        return person.flatMap(Person::getCar)
                .flatMap(Car::getInsurance)
                .map(Insurance::getName)
                .orElse("Unknown");
//      Hopefully you understand why the first two were flatMap and the last one is map
//      person is an optional, so calling a map(Person::getCar) on person will return Optional<Optional<Car>>
//      On which you can not call any function, and it would get worse in the chain Optional<Optional<Optional>>
//      So, like you did on the streams, you need to flatMap it to get Optional<Car> on which you can call getInsurance
    }

    public static void main(String[] args) {

        Insurance iffco = new Insurance();
        iffco.setName("IFFCO TOKIO");

        Insurance argo = new Insurance();
        argo.setName("HDFC ARGO");

        Car i10 = new Car();
        i10.setInsurance(iffco);

        Car santro = new Car();

        Person p = new Person();
        p.setCar(santro);

        Person p1 = new Person();
        p1.setCar(i10);

        System.out.println("---------------FROM PERSON GET INSURANCE----------------------");

//        System.out.println(getCarInsuranceName(Optional.of(p1)));
        System.out.println(getCarInsuranceName(Optional.of(p)));

        System.out.println(Optional.ofNullable(null));


    }
}
