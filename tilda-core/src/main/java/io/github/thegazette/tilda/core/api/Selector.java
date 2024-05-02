package io.github.thegazette.tilda.core.api;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

public interface Selector {
    String name();
    String iri();
    Optional<String> parent();
    Optional<String> select();
    Optional<String> where();
    Optional<String> orderBy();
    Optional<String> filter();
    Optional<String> sort();

}
