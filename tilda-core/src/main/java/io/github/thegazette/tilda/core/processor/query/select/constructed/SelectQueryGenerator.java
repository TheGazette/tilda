package io.github.thegazette.tilda.core.processor.query.select.constructed;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.github.thegazette.tilda.core.processor.context.input.ItemsInputContext;
import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.RequestParametersFilter;
import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.SelectorFilter;
import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.SelectorParentFilter;
import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.explicit.RequestParameterWhereFilter;
import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.explicit.SelectorWhereFilter;
import io.github.thegazette.tilda.core.processor.query.select.constructed.sorting.constructed.SelectorSort;
import org.slf4j.LoggerFactory;
import io.github.thegazette.tilda.core.api.API;
import io.github.thegazette.tilda.core.api.Configuration;
import io.github.thegazette.tilda.core.api.Endpoint;
import io.github.thegazette.tilda.core.processor.query.select.constructed.sorting.constructed.RequestParameterSort;
import io.github.thegazette.tilda.core.processor.query.select.constructed.sorting.constructed.SelectorParentSort;
import io.github.thegazette.tilda.core.processor.query.select.constructed.sorting.explicit.RequestParameterOrderBy;
import io.github.thegazette.tilda.core.processor.query.select.constructed.sorting.explicit.SelectorOrderBy;
import io.github.thegazette.tilda.core.processor.query.select.constructed.sorting.explicit.SelectorParentOrderBy;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface SelectQueryGenerator extends Function<ItemsInputContext, String> {
    static SelectQueryGenerator build(final Configuration configuration, final API api, final Endpoint endpoint) {
        final var logger = LoggerFactory.getLogger(SelectQueryGenerator.class);
        final var requestParameterWhereFilter = RequestParameterWhereFilter.build(configuration, api, endpoint);
        final var requestParametersFilter = RequestParametersFilter.build(configuration, api, endpoint);
        final var selectorWhereFilter = SelectorWhereFilter.build(configuration, api, endpoint);
        final var selectorFilter = SelectorFilter.build(configuration, api, endpoint);
        final var selectorParentFilter = SelectorParentFilter.build(configuration, api, endpoint);

        final var requestParameterOrderBy = RequestParameterOrderBy.build(configuration, api, endpoint);
        final var requestParameterSort = RequestParameterSort.build(configuration, api, endpoint);
        final var selectorOrderBy = SelectorOrderBy.build(configuration, api, endpoint);
        final var selectorSort = SelectorSort.build(configuration, api, endpoint);
        final var selectorParentOrderBy = SelectorParentOrderBy.build(configuration, api, endpoint);
        final var selectorParentSort = SelectorParentSort.build(configuration, api, endpoint);

        return (context) -> {
            final var queryBuilder = QueryBuilder.initial();

            requestParameterWhereFilter.accept(context, queryBuilder);
            requestParametersFilter.accept(context, queryBuilder);
            selectorWhereFilter.accept(context, queryBuilder);
            selectorFilter.accept(context, queryBuilder);
            selectorParentFilter.accept(context, queryBuilder);

            requestParameterOrderBy.accept(context, queryBuilder);
            requestParameterSort.accept(context, queryBuilder);
            selectorOrderBy.accept(context, queryBuilder);
            selectorSort.accept(context, queryBuilder);
            selectorParentOrderBy.accept(context, queryBuilder);
            selectorParentSort.accept(context, queryBuilder);

            return queryBuilder.toSelect();
        };

    }

    class QueryBuilder {
        public record SameSubject(String name, String predicate, String object){};
        public record Pattern(String name, String pattern){};
        public record Filter(String name, String filter){};

        private QueryBuilder() {
        }

        public static QueryBuilder initial() {
            return new QueryBuilder();
        }

        private List<SameSubject> requestParameterSameSubjects = Collections.emptyList();
        private List<Pattern> requestParameterPatterns = Collections.emptyList();
        private List<Filter> requestParameterFilters = Collections.emptyList();

        private List<SameSubject> selectorSameSubjects = Collections.emptyList();
        private List<Pattern> selectorPatterns = Collections.emptyList();
        private List<Filter> selectorFilters = Collections.emptyList();

        private String requestParameterWhere = null;

        private String selectorWhere = null;

        private List<SameSubject> selectorParentSameSubjects = Collections.emptyList();
        private List<Pattern> selectorParentPatterns = Collections.emptyList();
        private List<Filter> selectorParentFilters = Collections.emptyList();


        private String requestParameterOrderBy = null;
        private List<String> requestParameterSortConditions = null;
        private List<SameSubject> requestParameterSortPatterns = Collections.emptyList();
        private String selectorOrderBy = null;
        private List<String> selectorSortConditions = null;
        private List<SameSubject> selectorSortPatterns = Collections.emptyList();


        public void requestParameterSameSubjects(List<SameSubject> requestParameterSameSubjects) {
            this.requestParameterSameSubjects = requestParameterSameSubjects;
        }

        public void requestParameterPatterns(List<Pattern> requestParameterPatterns) {
            this.requestParameterPatterns = requestParameterPatterns;
        }

        public void requestParameterFilters(List<Filter> requestParameterFilters) {
            this.requestParameterFilters = requestParameterFilters;
        }

        public void selectorSameSubjects(List<SameSubject> sameSubjects) {
            this.selectorSameSubjects = sameSubjects;
        }

        public void selectorPatterns(List<Pattern> selectorPatterns) {
            this.selectorPatterns = selectorPatterns;
        }

        public void selectorFilters(List<Filter> selectorFilters) {
            this.selectorFilters = selectorFilters;
        }

        public void selectorParentSameSubjects(List<SameSubject> selectorParentPatterns) {
            this.selectorParentSameSubjects = selectorParentPatterns;
        }

        public void selectorParentFilters(List<Filter> selectorParentFilters) {
            this.selectorParentFilters = selectorParentFilters;
        }

        public void requestParameterWhere(String requestParameterWhere) {
            this.requestParameterWhere = requestParameterWhere;
        }

        public void selectorWhere(String selectorWhere) {
            this.selectorWhere = selectorWhere;
        }

        public void requestParameterOrderBy(String requestParameterOrderBy) {
            this.requestParameterOrderBy = requestParameterOrderBy;
        }

        public void requestParameterSortPatterns(List<SameSubject> requestParameterSortPatterns) {
            this.requestParameterSortPatterns = requestParameterSortPatterns;
        }
        public void requestParameterSortConditions(List<String> requestParameterSortConditions) {
            this.requestParameterSortConditions = requestParameterSortConditions;
        }

        public void selectorOrderBy(String selectorOrderBy) {
            this.selectorOrderBy = selectorOrderBy;
        }

        public void selectorSortConditions(List<String> selectorSortConditions) {
            this.selectorSortConditions = selectorSortConditions;
        }

        public void selectorSortPatterns(List<SameSubject> selectorSortPatterns) {
            this.selectorSortPatterns = selectorSortPatterns;
        }

        public Set<String> alreadySeenParameterNames() {
            return ImmutableSet.<String>builder()
                    .addAll(requestParameterSameSubjects.stream().map(SameSubject::name).toList())
                    .addAll(requestParameterPatterns.stream().map(Pattern::name).toList())
                    .addAll(requestParameterFilters.stream().map(Filter::name).toList())
                    .addAll(selectorSameSubjects.stream().map(SameSubject::name).toList())
                    .addAll(selectorPatterns.stream().map(Pattern::name).toList())
                    .addAll(selectorFilters.stream().map(Filter::name).toList())
                    .addAll(selectorParentSameSubjects.stream().map(SameSubject::name).toList())
                    .addAll(selectorParentPatterns.stream().map(Pattern::name).toList())
                    .addAll(selectorParentFilters.stream().map(Filter::name).toList())
                    .addAll(requestParameterSortPatterns.stream().map(SameSubject::name).toList())
                    .addAll(selectorSortPatterns.stream().map(SameSubject::name).toList())
                    .build();
        }

        public boolean isRequestParameterOrderBySet() {
            return requestParameterOrderBy != null && !requestParameterOrderBy.isEmpty();
        }

        public boolean isRequestParameterSortSet() {
            return requestParameterSortConditions != null && !requestParameterSortConditions.isEmpty();
        }

        public boolean isSelectorOrderBySet() {
            return selectorOrderBy != null && !selectorOrderBy.isEmpty();
        }

        public boolean isSelectorSortSet() {
            return selectorSortConditions != null && !selectorSortConditions.isEmpty();
        }

        public String toSelect() {
            return "SELECT ?item\r\n" +
                    "WHERE {\r\n" +
                    where() +
                    "\r\n}" +
                    orderBy();
        }

        private String where() {
            var sb = new StringBuilder();
            //The spec says
            //The _where and api:where properties hold SPARQL syntax directly, and can be simply concatentated into the GroupGraphPattern.
            //so we're going to just "simply concatenate" and see what happens

            sb.append(requestParameterWhere != null ? requestParameterWhere : "");
            sb.append(selectorWhere != null ? selectorWhere : "");

            var sameSubjects = ImmutableList.<SameSubject>builder()
                    .addAll(requestParameterSameSubjects)
                    .addAll(selectorSameSubjects)
                    .addAll(selectorParentSameSubjects)
                    .addAll(requestParameterSortPatterns)
                    .addAll(selectorSortPatterns)
                    .build();

            if (!sameSubjects.isEmpty()) {
                var ps = sameSubjects.stream()
                        .map(p -> p.predicate() + " " + p.object)
                        .collect(Collectors.joining(";\r\n\t"));
                sb.append("\t").append("?item ").append(ps).append(" .");
            }


            requestParameterPatterns.stream()
                    .map(Pattern::pattern)
                    .forEach(pattern -> sb.append("\t").append(pattern).append("\r\n"));

            selectorPatterns.stream()
                    .map(Pattern::pattern)
                    .forEach(pattern -> sb.append("\t").append(pattern).append("\r\n"));

            selectorParentPatterns.stream()
                    .map(Pattern::pattern)
                    .forEach(pattern -> sb.append("\t").append(pattern).append("\r\n"));


            var filters = ImmutableList.<String>builder()
                    .addAll(requestParameterFilters.stream().map(Filter::filter).toList())
                    .addAll(selectorFilters.stream().map(Filter::filter).toList())
                    .addAll(selectorParentFilters.stream().map(Filter::filter).toList())
                    .build();

            if (!filters.isEmpty()) {
                var fs = String.join(" && ", filters);
                sb.append("\tFILTER(").append(fs).append(")");
            }

            var trimmed = sb.toString().trim();

            return trimmed.isEmpty() ? "\t?item ?property ?value" : trimmed;
        }

        private String orderBy() {
            if (isRequestParameterOrderBySet())
                return "ORDER BY " + requestParameterOrderBy + "\r\n";

            if (isRequestParameterSortSet())
                return "ORDER BY " + String.join(" ", requestParameterSortConditions) + "\r\n";

            if (isSelectorOrderBySet())
                return "ORDER BY " + selectorOrderBy + "\r\n";

            if (isSelectorSortSet())
                return "ORDER BY " +  String.join(" ", selectorSortConditions) + "\r\n";

            return "";
        }
    }
}
