package com.rg.stream;

import com.rg.stream.sampler.AlgoXSampler;
import com.rg.stream.sampler.RandomSampler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamSamplerApplication {

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No sampling size");
        }

        final var samplingSize = Integer.parseInt(args[0]);
        try (final Reader r = new InputStreamReader(System.in, StandardCharsets.UTF_8);
             final BufferedReader br = new BufferedReader(r);
             final Stream<String> lines = br.lines()) {
            final RandomSampler<Character> rs = new AlgoXSampler<>(samplingSize, new Random());
            var chars = lines
                    //.peek(System.out::println)
                    .takeWhile(ll -> rs.streamSize() == 0 || samplingSize - rs.streamSize() > 0)
                    .flatMap(l -> IntStream.range(0, l.length()).mapToObj(l::charAt))
                    .limit(rs.streamSize() < Long.MAX_VALUE ?
                            Long.MAX_VALUE - rs.streamSize() : Long.MAX_VALUE)
                    .collect(AlgoXSampler.collector(rs));

            System.out.println("Random Sample : "+ chars.stream().map(String::valueOf)
                    .collect(Collectors.joining()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
