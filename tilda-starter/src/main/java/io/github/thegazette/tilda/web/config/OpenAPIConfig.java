package io.github.thegazette.tilda.web.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.eclipse.rdf4j.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.util.UriComponentsBuilder;
import io.github.thegazette.tilda.core.api.ContentNegotiation;
import io.github.thegazette.tilda.core.api.Endpoint;
import io.github.thegazette.tilda.web.config.properties.TildaProperties;
import io.github.thegazette.tilda.web.support.Configurations;

import java.util.function.Supplier;

@Configuration
@DependsOn("tilda-configuration-model")
public class OpenAPIConfig {
    private static final ApiResponse RDF_XML = new ApiResponse().content(new Content().addMediaType("application/rdf+xml", new MediaType()));
    @Bean
    public OpenAPI openAPI(@Autowired TildaProperties props,
                           @Autowired @Qualifier("tilda-configuration-model") Model model) {
        final var configuration = Configurations.configuration(props, model);
        final var openAPI = new OpenAPI();

        final var responses = new ApiResponses();
        final var r200 = new ApiResponse();
        final var c = new Content();
        configuration.formatters().forEach(formatter -> {
                c.addMediaType(formatter.mediaType().toString(), new MediaType());
            });
        r200.content(c);
        responses.addApiResponse("200", r200);


        configuration.apis().forEach((name, api) -> {
            openAPI.info(new Info().title(api.label().orElse(name)).description(api.comment().orElse(name)));
            api.endpoints().forEach(endpoint -> {
                if (configuration.endpoints().containsKey(endpoint)) {
                    var conf = configuration.endpoints().get(endpoint);

                    final Supplier<Operation> operation = () -> {
                        var op = new Operation()
                                //.operationId(api + endpoint)
                                .description(conf.comment().orElse(conf.iri()))
                                .addTagsItem("Endpoints");


                        UriComponentsBuilder.fromUriString(conf.uriTemplate()).build()
                                .getPathSegments()
                                .stream()
                                .filter(s -> s.startsWith("{"))
                                .map(s -> s.replace("{", "").replace("}", ""))
                                .map(s -> new Parameter().name(s).in(ParameterIn.PATH.toString()))
                                .forEach(op::addParametersItem);


                        if (conf instanceof Endpoint.Items) {
                            op.addParametersItem(new Parameter().name("_page").required(false).in(ParameterIn.QUERY.toString()));
                            op.addParametersItem(new Parameter().name("_pageSize").required(false).in(ParameterIn.QUERY.toString()));
                            op.addParametersItem(new Parameter().name("_sort").description("sort by ex: telephone for asc and -telephone for desc").required(false).in(ParameterIn.QUERY.toString()));
                        }

                        return op;
                    };


                    if (ContentNegotiation.PARAMETER.equals(api.contentNegotiation())) {
                        var op = operation.get().responses(responses).addParametersItem(new Parameter().name("_format").in(ParameterIn.QUERY.toString()).required(false));
                        openAPI.path(conf.uriTemplate(), new PathItem().get(op));
                    } else {
                        //I haven't found a good way to generate swagger that has an optional file type
                        //suffix, probably because most people creating a REST api would let HTTP's
                        //built-in content negotiation handle it
                        var op1 = operation.get().responses(responses);;
                        openAPI.path(conf.uriTemplate(), new PathItem().get(op1));
                        configuration.formatters().forEach(formatter -> {
                            final var content = new Content();
                            content.addMediaType(formatter.mediaType().toString(), new MediaType());
                            var op = operation.get()
                                    .responses(new ApiResponses().addApiResponse("200", new ApiResponse().content(content)));
                            openAPI.path(conf.uriTemplate() + "." + formatter.name(), new PathItem().get(op));

                        });
                    }


                }
            });
        });

        return openAPI;
    }
}
