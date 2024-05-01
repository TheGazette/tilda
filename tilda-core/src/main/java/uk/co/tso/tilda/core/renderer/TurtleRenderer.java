package uk.co.tso.tilda.core.renderer;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.WriterConfig;
import org.eclipse.rdf4j.rio.helpers.BasicWriterSettings;
import org.springframework.web.servlet.function.ServerResponse;
import uk.co.tso.tilda.core.api.formatter.Formatter;
import uk.co.tso.tilda.core.processor.context.OutputContext;
import uk.co.tso.tilda.core.processor.context.input.InputContext;

import java.io.StringWriter;

public record TurtleRenderer(Formatter formatter) implements Renderer {
    @Override
    public ServerResponse apply(InputContext inputContext, OutputContext outputContext) {
        var model = outputContext.model();
        var sw = new StringWriter();
        var config = new WriterConfig();
        config.set(BasicWriterSettings.INLINE_BLANK_NODES, true);
        config.set(BasicWriterSettings.PRETTY_PRINT, true);

        Rio.write(model, Rio.createWriter(RDFFormat.TURTLE, sw).setWriterConfig(config));
        var rdf = sw.toString();

        return ServerResponse.ok()
                .contentType(formatter.mediaType())
                .body(rdf);
    }
}
