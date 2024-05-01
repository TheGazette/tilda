package uk.co.tso.tilda.core.api.formatter;

import org.springframework.http.MediaType;
import uk.co.tso.tilda.core.util.Constants;

import java.util.List;

public record Xml() implements Formatter {

    public static final String NAME = "xml";

    @Override
    public String iri() {
        return Constants.LDA.XML_FORMATTER;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public List<MediaType> mediaTypes() {
        return List.of(MediaType.APPLICATION_XML);
    }

    @Override
    public MediaType mediaType() {
        return MediaType.APPLICATION_XML;
    }
}
