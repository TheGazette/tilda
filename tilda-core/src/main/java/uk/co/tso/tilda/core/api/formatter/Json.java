package uk.co.tso.tilda.core.api.formatter;

import org.springframework.http.MediaType;
import uk.co.tso.tilda.core.util.Constants;

import java.util.List;

public record Json() implements Formatter {
    @Override
    public String iri() {
        return Constants.LDA.JSON_FORMATTER;
    }

    @Override
    public String name() {
        return "json";
    }

    @Override
    public List<MediaType> mediaTypes() {
        return List.of(MediaType.APPLICATION_JSON);
    }

    @Override
    public MediaType mediaType() {
        return MediaType.APPLICATION_JSON;
    }
}
