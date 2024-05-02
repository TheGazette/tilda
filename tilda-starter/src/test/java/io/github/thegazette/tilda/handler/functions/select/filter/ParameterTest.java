package io.github.thegazette.tilda.handler.functions.select.filter;

import org.junit.jupiter.api.Test;
import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.parameter.Parameter;

import static org.junit.jupiter.api.Assertions.*;

class ParameterTest {
    @Test
    void test1() {
        var f = Parameter.ofQuery("x=y");
        assertEquals("x", f.name());
        assertEquals("y", f.value());
    }

    @Test
    void test2() {
        var f = Parameter.ofQuery("min-x=y");
        assertTrue(f instanceof Parameter.Min);
        assertEquals("x", f.name());
        assertEquals("y", f.value());
    }


}