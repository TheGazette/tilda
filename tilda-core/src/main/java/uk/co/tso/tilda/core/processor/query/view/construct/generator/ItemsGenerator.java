package uk.co.tso.tilda.core.processor.query.view.construct.generator;

import com.google.common.collect.Lists;
import uk.co.tso.tilda.core.api.vocabulary.Vocabulary;
import uk.co.tso.tilda.core.processor.query.view.construct.chains.PropertyChains;

import java.util.List;

public class ItemsGenerator extends BaseGenerator<List<String>> {
    public ItemsGenerator(Vocabulary vocabulary) {
        super(vocabulary);
    }

    @Override
    public Generated generate(PropertyChains chains, List<String> items, String additionalTriples, String unionTemplate) {
        if (items == null || items.isEmpty())
            throw new RuntimeException();
        StringBuilder templates = new StringBuilder();
        StringBuilder patterns = new StringBuilder();
        templates.append("{\r\n");
        patterns.append("\r\n{");

        var head = items.get(0);
        var tail = items.subList(1, items.size());

        for (var chain : chains.entrySet()) {
            int j = chain.getKey();
            var g = generate(chain.getValue(), head, j, 0);
            templates.append(g.template());
            if (j == 1) {
                patterns.append(g.pattern());
            } else {
                patterns.append("UNION {").append(g.pattern()).append("}");
            }
        }

        for (int i = 0; i < items.size(); i++) {
            patterns.append("UNION {");
            var item = items.get(i);
            for (var chain : chains.entrySet()) {
                int j = chain.getKey();
                var g = generate(chain.getValue(), item, j, i + 1);
                templates.append(g.template());
                if (j == 1) {
                    patterns.append(g.pattern());
                } else {
                    patterns.append("UNION {").append(g.pattern()).append("}");
                }
            }

            patterns.append("}");
        }


        templates.append("\r\n\t}");
        patterns.append("}");

        return new Generated(templates.toString(), patterns.toString());
    }
}
