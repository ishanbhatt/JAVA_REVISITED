package Chapter7;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ParrallelSeqStreams {

    public static long sequentialSum(long n) {
        return Stream.iterate(1L, i -> i +1)
                .limit(n)
                .reduce(0L, Long::sum);

    }

    public static long rangedSeqSum(long n) {
        return LongStream.rangeClosed(1, n)
                .reduce(0L, Long::sum);
    }

    public static long parallelSum(long n) {
        return Stream.iterate(1L, i -> i +1)
                .limit(n)
                .parallel()
                .reduce(0L, Long::sum);
    }

    public static long rangedParSum(long n) {
        return LongStream.rangeClosed(0,n)
                .parallel()
                .reduce(0L, Long::sum);
    }

    public static long iterSum(long n) {
        long sum = 0;
        for (long i=1L; i<=n; i++)
        {
            sum += i;
        }
        return sum;
    }

    public static long measureSumPerf(Function<Long, Long> adder, long n) {
        long fastest = Long.MAX_VALUE;
        for (int i =0; i<10; i++)
        {
            long start = System.nanoTime();
            long sum = adder.apply(n);
            long duration = (System.nanoTime() - start) / 1_000_000;;
            if (duration < fastest) fastest=duration;

        }
        return fastest;
    }

    public static long sideEffecSum(long n) {
//        This is HIGHLY wrong code never use it
//        Issue is the parallel operation being performed MUTATES total variable
//        This is not side effect free, such things should be done in sync block
//        If you do it in sync block all benefits of parallel are lost
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1,n).parallel().forEach(accumulator::add);
        return accumulator.total;
    }

    /*
    * Here, the performance is worse than the version using the parallel stream, but only because
    you’re obliged to put the whole stream of numbers into a long[] before being allowed to use it in
    the ForkJoinSumCalculator task.
    * */
    public static long forkJoinSum(long n) {
        long[] numbers = LongStream.rangeClosed(1,n).toArray();
        ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
        return new ForkJoinPool().invoke(task);
    }

    public static void main(String[] args) {
//        DO not combine .sequential(), .parallel() in one operation
//        the last one wins and that would be the nature of the operation
        long iterTime = measureSumPerf(ParrallelSeqStreams::iterSum, 10_000_000);
        long seqTime = measureSumPerf(ParrallelSeqStreams::sequentialSum, 10_000_000);

//        Using iterate in parallelSum method is highly disappointing
//        As, iterate needs previous value to be computed before it generates next value.
//        Thus, the whole stream is not available beforehand making it hard to parallelize
        long parTime = measureSumPerf(ParrallelSeqStreams::parallelSum, 10_000_000);

        long rangeSeqTime = measureSumPerf(ParrallelSeqStreams::rangedSeqSum, 10_000_000);
        long rangeParTime = measureSumPerf(ParrallelSeqStreams::rangedParSum, 10_000_000);

        System.out.println("ITER "+iterTime);
        System.out.println("SEQ "+seqTime);
        System.out.println("PAR "+parTime);

        System.out.println("RANGE SEQ "+rangeSeqTime);
        System.out.println("RANGE PAR "+rangeParTime);

        System.out.println(sideEffecSum(10_000));

        System.out.println("--------------------------FORK JOIN SUM-------------------------------");
        System.out.println("USING FORK JOIN "+ measureSumPerf(ParrallelSeqStreams::forkJoinSum, 10_000_000));

    }
}
