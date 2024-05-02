package io.github.thegazette.tilda.core.processor.query.view.construct.generator;

import io.github.thegazette.tilda.core.processor.query.view.construct.chains.PropertyChains;

public interface Generator<I> {
    public record Generated(String template, String pattern) {}

    Generated generate(PropertyChains chains, I i, String additionalTriples, String unionTemplate);

    default Generated generate(PropertyChains chains, I i) {
        return generate(chains, i, "", "");
    }

}
