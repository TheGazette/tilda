package io.github.thegazette.tilda.core.api;

import io.github.thegazette.tilda.core.api.common.HasVariables;
import io.github.thegazette.tilda.core.api.common.HasViewers;

import java.util.List;
import java.util.Optional;


public interface API extends HasViewers, HasVariables {
    String iri();
    String name();
    Optional<String> label();
    Optional<String> comment();
    String sparqlEndpoint();
    Optional<String> base();
    ContentNegotiation contentNegotiation();
    List<String> lang();
    Optional<String> maxPageSize();
    Optional<String> defaultPageSize();
    List<String> vocabularies();
    List<String> declaredEndpoints();
    List<String> endpoints();
    Optional<String> defaultFormatter();

}
