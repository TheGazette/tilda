package io.github.thegazette.tilda.core.api.formatter;

import io.github.thegazette.tilda.core.util.Constants;
import io.github.thegazette.tilda.core.util.TildaMediaTypes;
import org.springframework.http.MediaType;

import java.util.List;

public record RdfXml() implements Formatter {

    @Override
    public String iri() {
        return Constants.LDA.RDFXML_FORMATTER;
    }

    @Override
    public String name() {
        return "rdf";
    }

    @Override
    public List<MediaType> mediaTypes() {
        return List.of(TildaMediaTypes.RDF_XML);
    }



    @Override
    public MediaType mediaType() {
        return TildaMediaTypes.RDF_XML;
    }

    @Override
    public List<String> fileExtensions() {
        return List.of("rdf");
    }
}
