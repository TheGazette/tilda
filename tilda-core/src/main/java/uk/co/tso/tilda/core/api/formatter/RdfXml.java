package uk.co.tso.tilda.core.api.formatter;

import org.springframework.http.MediaType;
import uk.co.tso.tilda.core.util.Constants;
import uk.co.tso.tilda.core.util.TildaMediaTypes;

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
