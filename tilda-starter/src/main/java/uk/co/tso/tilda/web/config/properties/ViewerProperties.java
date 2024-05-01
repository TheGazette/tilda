package uk.co.tso.tilda.web.config.properties;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ViewerProperties {
    private String template;
    private List<String> graphTemplates;
    private List<String> property;
    private String properties;
    private List<String> include;
    private String additionalTriples;
    private String unionTemplate;
    private String includeDatasetClause;
    private String includeNamespaces;

    public Optional<String> findTemplate() {
        return template == null || "".equals(template) ? Optional.empty() : Optional.of(template);
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<String> getProperty() {
        return property != null ? property : Collections.emptyList();
    }

    public void setProperty(List<String> property) {
        this.property = property;
    }

    public Optional<String> findProperties() {
        return properties == null || "".equals(properties) ? Optional.empty() : Optional.of(properties);
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public List<String> getInclude() {
        return include != null ? include : Collections.emptyList();
    }

    public void setInclude(List<String> include) {
        this.include = include;
    }

    public List<String> getGraphTemplates() {
        return graphTemplates != null ? graphTemplates : Collections.emptyList();
    }

    public void setGraphTemplates(List<String> graphTemplates) {
        this.graphTemplates = graphTemplates;
    }

    public String getAdditionalTriples() {
        return additionalTriples;
    }

    public void setAdditionalTriples(String additionalTriples) {
        this.additionalTriples = additionalTriples;
    }
    public Optional<String> findAdditionalTriples() {
        return additionalTriples == null || "".equals(additionalTriples) ? Optional.empty() : Optional.of(additionalTriples);
    }

    public String getUnionTemplate() {
        return unionTemplate;
    }

    public void setUnionTemplate(String unionTemplate) {
        this.unionTemplate = unionTemplate;
    }

    public Optional<String> findUnionTemplate() {
        return unionTemplate == null || "".equals(unionTemplate) ? Optional.empty() : Optional.of(unionTemplate);
    }

    public String includeDatasetClause() {
        return includeDatasetClause;
    }

    public void setIncludeDatasetClause(String includeDatasetClause) {
        this.includeDatasetClause = includeDatasetClause;
    }

    public Optional<String> findIncludeDatasetClause() {
        return includeDatasetClause == null || "".equals(includeDatasetClause) ? Optional.empty() : Optional.of(includeDatasetClause);
    }

    public String includeNamespaces() {
        return includeNamespaces;
    }

    public void setIncludeNamespaces(String includeNamespaces) {
        this.includeNamespaces = includeNamespaces;
    }

    public Optional<String> findIncludeNamespaces() {
        return includeNamespaces == null || "".equals(includeNamespaces) ? Optional.empty() : Optional.of(includeNamespaces);
    }
}
