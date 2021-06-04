package com.rg.stream.sampler;



import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * A reduction operation that accumulates input elements into a {@link RandomSampler}, optionally transforming the
 * accumulated result into a final sample {@link Collection} after all input elements have been processed.
 *
 * @param <E> the type of input elements
 */
public class RandomSampleCollector<E>
        implements Collector<E, RandomSampler<E>, Collection<E>> {
    private final Supplier<RandomSampler<E>> supplier;

    /**
     * Construct a new {@link RandomSampleCollector} from the given supplier.
     *
     * @param supplier the {@link Supplier} of {@link RandomSampler} to use
     * @throws NullPointerException if {@code supplier} is {@code null}
     */
    public RandomSampleCollector(Supplier<RandomSampler<E>> supplier) {
        if (supplier == null) {
            throw new NullPointerException();
        }
        this.supplier = supplier;
    }

    /**
     * A function that creates and returns a new {@link RandomSampler}.
     *
     * @return a function which returns a new {@link RandomSampler}
     */
    @Override
    public Supplier<RandomSampler<E>> supplier() {
        return supplier;
    }

    /**
     * A function that folds a value into a {@link RandomSampler}.
     * <p>
     * The function returned by this method invokes the method {@link RandomSampler#feed(Object)}.
     *
     * @return a function which folds a value into a {@link RandomSampler}
     */
    @Override
    public BiConsumer<RandomSampler<E>, E> accumulator() {
        return RandomSampler::feed;
    }

    /**
     * A function that accepts two results of type {@link RandomSampler} and merges them.
     * <p>
     * This operation is not supported by this implementation because it is not a meaningful operation to combine two
     * instances of type {@link RandomSampler} and only sequential stream processing is supported.
     *
     * Thus, this method returns a {@link BinaryOperator} that always throws
     * {@link UnsupportedOperationException}.
     *
     * @return a function which throws {@link UnsupportedOperationException}
     */
    @Override
    public BinaryOperator<RandomSampler<E>> combiner() {
        return (a, b) -> {
            throw new UnsupportedOperationException();
        };
    }

    /**
     * Perform the final transformation from the {@link RandomSampler} accumulation type to a {@link Collection} that
     * holds the sample.
     * <p>
     * This transformation invokes the method {@link RandomSampler#sample()}.
     *
     * @return a function which transforms the {@link RandomSampler} to the sample {@link Collection}
     */
    @Override
    public Function<RandomSampler<E>, Collection<E>> finisher() {
        return rs -> new ArrayList<>(rs.sample());
    }

    /**
     * Returns a {@link Set} of {@link Characteristics} indicating the characteristics of this
     * {@code Collector}.
     * <p>
     * More specifically, returns only the characteristic {@link Characteristics#UNORDERED}.
     *
     * @return an immutable set of collector characteristics
     */
    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}
