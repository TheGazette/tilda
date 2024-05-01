package uk.co.tso.tilda.web.support;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import uk.co.tso.tilda.core.api.Selector;
import uk.co.tso.tilda.core.util.Constants;
import uk.co.tso.tilda.core.util.Optionals;
import uk.co.tso.tilda.web.config.properties.TildaProperties;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static uk.co.tso.tilda.web.support.Common.nameForSubject;
import static uk.co.tso.tilda.web.support.Common.nameFromSubject;

public interface Selectors {
    static Optional<String> parent(Model model, Resource subject) {
        return Util.objectResource(model, subject, Constants.PARENT).map(Resource::stringValue);
    }

    static Optional<String> select(Model model, Resource subject) {
        return Util.objectValue(model, subject, Constants.SELECT);
    }

    static Optional<String> where(Model model, Resource subject) {
        return Util.objectValue(model, subject, Constants.WHERE);
    }

    static Optional<String> orderBy(Model model, Resource subject){
        return Util.objectValue(model, subject, Constants.ORDER_BY);
    }

    static Optional<String> filter(Model model, Resource subject){
        return Util.objectValue(model, subject, Constants.FILTER);
    }

    static Optional<String> sort(Model model, Resource subject){
        return Util.objectValue(model, subject, Constants.SORT);
    }

    static Selector selector(final TildaProperties tildaProperties, final Model config, final Resource iri) {
        var name = nameForSubject(config, iri).orElse(nameFromSubject(iri));
        var properties = tildaProperties.selector(name);

        final Optional<String> parent = Optionals.eitherOr(properties.findParent(), parent(config, iri));
        final Optional<String> select = Optionals.eitherOr(properties.findSelect(), select(config, iri));
        final Optional<String> where = Optionals.eitherOr(properties.findWhere(), where(config, iri));
        final Optional<String> orderBy = Optionals.eitherOr(properties.findOrderBy(), orderBy(config, iri));
        final Optional<String> filter = Optionals.eitherOr(properties.findFilter(), filter(config, iri));
        final Optional<String> sort = Optionals.eitherOr(properties.findSort(), sort(config, iri));

        return new Selector() {

            @Override
            public String name() {
                return name;
            }

            @Override
            public String iri() {
                return iri.stringValue();
            }

            @Override
            public Optional<String> parent() {
                return parent;
            }

            @Override
            public Optional<String> select() {
                return select;
            }

            @Override
            public Optional<String> where() {
                return where;
            }

            @Override
            public Optional<String> orderBy() {
                return orderBy;
            }

            @Override
            public Optional<String> filter() {
                return filter;
            }

            @Override
            public Optional<String> sort() {
                return sort;
            }

            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("{\n");
                sb.append("name:").append(name()).append("\n");
                sb.append("parent:").append(parent().orElse("-----")).append("\n");
                sb.append("select:").append(select().orElse("-----")).append("\n");
                sb.append("where:").append(where().orElse("-----")).append("\n");
                sb.append("orderBy:").append(orderBy().orElse("-----")).append("\n");
                sb.append("filter:").append(filter().orElse("-----")).append("\n");
                sb.append("sort:").append(sort().orElse("-----")).append("\n");

                sb.append('}');
                return sb.toString();
            }
        };

    }

    static Map<String, Selector> selectors(final TildaProperties tildaProperties, final Model model) {
        return model.filter(null, Constants.RDF_TYPE, Constants.SELECTOR_CLASS).subjects().stream()
                .map(iri -> selector(tildaProperties, model, iri))
                .collect(Collectors.toMap(Selector::iri, Function.identity()));

    }

}
