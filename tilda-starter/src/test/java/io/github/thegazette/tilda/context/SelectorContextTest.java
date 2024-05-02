package io.github.thegazette.tilda.context;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import io.github.thegazette.tilda.web.config.properties.TildaProperties;
import io.github.thegazette.tilda.web.support.Selectors;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SelectorContextTest {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void from() throws IOException {
        var ttl = ResourceUtils.getFile("classpath:application.ttl");
        var input = new FileInputStream(ttl);
        var model =  Rio.parse(input, "", RDFFormat.TURTLE);

        var props = new TildaProperties();

        var context = Selectors.selectors(props, model);


        assertNotNull(context);

        context.forEach((r,f) -> logger.error("{} {}", r, f.iri()));

    }
}