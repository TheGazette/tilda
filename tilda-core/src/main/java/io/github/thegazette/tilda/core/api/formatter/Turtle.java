package io.github.thegazette.tilda.core.api.formatter;

import io.github.thegazette.tilda.core.util.Constants;
import io.github.thegazette.tilda.core.util.TildaMediaTypes;
import org.springframework.http.MediaType;

import java.util.List;

public record Turtle() implements Formatter {
    @Override
    public String iri() {
        return Constants.LDA.TURTLE_FORMATTER;
    }

    @Override
    public String name() {
        return "ttl";
    }

    @Override
    public List<MediaType> mediaTypes() {
        return List.of(TildaMediaTypes.TURTLE);
    }

    @Override
    public MediaType mediaType() {
        return TildaMediaTypes.TURTLE;
    }

    @Override
    public List<String> fileExtensions() {
        return List.of("ttl", "turtle");
    }
}
