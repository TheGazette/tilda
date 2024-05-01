package uk.co.tso.tilda.web.support;

import org.eclipse.rdf4j.model.*;
import uk.co.tso.tilda.core.util.Constants;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.eclipse.rdf4j.model.util.Values.iri;

public interface Util {

    static Optional<Resource> objectResource(Model model, Resource subject, IRI predicate) {
        return objectResources(model, subject, predicate).stream().findFirst();
    }

    static List<Resource> objectResources(Model model, Resource subject, IRI predicate) {
        return model.filter(subject, predicate, null).objects().stream()
                .filter(Value::isResource)
                .map(v -> (Resource) v)
                .toList();
    }

    static Optional<String> objectValue(Model model, IRI predicate) {
        return objectValueStream(model, null, predicate).findFirst();
    }


    static Optional<String> objectValue(Model model, Resource subject, IRI predicate) {
        return objectValueStream(model, subject, predicate).findFirst();
    }

    static List<String> objectValues(Model model, Resource subject, IRI predicate) {
        return  objectValueStream(model, subject, predicate).toList();
    }

    static Stream<String> objectValueStream(Model model, Resource subject, IRI predicate) {
        return model
                .filter(subject, predicate, null)
                .objects()
                .stream()
                .map(Value::stringValue);
    }

    static List<String> mimeTypesForSubject(Model model, Resource subject) {
        return objectValues(model, subject, Constants.MIME_TYPE);
    }

    static Optional<String> stylesheetForSubject(Model model, Resource subject) {
        return objectValue(model, subject, Constants.STYLESHEET);
    }

}
