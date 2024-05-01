package uk.co.tso.tilda.web.support;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import uk.co.tso.tilda.web.config.properties.TildaProperties;

import java.util.Map;
import java.util.stream.Collectors;

public interface Namespaces {
    static Map<String, String> namespaces(final TildaProperties tildaProperties, final Model model) {
        //TODO: Add a way to add namespaces by TildaProperties, for now we'll just go with what's in the TTL
        return model.getNamespaces().stream().collect(Collectors.toMap(Namespace::getPrefix, Namespace::getName));
    }


}
