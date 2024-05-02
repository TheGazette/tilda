package io.github.thegazette.tilda.web.support;

import java.util.Collections;
import java.util.List;

public interface Lists {
    static <T> List<T> eitherOr(List<T> l, List<T> r) {
        if (l != null && !l.isEmpty()) return l;
        if (r != null) return r;
        return Collections.emptyList();
    }
}
