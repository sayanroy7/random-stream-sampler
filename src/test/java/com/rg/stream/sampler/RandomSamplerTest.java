package com.rg.stream.sampler;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


public class RandomSamplerTest {

    private static final Random RANDOM = new Random();

    private static final int SAMPLE = 10;

    private RandomSampler<Integer> randomSampler;

    @BeforeEach
    public void init() {
        if (randomSampler == null) {
            randomSampler = new AlgoXSampler<>(SAMPLE, RANDOM);
        }
    }

    @Test
    public void streamFeedTest() {
        final int STREAM = 20;
        final RandomSampleCollector<Integer> collector = AlgoXSampler.collector(SAMPLE, RANDOM);
        final Collection<Integer> sample = IntStream.range(0, STREAM).boxed().collect(collector);
        assertFalse(sample.isEmpty());
        System.out.println(sample.toString());
    }

    /**
     * Equivalence between {@link RandomSampler#feed(Object)}, {@link RandomSampler#feed(Iterator)} and
     * {@link RandomSampler#feed(Iterable)}.
     */
    @Test
    public void feedAlternativeTest() {
        final RandomSampler<Integer> rs1 = new AlgoXSampler<>(SAMPLE, RANDOM);
        final RandomSampler<Integer> rs2 = new AlgoXSampler<>(SAMPLE, RANDOM);
        final RandomSampler<Integer> rs3 = new AlgoXSampler<>(SAMPLE, RANDOM);
        final Set<Integer> set = new HashSet<>();
        for (int i = 0; i < SAMPLE; i++) {
            set.add(i);
            rs1.feed(i);
        }
        rs2.feed(set.iterator());
        rs3.feed(set);
        assertTrue(RandomTestUtils.samplesEquals(rs1.sample(), rs2.sample()));
        assertTrue(RandomTestUtils.samplesEquals(rs2.sample(), rs3.sample()));
        assertEquals(rs1.streamSize(), rs2.streamSize());
        assertEquals(rs2.streamSize(), rs3.streamSize());
        assertEquals(rs1.sample().size(), rs2.sample().size());
        assertEquals(rs2.sample().size(), rs3.sample().size());
    }

    /**
     * {@link RandomSampler#streamSize()} correctness.
     */
    @Test
    public void streamSizeTest() {
        final int size = 1024;
        final RandomSampler<Integer> rs = randomSampler;
        for (int i = 0; i < size; i++) {
            rs.feed(0);
        }
        assertEquals(size, rs.streamSize());
    }

    /**
     * The first elements must go directly in the sample.
     */
    @Test
    public void firstElementsTest() {
        final RandomSampler<Integer> rs = randomSampler;
        final Set<Integer> feeded = new HashSet<>();
        for (int i = 0; i < rs.sampleSize(); i++) {
            rs.feed(i);
            feeded.add(i);
            assertTrue(RandomTestUtils.samplesEquals(rs.sample(), feeded));
        }
    }

    /**
     * The {@link RandomSampler#sample()} method must always return the same reference.
     */
    @Test
    public void sampleOnDifferentTimeTest() {
        final RandomSampler<Integer> rs = randomSampler;
        final Collection<Integer> sample = rs.sample();
        for (int i = 0; i < 1000; i++) {
            rs.feed(i);
           assertSame(sample, rs.sample());
        }
    }

    /**
     * If {@link RandomSampler#feed(Object)} returned {@code true}, than the sample has definitely changed, assuming
     * unique stream elements. Furthermore, the new sample has to contain the new element.
     */
    @Test
    public void feedReturnValueTest() {
        final RandomSampler<Integer> rs = randomSampler;
        Collection<Integer> sample = new ArrayList<>();
        for (int i = 0; i < 65536; i++) {
            final boolean changed = rs.feed(i);
            assertEquals(changed, !RandomTestUtils.samplesEquals(sample, rs.sample()));
            assertEquals(changed, rs.sample().contains(i));
            sample = new ArrayList<>(rs.sample());
        }
    }

}
