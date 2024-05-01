package uk.co.tso.tilda.core.util;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public interface Optionals {
    /**
     * Returns l if l is present, otherwise r (whether it is present or not)
     * @param l
     * @param r
     * @return
     * @param <T>
     */
    static <T> Optional<T> eitherOr(Optional<T> l, Optional<T> r) {
        return l.isPresent() ? l : r;
    }


    @SafeVarargs
    static <T> Optional<T> either(Optional<T>... options) {
        return Arrays.stream(options).filter(Optional::isPresent).findFirst().orElse(Optional.empty());
    }

    static Optional<String> ofString(String s) {
        return s == null || s.isEmpty() ? Optional.empty() : Optional.of(s);
    }

    static Optional<Integer> intFromString(String s) {
        if (s != null && !s.isEmpty()) {
            try {
                return Optional.of(Integer.parseInt(s));
            } catch (Exception ignored) {

            }
        }
        return Optional.empty();
    }

    static Optional<Integer> intFromString(String s, Predicate<Integer> guard) {
        return intFromString(s).filter(guard);
    }
}
