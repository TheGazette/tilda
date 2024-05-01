package uk.co.tso.tilda.util;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import uk.co.tso.tilda.core.util.Constants;
import uk.co.tso.tilda.web.config.properties.APIProperties;
import uk.co.tso.tilda.web.config.properties.FormatterProperties;
import uk.co.tso.tilda.web.config.properties.TildaProperties;
import uk.co.tso.tilda.web.support.Configurations;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

class ConfigTest {
    final Logger logger = LoggerFactory.getLogger(getClass());

    private TildaProperties tildaProperties() {
        var props = new TildaProperties();

        var api = new APIProperties();
        api.setSparqlEndpoint("https://www.thegazette.co.uk/sparql");
        props.setApis(Map.of("api", api));

      //  props.setApi(api);


        var fp = new FormatterProperties();
        fp.setStylesheet("views/xslt-styles/gazette_flint.xsl");
        props.setFormatters(Map.of("html", fp));

        return props;
    }

    private Model tildaModel() throws IOException {
        var ttl = ResourceUtils.getFile("classpath:application.ttl");
        var input = new FileInputStream(ttl);
        return   Rio.parse(input, "", RDFFormat.TURTLE);

    }

    @Test
    public void load() throws IOException {
        var props = tildaProperties();
        var model = tildaModel();

        model.filter(null, Constants.RDF_TYPE, Constants.API_CLASS)
                .forEach(statement -> logger.error("{}", statement));



        var config = Configurations.configuration(props, model);

        config.selectors().forEach((k, v) -> logger.warn("{}:{}", k, v));

        config.formatters().forEach((v) -> logger.warn("{}:{}", v.name(), v.iri()));
        config.endpoints().forEach((k, v) -> logger.warn("{}:{}", k, v));

        config.vocabulary().namespaces().forEach((k, v) -> logger.warn("{}:{}", k, v));
    }
}