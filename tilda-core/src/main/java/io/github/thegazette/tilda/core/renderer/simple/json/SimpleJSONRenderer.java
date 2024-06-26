package io.github.thegazette.tilda.core.renderer.simple.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thegazette.tilda.core.processor.context.OutputContext;
import io.github.thegazette.tilda.core.processor.context.input.InputContext;
import io.github.thegazette.tilda.core.renderer.simple.intermediate.ModelToResults;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Values;
import org.springframework.web.servlet.function.ServerResponse;
import io.github.thegazette.tilda.core.api.formatter.Formatter;
import io.github.thegazette.tilda.core.api.vocabulary.Vocabulary;
import io.github.thegazette.tilda.core.renderer.Renderer;

import java.io.IOException;
import java.io.StringWriter;

public final class SimpleJSONRenderer implements Renderer {
    private final Formatter formatter;
    private final ModelToResults modelToResults;

    private SimpleJSONRenderer(Formatter formatter, ModelToResults modelToResults) {
        this.formatter = formatter;
        this.modelToResults = modelToResults;
    }

    public static SimpleJSONRenderer from(final Formatter formatter, final Vocabulary vocabulary) {
        final var modelToResults = ModelToResults.build(vocabulary);
        return new SimpleJSONRenderer(formatter, modelToResults);
    }

    @Override
    public ServerResponse apply(InputContext inputContext, OutputContext outputContext) {
        final var model = outputContext.model();
        var sw = new StringWriter();

        final IRI uriIRI = Values.iri(inputContext.request().uri().toString());

        var objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(sw, modelToResults.apply(model, uriIRI));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var xml = sw.toString();
        return ServerResponse.ok()
                .contentType(formatter.mediaType())
                .body(xml);
    }

    @Override
    public Formatter formatter() {
        return formatter;
    }
}
