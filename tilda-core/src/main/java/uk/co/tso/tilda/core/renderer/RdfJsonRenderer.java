package uk.co.tso.tilda.core.renderer;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.web.servlet.function.ServerResponse;
import uk.co.tso.tilda.core.api.formatter.Formatter;
import uk.co.tso.tilda.core.processor.context.OutputContext;
import uk.co.tso.tilda.core.processor.context.input.InputContext;

import java.io.StringWriter;

public record RdfJsonRenderer(Formatter formatter) implements Renderer {
    @Override
    public ServerResponse apply(InputContext inputContext, OutputContext outputContext) {
        var model = outputContext.model();
        var sw = new StringWriter();
        Rio.write(model, Rio.createWriter(RDFFormat.RDFJSON, sw));
        var rdf = sw.toString();

        return ServerResponse.ok()
                .contentType(formatter.mediaType())
                .body(rdf);
    }
}
