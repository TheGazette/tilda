package io.github.thegazette.tilda.web.support;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import io.github.thegazette.tilda.core.util.Constants;

import java.util.List;
import java.util.Optional;

public interface Common {
    static String iri(Resource subject) {
        return subject.stringValue();
    }

    static Optional<String> comment(Model model, Resource subject) {
        return Util.objectValue(model, subject, Constants.RDFS_COMMENT);
    }

    static Optional<String> labelRDFS(Model model, Resource subject) {
        return Util.objectValue(model, subject, Constants.RDFS_LABEL);
    }

    static List<String> lang(Model model, Resource subject) {
        return model
                .filter(subject, Constants.API_LANG, null)
                .objects()
                .stream()
                .map(Value::stringValue)
                .toList();
    }

    static Optional<String> nameForSubject(Model model, Resource subject) {
        return Util.objectValue(model, subject, Constants.NAME);
    }

    static String nameFromSubject(Resource subject) {
        if (subject.isIRI()) {
            var iri = (IRI) subject;
            return iri.getLocalName();
        }

        if (subject.isLiteral()) {
            return subject.stringValue();
        }

        throw new RuntimeException();
    }

    static String name(final Model model, final Resource iri) {
        return nameForSubject(model, iri).orElse(nameFromSubject(iri));
    }
}
