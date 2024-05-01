package uk.co.tso.tilda.core.api;

import uk.co.tso.tilda.core.api.common.HasVariables;
import uk.co.tso.tilda.core.api.common.HasViewers;

import java.util.List;
import java.util.Optional;

public sealed interface Endpoint extends HasViewers, HasVariables permits Endpoint.Item, Endpoint.Items {
    String iri();
    String uriTemplate();
    Optional<String> defaultPageSize();
    Optional<String> comment();
    Optional<String> exampleRequestPath();
    List<String> lang();
    List<String> formatters();
    Optional<String> defaultFormatter();

     non-sealed interface Item extends Endpoint {
        String itemTemplate();
    }

    //aka "ListEndpoint"
    non-sealed interface Items extends Endpoint {
        String selector();
    }
}
