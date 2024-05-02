package io.github.thegazette.tilda.core.processor.query.view.construct.chains;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class PropertyChainTest {
    @Test

    void test0() {
        assertThrows(RuntimeException.class, () -> PropertyChain.ofLabel(null));
        assertThrows(RuntimeException.class, () -> PropertyChain.ofLabel(""));
    }

    @Test
    void test1() {
        var wildChain = PropertyChain.ofLabel("*");
        assertEquals("*", wildChain.label());
        assertEquals(0, wildChain.size());

        var labelChain = PropertyChain.ofLabel("label1");
        assertEquals("label1", labelChain.label());
        assertEquals(0, labelChain.size());

    }


    @Test
    void test2() {
        var wildChain = PropertyChain.ofLabel("*");
        wildChain.children("*");
        assertEquals("*", wildChain.label());
        assertEquals(1, wildChain.size());
        assertTrue(wildChain.containsKey(1));

        var labelChain = PropertyChain.ofLabel("label1");
        labelChain.children("*");
        assertEquals("label1", labelChain.label());
        assertEquals(1, labelChain.size());
        assertTrue(labelChain.containsKey(1));

    }

    @Test
    void test3() {
        var labelChain = PropertyChain.ofLabel("label1");
        labelChain.children("*");
        labelChain.children("*.*");
        assertEquals("label1", labelChain.label());
        assertEquals(1, labelChain.size());
        assertTrue(labelChain.containsKey(1));

        assertEquals("*", labelChain.get(1).label());
        assertEquals(1, labelChain.get(1).size());
        assertTrue(labelChain.get(1).containsKey(1));

        assertEquals("*", labelChain.get(1).get(1).label());
        assertEquals(0, labelChain.get(1).get(1).size());
        assertFalse(labelChain.get(1).get(1).containsKey(1));


    }

}