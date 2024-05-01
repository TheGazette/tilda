package uk.co.tso.tilda.core.processor.query.view.construct.generator;

import uk.co.tso.tilda.core.api.vocabulary.Vocabulary;
import uk.co.tso.tilda.core.processor.query.view.construct.chains.PropertyChain;


public abstract class BaseGenerator<I> implements Generator<I>{
    private final Vocabulary vocabulary;

    protected BaseGenerator(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }

    protected Generated generate(PropertyChain chain, String item, int starter, int ender) {
        var iri = "<" + item + ">";
        var varStarter = "?var_" + starter;
        var varEnder = "_" + ender;

      return gen(chain, iri, varStarter, varEnder);
    }

    private Generated gen(PropertyChain chain, String iri, String varStarter, String varEnder) {
        StringBuilder templates = new StringBuilder();
        StringBuilder patterns = new StringBuilder();

        var isWildcard = "*".equals(chain.label());

        var prop = isWildcard
                ? varStarter + varEnder + "_prop"
                : vocabulary.getPropertyIRIByLabel(chain.label());
        var var = varStarter + varEnder;

        var stmt = "\t" + iri + " " + prop + " " + var + " .\r\n";
        templates.append(stmt);
        patterns.append("{ \r\n")
                .append(stmt);

        if (chain.size() > 0) {
            var nextItem = var;
            for (var entry : chain.entrySet()) {
                var nextVarStarter = varStarter + "_" + entry.getKey();
                var g2 = gen(entry.getValue(), nextItem, nextVarStarter, varEnder);
                templates.append(g2.template());
                patterns.append(" OPTIONAL ");
                patterns.append(g2.pattern());
            }
        }

        patterns.append("}");

        return new Generated(templates.toString(), patterns.toString());
    }

}
