package com.rg.stream;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StreamSamplerApplicationTest {

    @Test
    public void mainNoSampleSize() throws IOException {
        final InputStream original = System.in;
        try (final InputStream is = Files.newInputStream(Paths.get("src/test/resources/commands/sample1.txt"))) {
            System.setIn(is);
            IllegalArgumentException err = assertThrows(IllegalArgumentException.class,
                    () -> StreamSamplerApplication.main(new String[]{}));
            assertEquals("No sampling size", err.getMessage());
        }
        System.setIn(original);
    }

    @Test
    public void mainSearch() throws IOException {
        final InputStream original = System.in;
        try (final InputStream is = Files.newInputStream(Paths.get("src/test/resources/commands/sample1.txt"))) {
            System.setIn(is);
            StreamSamplerApplication.main(new String[]{"5"});
        }
        System.setIn(original);
    }

    @Test
    public void mainSearchStreamPiped() throws IOException {
        final InputStream original = System.in;
        try (final InputStream is = Files.newInputStream(Paths.get("src/test/resources/commands/sample2.txt"))) {
            System.setIn(is);
            StreamSamplerApplication.main(new String[]{"50"});
        }
        System.setIn(original);
    }
}