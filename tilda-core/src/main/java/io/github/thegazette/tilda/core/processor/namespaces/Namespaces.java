package io.github.thegazette.tilda.core.processor.namespaces;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.thegazette.tilda.core.api.API;
import io.github.thegazette.tilda.core.api.Configuration;
import io.github.thegazette.tilda.core.api.Endpoint;
import io.github.thegazette.tilda.core.util.Constants;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Sending namespaces as PREFIXes in the query (at least the construct query) doesn't seem
 * to result in namespaces being used in the resulting model
 *
 * So, we brute force namespaces back into the model by taking the namespaces
 * from the configuration ttl and then adding them into the model
 * if it looks like they've been used
 *
 * This function will take a model, look for configured used namespaces
 * and return a map containing those namespaces
 * (which can be added back to the model by the caller)
 *
 * TODO - Seeing as this is remarkably sub-optimal, look for a way
 * to get query results with pre-populated namespaces
 *
 */
public interface Namespaces extends Function<Model, Map<String, String>> {
    static Namespaces build(Configuration configuration, API api, Endpoint endpoint) {
        final var logger = LoggerFactory.getLogger(Namespaces.class);

        final var builtIns = ImmutableMap.<String, String>builder()
                .put("linked-data", Constants.LDA.NS)
                .put("foaf", Constants.FOAF.NS)
                .put("opensearch", Constants.OPENSEARCH.NS)
                .put("rdfs", Constants.RDFS.NS)
                .build();

        final var configured = configuration.vocabulary().namespaces()
                .entrySet()
                .stream()
                .filter(e -> !builtIns.containsKey(e.getKey()))
                .filter(e -> !builtIns.containsValue(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        final var namespaces = ImmutableMap.<String, String>builder()
                .putAll(builtIns)
                .putAll(configured)
                .build();

        final Function<Resource, String> namespacePortion = (r) -> {
            var s = r.stringValue();
            var ss = s.split("#");
            if (ss.length > 1)
                return ss[0] + "#";

            return s.substring(0, s.lastIndexOf("/") + 1);
        };

        return (model) -> {
            final var subjects = model.subjects().stream()
                    .filter(Resource::isIRI)
                    .map(namespacePortion)
                    .distinct()
                    .toList();
            final var predicates = model.predicates().stream()
                    .filter(Resource::isIRI)
                    .map(namespacePortion)
                    .distinct()
                    .toList();
            final var objects = model.objects().stream()
                    .filter(Value::isIRI)
                    .map(Value::stringValue)
                    .map(s -> s.split("#"))
                    .map(ss -> ss[0] + ((ss.length > 1) ? "#" : ""))
                    .distinct()
                    .toList();
          final var used = ImmutableList.<String>builder()
                    .addAll(subjects)
                    .addAll(predicates)
                    .addAll(objects)
                    .build()
                    .stream()
                    .distinct()
                    .toList();

          final Predicate<Map.Entry<String, String>> isUsed = e -> used.contains(e.getValue());
          return namespaces
                    .entrySet().stream()
                    .filter(isUsed)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        };
    }

}
