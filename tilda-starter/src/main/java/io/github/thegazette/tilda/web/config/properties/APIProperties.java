package io.github.thegazette.tilda.web.config.properties;

import io.github.thegazette.tilda.core.util.Optionals;

import java.util.List;
import java.util.Optional;

public class APIProperties {
    private String sparqlEndpoint;
    private String base;
    private String contentNegotiation;
    private List<String> lang;
    private String maxPageSize;
    private String defaultPageSize;
    private String defaultViewer;

    public String getSparqlEndpoint() {
        return sparqlEndpoint;
    }

    public void setSparqlEndpoint(String sparqlEndpoint) {
        this.sparqlEndpoint = sparqlEndpoint;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getContentNegotiation() {
        return contentNegotiation;
    }

    public void setContentNegotiation(String contentNegotiation) {
        this.contentNegotiation = contentNegotiation;
    }

    public List<String> getLang() {
        return lang;
    }

    public void setLang(List<String> lang) {
        this.lang = lang;
    }

    public String getMaxPageSize() {
        return maxPageSize;
    }

    public void setMaxPageSize(String maxPageSize) {
        this.maxPageSize = maxPageSize;
    }

    public String getDefaultPageSize() {
        return defaultPageSize;
    }

    public void setDefaultPageSize(String defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

    public String getDefaultViewer() {
        return defaultViewer;
    }

    public void setDefaultViewer(String defaultViewer) {
        this.defaultViewer = defaultViewer;
    }

    public Optional<String> findSparqlEndpoint() {
        return Optionals.ofString(sparqlEndpoint);
    }

    public Optional<String> findBase() {
        return Optionals.ofString(base);
    }

    public Optional<String> findContentNegotiation() {
        return Optionals.ofString(contentNegotiation);
    }

    public Optional<String> findDefaultPageSize() {
        return Optionals.ofString(defaultPageSize);
    }

    public Optional<String> findDefaultViewer() {
        return Optionals.ofString(defaultViewer);
    }

    public Optional<String> findMaxPageSize() {
        return Optionals.ofString(maxPageSize);
    }
}
