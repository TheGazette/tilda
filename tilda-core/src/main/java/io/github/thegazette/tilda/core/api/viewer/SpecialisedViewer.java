package io.github.thegazette.tilda.core.api.viewer;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class SpecialisedViewer implements Viewer {
    public enum IncludeDatasetClause {
        DEFAULT,
        YES,
        NO;

        static IncludeDatasetClause from(String s) {
            if (s == null)
                return DEFAULT;

            return switch (s.toLowerCase()) {
                case "yes" -> YES;
                case "no" -> NO;
                default -> DEFAULT;
            };
        }
    }

    public enum IncludeNamespaces {
        DEFAULT,
        YES,
        NO;

        static IncludeNamespaces from(String s) {
            if (s == null)
                return DEFAULT;

            return switch (s.toLowerCase()) {
                case "yes" -> YES;
                case "no" -> NO;
                default -> DEFAULT;
            };
        }
    }

    private final String iri;
    private final String name;
    private final Optional<String> template;
    private final List<String> graphTemplates;
    private final List<String> property;
    private final Optional<String> properties;
    private final List<String> includes;
    private final Optional<String> additionalTriples;
    private final Optional<String> unionTemplate;
    private final IncludeDatasetClause includeDatasetClause;
    private final IncludeNamespaces includeNamespaces;

    public static final class Builder {
        private String iri;
        private String name;
        private Optional<String> template = Optional.empty();
        private final ImmutableList.Builder<String> graphTemplates = ImmutableList.builder();
        private final ImmutableList.Builder<String> property = ImmutableList.builder();
        private Optional<String> properties = Optional.empty();
        private final ImmutableList.Builder<String> includes = ImmutableList.builder();
        private Optional<String> additionalTriples = Optional.empty();
        private Optional<String> unionTemplate = Optional.empty();
        private IncludeDatasetClause includeDatasetClause = IncludeDatasetClause.DEFAULT;
        private IncludeNamespaces includeNamespaces = IncludeNamespaces.DEFAULT;
        private Builder() {
        }

        public Builder iri(String iri) {
            this.iri = iri;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }


        public Builder template(Optional<String> template) {
            this.template = template;
            return this;
        }

        public Builder graphTemplates(List<String> graphTemplates) {
            if (graphTemplates == null || graphTemplates.isEmpty())
                return this;
            this.graphTemplates.addAll(graphTemplates);
            return this;
        }

        public Builder property(List<String> property) {
            if (property == null || property.isEmpty())
                return this;
            this.property.addAll(property);
            return this;
        }

        public Builder properties(Optional<String> properties) {
            this.properties = properties;
            return this;
        }

        public Builder includes(List<String> includes) {
            if (includes == null || includes.isEmpty())
                return this;

            this.includes.addAll(includes);
            return this;
        }

        public Builder additionalTriples(Optional<String> additionalTriples) {
            this.additionalTriples = additionalTriples;
            return this;
        }

        public Builder unionTemplate(Optional<String> unionTemplate) {
            this.unionTemplate = unionTemplate;
            return this;
        }

        public Builder includeDatasetClause(Optional<String> includeDatasetClause) {
            this.includeDatasetClause = includeDatasetClause
                    .map(IncludeDatasetClause::from)
                    .orElse(IncludeDatasetClause.DEFAULT);

            return this;
        }

        public Builder includeNamespaces(Optional<String> includeNamespaces) {
            this.includeNamespaces = includeNamespaces
                    .map(IncludeNamespaces::from)
                    .orElse(IncludeNamespaces.DEFAULT);

            return this;
        }
        public SpecialisedViewer build() {
            return new SpecialisedViewer(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private SpecialisedViewer(Builder b) {
        this.iri = b.iri;
        this.name = b.name;
        this.template = b.template;
        this.graphTemplates = b.graphTemplates.build();
        this.property = b.property.build();
        this.properties = b.properties;
        this.includes = b.includes.build();
        this.additionalTriples = b.additionalTriples;
        this.unionTemplate = b.unionTemplate;
        this.includeDatasetClause = b.includeDatasetClause;
        this.includeNamespaces = b.includeNamespaces;
    }

    @Override
    public String iri() {
        return iri;
    }

    @Override
    public String name() {
        return name;
    }

     @Override
    public Optional<String> template() {
        return template;
    }

    @Override
    public List<String> graphTemplates() {
        return graphTemplates;
    }

    @Override
    public List<String> property() {
        return property;
    }

    @Override
    public Optional<String> propertyChain() {
        return properties;
    }

    @Override
    public List<String> include() {
        return includes;
    }

    public Optional<String> additionalTriples() {
        return additionalTriples;
    }

    public Optional<String> unionTemplate() {
        return unionTemplate;
    }

    public IncludeDatasetClause includeDatasetClause() {
        return includeDatasetClause;
    }

    public IncludeNamespaces includeNamespaces() {
        return includeNamespaces;
    }
}
