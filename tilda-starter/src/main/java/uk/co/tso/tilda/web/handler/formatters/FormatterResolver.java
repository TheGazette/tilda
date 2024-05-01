package uk.co.tso.tilda.web.handler.formatters;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.function.ServerRequest;
import uk.co.tso.tilda.core.api.ContentNegotiation;
import uk.co.tso.tilda.core.api.formatter.Formatter;
import uk.co.tso.tilda.core.config.APIConfigurationContext;
import uk.co.tso.tilda.core.config.EndpointConfigurationContext;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface FormatterResolver extends Function<ServerRequest, Formatter> {
    interface Factory {
        static FormatterResolver build(EndpointConfigurationContext config) {
            final var configuration = config.configuration();
            final var api = config.api();
            final var endpoint = config.endpoint();

            final boolean isParameterBasedCN = ContentNegotiation.PARAMETER.equals(api.contentNegotiation());
            final var formatters = configuration.formatters();
            final Function<String, Optional<Formatter>> findFormatterByName = (name) -> {
                if (name == null || name.isEmpty())
                    return Optional.empty();

                return formatters.stream().filter(f -> f.name().equalsIgnoreCase(name)).findFirst();
            };

            final Supplier<Optional<String>> findDefaultEndpoint = () -> {
                var byEndpoint = endpoint.defaultFormatter().filter(s -> !s.isEmpty());
                if (byEndpoint.isPresent())
                    return byEndpoint;
                return api.defaultFormatter();
            };

            final var defaultFormatter = findDefaultEndpoint.get()
                    .flatMap(findFormatterByName)
                    .orElse(Formatter.JSON);



            return (request) -> {
                //According to the spec, we're supposed to throw a 400 Bad Request
                //if parameter cn has been selected, a _format has been sent and no formatter found
                if (isParameterBasedCN && request.param("_format").isPresent())
                    return request.param("_format").flatMap(findFormatterByName)
                            .orElseThrow(() ->new ResponseStatusException(HttpStatusCode.valueOf(400)));

                //According to the spec, if a file type extension is in the URI we should use that to look
                //up the viewer. Unlike the above, not finding one is not an issue here
                //However, it's 2023 and thanks to Reflected File Download attacks
                //such handling is now considered poor form
                //So we're going to throw a 404 if there's a file extension but no matching viewer was found
                if(!isParameterBasedCN) {
                    var l = request.requestPath().subPath(request.requestPath().elements().size() - 1);
                    var s = l.value().split("\\.");
                    if (s.length > 1) {
                        var suffix = s[s.length - 1];
                        return findFormatterByName.apply(suffix).orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404)));
                    }
                }
                final var accept = request.headers().accept();
                return formatters.stream()
                        .filter(f -> f.isAcceptable(accept)).findFirst().orElse(defaultFormatter);

            };

        }
    }
}
