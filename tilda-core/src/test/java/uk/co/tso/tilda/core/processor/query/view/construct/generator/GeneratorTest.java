package uk.co.tso.tilda.core.processor.query.view.construct.generator;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.LoggerFactory;
import uk.co.tso.tilda.core.api.vocabulary.Vocabulary;
import uk.co.tso.tilda.core.api.vocabulary.property.Property;
import uk.co.tso.tilda.core.processor.query.view.construct.chains.PropertyChains;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GeneratorTest {
    private final Vocabulary mockVocab = new Vocabulary() {
        @Override
        public Map<String, String> namespaces() {
            return Collections.emptyMap();
        }

        @Override
        public List<Property> properties() {
            return Collections.emptyList();
        }

        @Override
        public Optional<String> findPropertyIRIByLabel(String label) {
            if ("isAbout".equals(label))
                return Optional.of("<https://www.thegazette.co.uk/def/publication#isAbout>");
            if ("type".equals(label))
                return Optional.of("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>");
            if ("organisationName".equals(label))
                return Optional.of("<https://www.thegazette.co.uk/def/organisation#name>");


            return Optional.empty();
        }
    };

    private final String chains1 = "*,isAbout.*,isAbout.*.*,isAbout.*.*.*,isAbout.*.*.*.type,isAbout.*.*.*.organisationName";

    @Test
    public void test1() {
        var g = new ItemGenerator(mockVocab);
        var generated = g.generate( PropertyChains.from(chains1), "https://www.thegazette.co.uk/id/notice/4411940", "", "");
        assertNotNull(generated);

        var query = "CONSTRUCT " + generated.template() + "WHERE " + generated.pattern();
        System.out.print(query);

    }
}