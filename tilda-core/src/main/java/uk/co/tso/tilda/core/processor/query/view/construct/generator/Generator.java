package uk.co.tso.tilda.core.processor.query.view.construct.generator;

import uk.co.tso.tilda.core.api.vocabulary.Vocabulary;
import uk.co.tso.tilda.core.processor.query.view.construct.chains.PropertyChain;
import uk.co.tso.tilda.core.processor.query.view.construct.chains.PropertyChains;

import java.util.List;

public interface Generator<I> {
    public record Generated(String template, String pattern) {}

    Generated generate(PropertyChains chains, I i, String additionalTriples, String unionTemplate);

    default Generated generate(PropertyChains chains, I i) {
        return generate(chains, i, "", "");
    }

}
