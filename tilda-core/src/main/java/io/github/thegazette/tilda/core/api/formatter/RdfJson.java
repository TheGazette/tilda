package io.github.thegazette.tilda.core.api.formatter;

import io.github.thegazette.tilda.core.util.Constants;
import io.github.thegazette.tilda.core.util.TildaMediaTypes;
import org.springframework.http.MediaType;

import java.util.List;

public record RdfJson() implements Formatter {

    @Override
    public String iri() {
        return Constants.LDA.RDFJSON_FORMATTER;
    }

    @Override
    public String name() {
        return "rdfjson";
    }

    @Override
    public List<MediaType> mediaTypes() {
        return List.of(TildaMediaTypes.RDF_JSON);
    }

    @Override
    public MediaType mediaType() {
        return TildaMediaTypes.RDF_JSON;
    }

    @Override
    public List<String> fileExtensions() {
        return List.of("rdfjson", "rj");
    }
}
