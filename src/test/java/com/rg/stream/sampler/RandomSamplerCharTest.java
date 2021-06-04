package com.rg.stream.sampler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

public class RandomSamplerCharTest {

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
    public void finiteStreamOfCharsTest() {
        String charSequence = "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG";
        var chars = charSequence.chars().mapToObj(c -> (char)c)
                .collect(AlgoXSampler.collector(randomSampler));
        assertFalse(chars.isEmpty());
        System.out.println(chars.toString());
    }

    @Test
    public void finiteStreamHigherSamplerTest() {
        String charSequence = "THEQUICK";
        var chars = charSequence.chars().mapToObj(c -> (char)c)
                .collect(AlgoXSampler.collector(randomSampler));
        assertFalse(chars.isEmpty());
        System.out.println(chars.toString());
        assertEquals(charSequence, chars.stream().map(String::valueOf)
                .collect(Collectors.joining()));
    }

    @Test
    public void skipLength() {
        final int streamSize = 150;
        final int sampleSize = 2;
        final AlgoXSampler<Character> sampler = new AlgoXSampler<>(sampleSize, RANDOM);
        long skipLength = sampler.skipLength(streamSize, sampleSize, RANDOM);
        assertTrue(skipLength < streamSize);
    }
}