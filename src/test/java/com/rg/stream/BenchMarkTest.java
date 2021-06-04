package com.rg.stream;

import com.rg.stream.sampler.AlgoXSampler;
import com.rg.stream.sampler.RandomSampleCollector;
import com.rg.stream.sampler.RandomSampler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class BenchMarkTest {

    private static final Random RANDOM = new Random();

    private static final int SAMPLE = 10;

    private RandomSampler<Character> randomSampler;

    @BeforeEach
    public void init() {
        if (randomSampler == null) {
            randomSampler = new AlgoXSampler<>(SAMPLE, RANDOM);
        }
    }

    @Test
    public void benchmarkTest() {
        final int STREAM = 100000000;
        final long start = System.nanoTime();
        final RandomSampleCollector<Integer> collector = AlgoXSampler.collector(SAMPLE, RANDOM);
        final Collection<Integer> sample = IntStream.range(0, STREAM).boxed().collect(collector);
        final long end = System.nanoTime() - start;
        assertFalse(sample.isEmpty());
        System.out.println("Sample collected: " + sample.toString());
        System.out.println("Total time elapsed: " + end);
    }


}
