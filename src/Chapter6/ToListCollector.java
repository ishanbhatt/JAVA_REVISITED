package Chapter6;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

// If we remove type information from here , the code using this collector breaks as it can't infer the types
// As it will use original Collector's <T,A,R> which arent bound to anything
public class ToListCollector<T> implements Collector <T, List<T>, List<T>>{

    @Override
    public Supplier<List<T>> supplier() {
//        Creates an empty accumulator, which will be used further down
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<T>, T> accumulator() {
//        accumulates the results into accumulator returned by supplier, returns void
        return List::add;
    }

    @Override
    public Function<List<T>, List<T>> finisher() {
//        Function to be called after the end of accumulation after completely traversing the stream
//         This won't get called as characteristic is having IDENTITY
        return w->w;  // Function.identity() can also be used
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
//        THis would not have been called of CONCURRENT characteristics wasn't set
//        To combine results of the multiple streams
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
