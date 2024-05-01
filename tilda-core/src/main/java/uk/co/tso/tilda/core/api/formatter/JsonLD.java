package uk.co.tso.tilda.core.api.formatter;

import org.springframework.http.MediaType;
import uk.co.tso.tilda.core.util.Constants;
import uk.co.tso.tilda.core.util.TildaMediaTypes;

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
