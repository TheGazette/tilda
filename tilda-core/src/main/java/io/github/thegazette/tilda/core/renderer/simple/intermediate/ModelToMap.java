package io.github.thegazette.tilda.core.renderer.simple.intermediate;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.eclipse.rdf4j.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.thegazette.tilda.core.api.vocabulary.Vocabulary;
import io.github.thegazette.tilda.core.api.vocabulary.property.Property;
import io.github.thegazette.tilda.core.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
public class ModelToMap {
    private final Logger logger = LoggerFactory.getLogger(ModelToMap.class);
    private final Model model;
    private final Vocabulary vocabulary;
    private final List<String> visited;
    private int bNodeCount = 1;

    private ModelToMap(Model model,  Vocabulary vocabulary) {
        this.model = model;
        this.visited = Lists.newArrayListWithExpectedSize(model.subjects().size());
        this.vocabulary = vocabulary;
    }

    public static ModelToMap with(Model model, Vocabulary vocabulary) {
        return new ModelToMap(model, vocabulary);
    }



    public Map<String, Object> apply(final Resource subject) {
        if (visited.contains(subject.stringValue()))
            return Collections.emptyMap();

        visited.add(subject.stringValue());

        final var forSubject = model.filter(subject, null, null);
        if (forSubject.size() == 0)
            return Collections.emptyMap();

        final var b = ImmutableMap.<String, Object>builder();
        if (subject.isBNode()) {
            var bnode = (BNode) subject;
            b.put("_id", bnode.getID());
        } else {
            b.put("_about", subject.stringValue());
        }

        final var predicates = forSubject.predicates();

        for (var predicate : predicates) {
            //var label = antiVocabulary.getOrDefault(predicate.stringValue(), predicate.getLocalName());
            var label = vocabulary
                    .findPropertyByValue(predicate.stringValue())
                    .map(Property::label)
                    .orElse(predicate.getLocalName());

            final var forSubjectAndPredicate = model.filter(subject, predicate, null);
            if (forSubjectAndPredicate.size() > 1) {
                var values = ImmutableList.builder();

                for (var statement : forSubjectAndPredicate) {
                    var object = value(statement);
                    values.add(object);
                }

                b.put(label, values.build());
            } else {
                forSubjectAndPredicate.stream().findFirst().ifPresent(statement -> {
                    b.put(label, value(statement));
                });
            }
        }


        return b.build();
    }


    private Object value(Statement statement) {
        var value = statement.getObject();

        if (value.isLiteral()) {
            var literal = (Literal) value;
            var label = literal.getLabel();
            var dataType = literal.getDatatype().getLocalName();

            var formattedDate = "";
            switch (dataType) {
                case "date" -> {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        var iDate = inputFormat.parse(label);
                        formattedDate = outputFormat.format(iDate);
                        return formattedDate;
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "dateTime" -> {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    try {
                        var iDate = inputFormat.parse(label);
                        formattedDate = outputFormat.format(iDate);
                        return formattedDate;
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                default -> {
                    return label;
                }
            }
        }

        if (value.isResource()) {
            var resource = (Resource) value;
            if (isList(resource))
                return list(resource);

            var object = apply(resource);
            if (object != null && !object.isEmpty())
                return object;
            return value.stringValue();
        }

        return Collections.<String, Object>emptyMap();

    }

    private Object list(Resource resource) {
        var builder = ImmutableList.<Map<String, Object>>builder();
        var rest = resource;
        do {
            var first = model
                    .filter(rest, Constants.RDF_FIRST, null)
                    .objects()
                    .stream()
                    .findFirst()
                    .filter(Value::isResource)
                    .map(v -> (Resource) v)
                    .orElseThrow();

            builder.add(apply(first));

            rest = model
                    .filter(rest, Constants.RDF_REST, null)
                    .objects()
                    .stream()
                    .filter(Value::isResource)
                    .map(v -> (Resource) v)
                    .findFirst()
                    .orElseThrow();
        } while (!Constants.RDF_NIL.equals(rest));
        return builder.build();
    }

    private boolean isList(Resource resource) {
        return rdfType(resource).filter(Constants.RDF_LIST::equals).isPresent();
    }

    private Optional<IRI> rdfType(Resource value) {
        return model
                .filter(value, Constants.RDF_TYPE, null)
                .objects()
                .stream()
                .filter(Value::isIRI)
                .map(v -> (IRI) v)
                .findFirst();
    }
}
