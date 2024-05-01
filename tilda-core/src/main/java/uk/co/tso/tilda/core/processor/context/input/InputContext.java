package uk.co.tso.tilda.core.processor.context.input;

import org.springframework.web.servlet.function.ServerRequest;

import uk.co.tso.tilda.core.config.EndpointConfigurationContext;
import uk.co.tso.tilda.core.api.formatter.Formatter;
import uk.co.tso.tilda.core.api.variable.Variable;
import uk.co.tso.tilda.core.api.viewer.Viewer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface InputContext {
    EndpointConfigurationContext<?> configuration();

    ServerRequest request();
    List<Variable> variables();
    Viewer viewer();
    Formatter formatter();

    default Optional<String> param(String name) {
        return request().param(name);
    }

    default Optional<String> properties() {
        return this.param("_properties");
    }

    default Map<String, Object> variableMap() {
        return this.variables().stream().collect(Collectors.toMap(Variable::name, Variable::value));
    }
}
