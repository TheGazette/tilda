package io.github.thegazette.tilda.web.handler.variables;

import org.springframework.web.servlet.function.ServerRequest;
import io.github.thegazette.tilda.core.api.variable.Variable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;


public interface VariablesResolver extends Function<ServerRequest, List<Variable>> {
    Function<List<Variable>, Predicate<Variable>> notInVars = (vars) -> (variable) -> vars.stream().noneMatch(variable::isSameName);

}
