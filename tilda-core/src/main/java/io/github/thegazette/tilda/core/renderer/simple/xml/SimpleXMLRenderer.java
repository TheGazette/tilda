package io.github.thegazette.tilda.core.renderer.simple.xml;

import io.github.thegazette.tilda.core.processor.context.OutputContext;
import io.github.thegazette.tilda.core.processor.context.input.InputContext;
import io.github.thegazette.tilda.core.renderer.simple.intermediate.ModelToResults;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Values;
import org.springframework.web.servlet.function.ServerResponse;
import io.github.thegazette.tilda.core.api.formatter.Formatter;
import io.github.thegazette.tilda.core.api.vocabulary.Vocabulary;
import io.github.thegazette.tilda.core.renderer.Renderer;

import java.io.StringWriter;

public final class SimpleXMLRenderer implements Renderer {
    private final Formatter formatter;
    private final ModelToResults modelToResults;

    private SimpleXMLRenderer(Formatter formatter, ModelToResults modelToResults) {
        this.formatter = formatter;
        this.modelToResults = modelToResults;
    }

    public static SimpleXMLRenderer from(final Formatter formatter, final Vocabulary vocabulary) {
        final var modelToResults = ModelToResults.build(vocabulary);
        return new SimpleXMLRenderer(formatter, modelToResults);
    }

    @Override
    public ServerResponse apply(InputContext inputContext, OutputContext outputContext) {
        final var model = outputContext.model();
        final IRI uriIRI = Values.iri(inputContext.request().uri().toString());

        final var results = modelToResults.apply(model, uriIRI);

        final var writer = new SimpleXMLWriter();
        var sw = new StringWriter();

        writer.write(results, sw);


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
