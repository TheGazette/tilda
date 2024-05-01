package uk.co.tso.tilda.core.api;


import uk.co.tso.tilda.core.api.formatter.Formatter;
import uk.co.tso.tilda.core.api.viewer.Viewer;
import uk.co.tso.tilda.core.api.vocabulary.Vocabulary;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Configuration {
    /**
     * The API specifications appears to make no mention if there can be only one
     * "api" defined or not. As I don't see why you might not want a single
     * API server sitting in front of multiple sparql endpoints, we're going
     * to allow it, even if it does complicate HandlerFunction generation a bit
     * @return
     */
    Map<String, API> apis();
    Map<String, Endpoint> endpoints();
    List<Formatter> formatters();
    Map<String, Selector> selectors();
    List<Viewer> viewers();
    Vocabulary vocabulary();


    default Optional<Selector> selector(String selector) {
        return Optional.ofNullable(selectors().get(selector));
    }
    default Optional<Viewer> viewer(String name) {
        return viewers().stream().filter(v -> v.matchIRIOrName(name)).findFirst();
    }
}
