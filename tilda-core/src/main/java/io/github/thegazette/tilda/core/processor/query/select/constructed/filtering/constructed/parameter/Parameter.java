package io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.parameter;

public sealed interface Parameter permits
        Parameter.Plain,
        Parameter.Min, Parameter.Max,
        Parameter.MinEx, Parameter.MaxEx,
        Parameter.Name, Parameter.Exists{
    String name();
    String value();
    static Parameter ofQuery(String q) {
        final var nv = q.split("=");
        final var name = nv[0];
        final var value = nv[1];
        return of(name, value);
    }

    static Parameter of(String name, String value) {
        var pn = name.split("-");
        if (pn.length == 1)
            return new Plain(name, value);
        return switch(pn[0].toLowerCase()) {
            case "min" -> new Min(pn[1], value);
            case "max" -> new Max(pn[1], value);
            case "minex" -> new MinEx(pn[1], value);
            case "maxex" -> new MaxEx(pn[1], value);
            case "name" -> new Name(pn[1], value);
            case "exists" -> new Exists(pn[1], value);
            default -> throw new UnsupportedOperationException("Unknown prefix on filter");
        };
    }

    record Plain(String name, String value) implements Parameter {}
    record Min(String name, String value) implements Parameter {}
    record Max(String name, String value) implements Parameter {}
    record MinEx(String name, String value) implements Parameter {}
    record MaxEx(String name, String value) implements Parameter {}
    record Name(String name, String value) implements Parameter {}
    record Exists(String name, String value) implements Parameter {}


}
