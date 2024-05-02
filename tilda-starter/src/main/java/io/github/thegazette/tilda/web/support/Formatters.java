package io.github.thegazette.tilda.web.support;

import com.google.common.collect.ImmutableList;
import org.eclipse.rdf4j.model.*;
import org.springframework.http.MediaType;
import io.github.thegazette.tilda.core.api.formatter.Formatter;
import io.github.thegazette.tilda.core.api.formatter.Xslt;
import io.github.thegazette.tilda.core.util.Constants;
import io.github.thegazette.tilda.web.config.properties.FormatterProperties;
import io.github.thegazette.tilda.web.config.properties.TildaProperties;

import java.util.*;

import static io.github.thegazette.tilda.web.support.Util.*;

public interface Formatters {

    static List<Formatter> formatters(final TildaProperties tildaProperties, final Model model) {
        final Map<String, FormatterProperties> properties = tildaProperties.getFormatters();

        final var xslts = model.filter(null, Constants.RDF_TYPE, Constants.XSLT_FORMATTER)
                .stream().map(Statement::getSubject)
                .map(iri -> {
                    var name = Common.nameForSubject(model, iri).orElse(Common.nameFromSubject(iri));
                    var props = properties.getOrDefault(name, new FormatterProperties());

                    List<String> mimeTypes = props.findMimeTypes().orElse(mimeTypesForSubject(model, iri));
                    var prefix = tildaProperties.getServer().getXsl().getPrefix();

                    var stylesheet = props.isStylesheetPresent()
                            ? props.getStylesheet()
                            : (stylesheetForSubject(model, iri)).orElseThrow();

                    return new Xslt(iri.stringValue(), name, mimeTypes, prefix, stylesheet, MediaType.TEXT_HTML);
                }).toList();


        return ImmutableList.<Formatter>builder()
                .add(Formatter.JSON)
                .add(Formatter.RDFXML)
                .add(Formatter.TURTLE)
                .add(Formatter.XML)
                .add(Formatter.RDFJSON)
                .add(Formatter.JSONLD)
                .addAll(xslts)
                .build();


    }

}
