package com.rg.stream.sampler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RandomTestUtils {

    public static <E> boolean samplesEquals(Collection<E> a, Collection<E> b) {
        if (a.size() != b.size()) {
            return false;
        }
        if (a == b) {
            return true;
        }
        final Map<E, Integer> multiA = new HashMap<>();
        final Map<E, Integer> multiB = new HashMap<>();
        for (E e : a) {
            multiA.merge(e, 1, Integer::sum);
        }
        for (E e : b) {
            multiB.merge(e, 1, Integer::sum);
        }
        return multiA.equals(multiB);
    }

}
