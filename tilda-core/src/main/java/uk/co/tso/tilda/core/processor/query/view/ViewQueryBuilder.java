package uk.co.tso.tilda.core.processor.query.view;

import uk.co.tso.tilda.core.processor.context.input.InputContext;

import java.util.function.BiFunction;

public interface ViewQueryBuilder<I> extends BiFunction<InputContext, I, String> {

}
