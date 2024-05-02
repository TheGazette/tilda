package io.github.thegazette.tilda.core.api.formatter;

import io.github.thegazette.tilda.core.util.Constants;
import io.github.thegazette.tilda.core.util.TildaMediaTypes;
import org.springframework.http.MediaType;

import java.util.List;

public record JsonLD() implements Formatter {

    @Override
    public String iri() {
        return Constants.LDA.JSONLD_FORMATTER;
    }

    @Override
    public String name() {
        return "jsonld";
    }

    @Override
    public List<MediaType> mediaTypes() {
        return List.of(TildaMediaTypes.JSON_LD);
    }


    @Override
    public MediaType mediaType() {
        return TildaMediaTypes.JSON_LD;
    }

    @Override
    public List<String> fileExtensions() {
        return List.of("jsonld");
    }
}
