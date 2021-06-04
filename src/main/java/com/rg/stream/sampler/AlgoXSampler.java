package com.rg.stream.sampler;


import java.util.Random;

/**
 * Implementation of <i>Algorithm X</i> in <b>Random Sampling with a Reservoir</b>.
 * <p>
 *
 * algorithms decide how many items to skip, rather than deciding whether or not to skip an item each time it is fed.
 * This property allows these algorithms to perform better by efficiently calculating the number of items that need to
 * be skipped, while making fewer calls to the Random Number generator.
 * <p>
 * This implementation will not take more than {@link Long#MAX_VALUE} items if they are fed.
 * <p>
 * The space complexity of this class is {@code O(k)}, where {@code k} is the sample size.
 *
 * @param <T> the item type
 * @see <a href="https://en.wikipedia.org/wiki/Reservoir_sampling">Random Sampling with a Reservoir</a>
 */
public class AlgoXSampler<T> extends DefaultRandomSampler<T> {
    /**
     * Construct a new instance of {@link AlgoXSampler} using the specified sample size and random generator.
     * The implementation assumes that {@code random} conforms to the contract of {@link Random} and will
     * perform no checks to ensure that.
     *
     * If this contract is violated, the behavior is undefined.
     *
     * @param sampleSize the sample size
     * @param random     the RNG to use
     * @throws NullPointerException     if {@code random} is {@code null}
     * @throws IllegalArgumentException if {@code sampleSize} is less than 1
     */
    public AlgoXSampler(int sampleSize, Random random) {
        super(sampleSize, random);
    }

    /**
     * Construct a new instance of {@link AlgoXSampler} using the specified sample size and a default source of
     * randomness.
     *
     * @param sampleSize the sample size
     * @throws IllegalArgumentException if {@code sampleSize} is less than 1
     */
    public AlgoXSampler(int sampleSize) {
        this(sampleSize, new Random());
    }

    /**
     * Get a {@link RandomSampleCollector} from this class.
     *
     * @param sampleSize the sample size
     * @param random     the random to use
     * @param <E>        the type of elements
     * @return a {@link RandomSampleCollector} from this class
     */
    public static <E> RandomSampleCollector<E> collector(int sampleSize, Random random) {
        return new RandomSampleCollector<>(() -> new AlgoXSampler<>(sampleSize, random));
    }

    public static <E> RandomSampleCollector<E> collector(RandomSampler<E> sampling) {
        return new RandomSampleCollector<>(() -> sampling);
    }

    @Override
    long skipLength(long streamSize, int sampleSize, Random random) {
        streamSize++;

        final double r = random.nextDouble();
        long skipCount = 0;

        double quot = (streamSize - sampleSize) / (double) streamSize;
        while (quot > r && streamSize > 0) {
            skipCount++;
            streamSize++;
            quot = (quot * (streamSize - sampleSize)) / (double) streamSize;
        }

        return skipCount;
    }
}
