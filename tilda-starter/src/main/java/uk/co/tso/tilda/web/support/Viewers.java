package uk.co.tso.tilda.web.support;

import com.google.common.collect.ImmutableList;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import uk.co.tso.tilda.core.api.viewer.*;
import uk.co.tso.tilda.core.util.Constants;
import uk.co.tso.tilda.core.util.Optionals;
import uk.co.tso.tilda.web.config.properties.TildaProperties;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static uk.co.tso.tilda.web.support.Common.name;

public interface Viewers {

    static Optional<String> template(Model model, Resource subject) {
        return model.filter(subject, Constants.TEMPLATE, null).stream()
                .findFirst().map(Statement::getObject).map(Value::stringValue);
    }


    static List<String> graphTemplates(Model model, Resource subject) {
        return model.filter(subject, Constants.GRAPH_TEMPLATE, null).objects().stream()
                .filter(Value::isResource)
                .map(v -> (Resource) v)
                .map(Resource::stringValue)
                .toList();
    }

    static List<String> property(Model model, Resource subject) {
        return model.filter(subject, Constants.PROPERTY, null).objects().stream()
                .filter(Value::isResource)
                .map(v -> (Resource) v)
                .map(Resource::stringValue)
                .toList();
    }

    static Optional<String> propertiesForSubject(Model model, Resource subject) {
        return model.filter(subject, Constants.PROPERTIES, null).stream()
                .findFirst().map(Statement::getObject).map(Value::stringValue);
    }

    static Optional<String> additionalTriples(Model model, Resource subject) {
        return model.filter(subject, Constants.TILDA_ADDITIONAL_TRIPLES, null).stream()
                .findFirst().map(Statement::getObject).map(Value::stringValue);
    }

    static Optional<String> unionTemplate(Model model, Resource subject) {
        return model.filter(subject, Constants.TILDA_UNION_TEMPLATE, null).stream()
                .findFirst().map(Statement::getObject).map(Value::stringValue);
    }

    static Optional<String> includeDatasetClause(Model model, Resource subject) {
        return model.filter(subject, Constants.TILDA_INCLUDE_DATASET_CLAUSE, null).stream()
                .findFirst().map(Statement::getObject).map(Value::stringValue);
    }


    static Optional<String> includeNamespaces(Model model, Resource subject) {
        return model.filter(subject, Constants.TILDA_INCLUDE_NAMESPACES, null).stream()
                .findFirst().map(Statement::getObject).map(Value::stringValue);
    }


    static List<Resource> includeForSubject(Model model, Resource subject) {
        return model.filter(subject, Constants.INCLUDE, null).objects().stream()
                .filter(Value::isResource)
                .map(v -> (Resource) v)
                .toList();

    }
    static Viewer viewer(final TildaProperties tildaProperties, final Model model, final Resource iri) {
        var name = name(model, iri);
        var properties = tildaProperties.viewer(name);

        var template = Optionals.eitherOr(properties.findTemplate(), template(model, iri));
        var graphTemplates = Lists.eitherOr(properties.getGraphTemplates(), graphTemplates(model, iri));

        List<String> property = properties.getProperty().isEmpty()
                ? property(model, iri)
                : properties.getProperty();

        var ps = Optionals.eitherOr(properties.findProperties(), propertiesForSubject(model, iri));

        List<String> includes = properties.getInclude().isEmpty()
                ? includeForSubject(model, iri).stream().map(Resource::stringValue).toList()
                : properties.getInclude();

        var additionalTriples = Optionals.eitherOr(properties.findAdditionalTriples(), additionalTriples(model, iri));

        var unionTemplate = Optionals.eitherOr(properties.findUnionTemplate(), unionTemplate(model, iri));

        var includeDatasetClause = Optionals.eitherOr(properties.findIncludeDatasetClause(), includeDatasetClause(model, iri));

        var includeNamespaces = Optionals.eitherOr(properties.findIncludeNamespaces(), includeNamespaces(model, iri));

        return SpecialisedViewer.builder()
                .iri(iri.stringValue())
                .name(name)
                .template(template)
                .graphTemplates(graphTemplates)
                .property(property)
                .properties(ps)
                .includes(includes)
                .additionalTriples(additionalTriples)
                .unionTemplate(unionTemplate)
                .includeDatasetClause(includeDatasetClause)
                .includeNamespaces(includeNamespaces)
                .build();

    }

    static void validateViewers(List<Viewer> vs) {
        final var iris = vs.stream()
                .map(Viewer::iri)
                .peek(iri -> {
                    if (Constants.LDA.DESCRIBE_VIEWER.equalsIgnoreCase(iri))
                        throw new RuntimeException("Built in Describe viewer redefined");
                    if (Constants.LDA.LABELLED_DESCRIBE_VIEWER.equalsIgnoreCase(iri))
                        throw new RuntimeException("Built in labelled describe viewer redefined");
                    if (Constants.LDA.BASIC_VIEWER.equalsIgnoreCase(iri))
                        throw new RuntimeException("Built in basic viewer redefined");
                })
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .anyMatch(e -> e.getValue() > 1)
                ;

        if (iris)
            throw new RuntimeException("Duplicated Viewer IRIs detected");

        final var names = vs.stream()
                .map(Viewer::name)
                .peek(name -> {
                    if (DescribeViewer.NAME.equalsIgnoreCase(name))
                        throw new RuntimeException("Built in Describe viewer redefined");
                    if (LabelledDescribeViewer.NAME.equalsIgnoreCase(name))
                        throw new RuntimeException("Built in labelled describe viewer redefined");
                    if (BasicViewer.NAME.equalsIgnoreCase(name))
                        throw new RuntimeException("Built in basic viewer redefined");
                })
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .anyMatch(e -> e.getValue() > 1)
                ;

        if (names)
            throw new RuntimeException("Duplicated Viewer names detected");

    }

    static List<Viewer> viewers(final TildaProperties tildaProperties, final Model model) {
        final List<Viewer> defined =  model.filter(null, Constants.RDF_TYPE, Constants.VIEWER_CLASS).stream()
                .map(Statement::getSubject)
                .map(iri -> viewer(tildaProperties, model, iri))
                .toList();

        validateViewers(defined);

        final ImmutableList.Builder<Viewer> viewers = ImmutableList.builder();

        viewers.addAll(defined);

        viewers.add(new DescribeViewer());
        viewers.add(new LabelledDescribeViewer());
        viewers.add(new BasicViewer());

        return viewers.build();
    }

}
