package com.rg.stream.sampler;



import java.util.Collection;
import java.util.Iterator;

/**
 * Represents a reservoir sampling algorithm.
 * <p>
 * A reservoir sampling algorithm randomly chooses a sample of {@code k} items from a list {@code S} containing
 * {@code n} items, where {@code n} may be unknown.
 * <p>
 * All classes implementing {@code RandomSampling} have equivalent behavior and are differentiated only on their
 * performance characteristics.
 * <p>
 * A {@code RandomSampling} algorithm does not keep track of duplicate elements because that would result in a linear
 * memory complexity. Thus, it is valid to feed the same element multiple times in the same instance. For example it is
 * possible to feed both {@code x} and {@code y}, where {@code x.equals(y)}. The algorithm will treat these items as
 * distinct, even if they are reference-equals ({@code x == y}). As a result, the final sample {@link Collection} may
 * contain duplicate elements. Furthermore, elements need not be immutable and the sampling process does not rely on the
 * elements' {@code hashCode()} and {@code equals()} methods.
 * <p>
 * Implementations need to manage the internal sampling buffer to finite set for calculating it efficiently,
 * which is typically related to the stream size. The default implementation uses a hard limit of {@code Long.MAX_VALUE}
 * for the buffer size to feed the source.
 * <p>
 * Classes that implement this interface have a static method with signature
 * <pre><code>
 * public static &lt;E&gt; RandomSamplingCollector&lt;E&gt; collector(RandomSampling<E> randomSampling)
 * </code></pre>
 * that returns a {@link RandomSampleCollector} to use with the Java stream API.
 * <p>
 * Implementations of this interface have constant space complexity in respect to the stream size.
 *
 * @param <T> the item type
 */
public interface RandomSampler<T> {
    /**
     * Feed an item from the stream to the algorithm.
     *
     * @param item the item to feed to the algorithm
     * @return {@code true} if the sample was modified as a result of this operation
     * @throws NullPointerException    if {@code item} is {@code null}
     */
    boolean feed(T item);

    /**
     * Feed an {@link Iterator} of items of type {@code T} to the algorithm.
     * <p>
     * This method is equivalent to invoking the method {@link #feed(Object)} for each item in {@code items}:
     * <pre><code>
     * while (items.hasNext()) {
     *     feed(items.next());
     * }
     * </code></pre>
     * <p>
     * After this method returns, the {@code items} iterator is left exhausted.
     *
     * @param items the items to feed to the algorithm
     * @return {@code true} if the sample was modified as a result of this operation
     * @throws NullPointerException    if {@code items} is {@code null} or any item in {@code items} is {@code null}
     */
    default boolean feed(Iterator<T> items) {
        boolean r = false;
        while (items.hasNext()) {
            r = feed(items.next()) || r;
        }
        return r;
    }

    /**
     * Feed an {@link Iterable} of items of type {@code T} to the algorithm.
     * <p>
     * This method is equivalent to invoking the method {@link #feed(Object)} for each item in {@code items}:
     * <pre><code>
     * for (T item : items) {
     *     feed(item);
     * }
     * </code></pre>
     *
     * @param items the items to feed to the algorithm
     * @return {@code true} if the sample was modified as a result of this operation
     * @throws NullPointerException    if {@code items} is {@code null} or any item in {@code items} is {@code null}
     */
    default boolean feed(Iterable<T> items) {
        boolean r = false;
        for (T item : items) {
            r = feed(item) || r;
        }
        return r;
    }

    /**
     * Get the expected reservoir size that this algorithm was created with.
     * <p>
     * The size is an {@code int} greater than zero that represents the number of items that the algorithm will try to
     * select from the source stream. The sample size remains constant during the lifetime of the instance.
     * <p>
     * The value of {@code sampleSize()} may not necessarily reflect the size of the collection returned by
     * {@link #sample()}, that is {@code sampleSize()} and {@code sample().size()} may not be equal. This occurs when
     * the items processed from the stream are less than {@code sampleSize()}, in which case there weren't enough items
     * to fill the reservoir yet. More specifically, it holds that
     * {@code sample().size() == min(sampleSize(), streamSize())} assuming that {@code streamSize()} does not overflow.
     * <p>
     * This method runs in constant time.
     *
     * @return the expected sample size
     */
    int sampleSize();

    /**
     * Get the number of items that have been fed to the algorithm during the lifetime of this instance.
     * <p>
     * If more than {@link Long#MAX_VALUE} items has been fed to the instance, this method may return
     * {@link Long#MAX_VALUE} or any other undefined {@link Long} value.
     * <p>
     * This method runs in constant time.
     *
     * @return the number of items that have been fed to the algorithm
     */
    long streamSize();

    /**
     * Get the sample of all the items that have been fed to the algorithm during the lifetime of the instance.
     * <p>
     * This method returns a readonly {@link Collection} view of the items in the sample which is backed by the
     * instance; subsequent modification of the instance (using any of the {@code feed} methods) will reflect on this
     * collection.
     *
     * The items returned are in no particular order inside the sample collection unless otherwise specified.
     * <p>
     * The {@link Collection} returned cannot be {@code null} but it can be empty if and only if no items have been
     * fed to the implementation. The {@link Collection} may also contain duplicate elements if an object has been fed
     * multiple times. It, furthermore, holds that {@code sample().size() == min(sampleSize(), streamSize())}, assuming
     * that {@code streamSize()} does not overflow.
     * <p>
     *
     * This method runs in constant time.
     *
     * @return the sample of the items that have been fed to this instance
     */
    Collection<T> sample();
}
