package uk.co.tso.tilda.web.config;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class TildaConfiguration {

    @Bean("tilda-configuration-model")
    public Model model(@Value("classpath:application.ttl") Resource modelFile) throws IOException {
        var input = modelFile.getInputStream();
        return Rio.parse(input, "", RDFFormat.TURTLE);
    }


}
