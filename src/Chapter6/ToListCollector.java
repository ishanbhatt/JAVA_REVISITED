package Chapter6;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {

    @Override
    public Supplier<List<T>> supplier() {
        System.out.println("SUPPLIER");
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<T>, T> accumulator() {
        System.out.println("ACCUMULATOR");
        return List::add;
    }

    @Override
    public Function<List<T>, List<T>> finisher() {
        System.out.println("FINISHER");  // This won't get called as characteristic is having IDENTITY
        return w->w;  // Function.identity() can also be used
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
        System.out.println("COMBINER");
        return (ts, ts2) -> {
            ts.addAll(ts2);
            return ts;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH, Characteristics.CONCURRENT));
    }
}
