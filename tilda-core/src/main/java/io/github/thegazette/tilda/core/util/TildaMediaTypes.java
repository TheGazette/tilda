package io.github.thegazette.tilda.core.util;

import org.springframework.http.MediaType;

public interface TildaMediaTypes {
    MediaType RDF_XML = MediaType.parseMediaType("application/rdf+xml");
    MediaType RDF_JSON = MediaType.parseMediaType("application/rdf+json");
    MediaType TURTLE = MediaType.parseMediaType("text/turtle");
    MediaType JSON_LD = MediaType.parseMediaType("application/ld+json");
}
