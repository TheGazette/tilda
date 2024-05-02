package io.github.thegazette.tilda.core.renderer.simple.intermediate;

import com.google.common.collect.ImmutableMap;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import io.github.thegazette.tilda.core.api.vocabulary.Vocabulary;

import java.util.Map;
import java.util.function.BiFunction;

public interface ModelToResults extends BiFunction<Model, Resource, Map<String, Object>> {
    static ModelToResults build(final Vocabulary vocabulary) {
        return (model, resource) -> ImmutableMap.<String, Object>builder()
                .put("format", "linked-data-api")
                .put("version", "0.2")
                .put("results", ModelToMap.with(model, vocabulary).apply(resource))
                .build();
    }
}
