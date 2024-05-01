package uk.co.tso.tilda.web.handler.variables;

import com.google.common.collect.ImmutableList;
import org.springframework.web.util.UriComponentsBuilder;
import uk.co.tso.tilda.core.api.variable.ItemVariable;
import uk.co.tso.tilda.core.api.variable.Variable;
import uk.co.tso.tilda.core.config.ItemEndpointConfigurationContext;

import java.util.Map;
import java.util.stream.Collectors;

public interface ItemEndpointVariableResolver extends VariablesResolver {
    interface Factory {
        static ItemEndpointVariableResolver build(final ItemEndpointConfigurationContext config) {
            final var parentVariables = EndpointVariableResolver.Factory.build(config);
            final var itemTemplate = config.endpoint().itemTemplate();

            return (request) -> {
                final var parentVars = parentVariables.apply(request);
                final Map<String, Object> uriVariables = parentVars.stream().collect(Collectors.toMap(Variable::name, Variable::value));

                final var item = ItemVariable.of("item", UriComponentsBuilder.fromUriString(itemTemplate).uriVariables(uriVariables).build().toUriString());

                return ImmutableList.<Variable>builder()
                        .add(item)
                        .addAll(parentVars.stream().filter(v -> !v.isSameName(item)).toList())
                        .build();
            };
        }
    }

}
