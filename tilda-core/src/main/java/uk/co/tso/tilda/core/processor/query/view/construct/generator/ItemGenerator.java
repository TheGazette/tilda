package uk.co.tso.tilda.core.processor.query.view.construct.generator;

import uk.co.tso.tilda.core.api.vocabulary.Vocabulary;
import uk.co.tso.tilda.core.processor.query.view.construct.chains.PropertyChains;

public class ItemGenerator extends BaseGenerator<String> {
    private final Vocabulary vocabulary;

    public ItemGenerator(Vocabulary vocabulary) {
        super(vocabulary);
        this.vocabulary = vocabulary;
    }

    public Generated generate(PropertyChains chains, String item, String additionalTriples, String unionTemplate ) {
        StringBuilder templates = new StringBuilder();
        StringBuilder patterns = new StringBuilder();
        templates.append("{\r\n");
        patterns.append("\r\n{");


        for (var chain : chains.entrySet()) {
            int j = chain.getKey();
            var g = generate(chain.getValue(), item, j, 0);
            templates.append(g.template());
            if (j == 1) {
                patterns.append(g.pattern());
            } else {
                patterns.append("UNION {").append(g.pattern()).append("}");
            }

        }
        if (!additionalTriples.isEmpty())
            templates.append(additionalTriples);
        templates.append("\r\n\t}");
        if (!unionTemplate.isEmpty())
            patterns.append("UNION { ").append(unionTemplate).append("}");
        patterns.append("}");

        return new Generated(templates.toString(), patterns.toString());
    }

}
