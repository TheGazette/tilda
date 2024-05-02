package io.github.thegazette.tilda.core.api.common;

import io.github.thegazette.tilda.core.api.variable.Variable;

import java.util.Collections;
import java.util.List;

/**
 * The spec says variables can be defined on the API and endpoints
 *
 * Gazettes doesn't seem to use this, so we're not bothering for now
 */
public interface HasVariables {
    default List<Variable> variables() {return Collections.emptyList();}
}
