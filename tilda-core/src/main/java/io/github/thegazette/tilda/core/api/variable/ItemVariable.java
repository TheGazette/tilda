package io.github.thegazette.tilda.core.api.variable;

public record ItemVariable(String name, String value) implements Variable {
    public static Variable of(String name, String value) {
        return new ItemVariable(name, value);
    }
}
