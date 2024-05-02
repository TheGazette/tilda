package io.github.thegazette.tilda.core.api.formatter;

import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

public sealed interface Formatter permits Json, RdfXml, RdfJson, Turtle, Xml, Xslt, JsonLD {
    Json JSON = new Json();
    RdfXml RDFXML = new RdfXml();
    RdfJson RDFJSON = new RdfJson();
    Turtle TURTLE = new Turtle();
    Xml XML = new Xml();
    JsonLD JSONLD = new JsonLD();
    String iri();
    String name();
    List<MediaType> mediaTypes();

    MediaType mediaType();

    default boolean isAcceptable(List<MediaType> mediaTypes) {
        return mediaTypes.stream().anyMatch(mt -> mt.equalsTypeAndSubtype(mediaType()));
    }

    default List<String> fileExtensions() {
        return Collections.emptyList();
    }

    default boolean isAcceptableForFileExtension(String fileType) {
        if (fileType == null || fileType.isEmpty())
            return false;
        return fileExtensions().stream().anyMatch(fileType::equalsIgnoreCase);
    }

}
