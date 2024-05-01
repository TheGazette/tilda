package uk.co.tso.tilda.web.support;

import com.github.jsonldjava.shaded.com.google.common.collect.ImmutableList;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import uk.co.tso.tilda.core.api.vocabulary.property.ApiProperty;
import uk.co.tso.tilda.core.api.vocabulary.property.Property;
import uk.co.tso.tilda.core.api.vocabulary.property.RdfTypeProperty;
import uk.co.tso.tilda.core.util.Constants;
import uk.co.tso.tilda.web.config.properties.TildaProperties;

import java.util.List;
import java.util.Optional;


public interface Properties {
    static List<Property> property(final Model model, final Resource subject, final IRI predicate) {
        final var value = subject.stringValue();
        final var labels = model
                .filter(subject, predicate, null)
                .objects()
                .stream()
                .map(Value::stringValue)
                .toList();

        if (Constants.RDF.TYPE.equals(value))
            return labels.stream().map(RdfTypeProperty::from).toList();

        final var range = model
                .filter(subject, Constants.RDFS_RANGE, null)
                .objects()
                .stream()
                .map(Value::stringValue)
                .findFirst();

        final var iri = (value.startsWith("<") ? "" : "<") + value + (value.endsWith(">") ? "" : ">");
        return labels.stream().map(label -> ApiProperty.from(value, label, iri, range)).toList();
    }
    static List<Property> properties(final TildaProperties tildaProperties, final Model model) {
        /*
          It looks like anything with a "label" is a property, and there are two kinds of
          labels - API and RDFS (API takes precedence)

          I'm leaving aside defining properties in TildaProperties(aka, YAML) for now.
        */

        final var api = model.filter(null, Constants.API_LABEL, null)
                .subjects()
                .stream()
                .map(r -> property(model, r, Constants.API_LABEL))
                .flatMap(List::stream)
                .toList();

        final var apiLabels = api.stream().map(Property::label).toList();

        final var rdfs =
                model.filter(null, Constants.RDFS_LABEL, null)
                        .subjects()
                        .stream()
                        .map(r -> property(model, r, Constants.RDFS_LABEL))
                        .flatMap(List::stream)
                        .filter(p -> !apiLabels.contains(p.label()))
                        .toList();

        return ImmutableList.<Property>builder().addAll(api).addAll(rdfs).build();
    }
}
