package uk.co.tso.tilda.core.util;

import java.util.Map;
import java.util.function.BiConsumer;

public interface SPARQL {
    static String prefixes(final Map<String, String> namespaces) {
        final var sb = new StringBuilder();
        final BiConsumer<String, String> prefix = (k, v) -> sb.append("PREFIX ").append(k).append(": <").append(v).append(">\n");

        namespaces.forEach(prefix);

        return sb.toString();
    }

}
