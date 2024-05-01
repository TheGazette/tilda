package uk.co.tso.tilda.web.config.properties;

import uk.co.tso.tilda.core.util.Optionals;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class EndpointProperties {
    String uriTemplate;
    String comment;
    String exampleRequestPath;
    String defaultPageSize;
    List<String> lang;
    List<String> viewers;
    List<String> formatters;

    public String defaultViewer() {
        return defaultViewer;
    }

    public EndpointProperties setDefaultViewer(String defaultViewer) {
        this.defaultViewer = defaultViewer;
        return this;
    }

    String defaultViewer;
    String defaultFormatter;
    String itemTemplate;
    String selector;


    public String getUriTemplate() {
        return uriTemplate;
    }
    public void setUriTemplate(String uriTemplate) {
        this.uriTemplate = uriTemplate;
    }

    public String getComment() {return comment;}
    public void setComment(String comment) {this.comment = comment;}

    public String getExampleRequestPath() {return exampleRequestPath;}
    public void setExampleRequestPath(String exampleRequestPath) {this.exampleRequestPath = exampleRequestPath;}

    public List<String> getLang() {return lang != null ? lang : Collections.emptyList();}
    public void setLang(List<String> lang) {this.lang = lang;}

    public List<String> getViewers() {return viewers != null ? viewers : Collections.emptyList();}
    public void setViewers(List<String> viewers) {this.viewers = viewers;}

    public List<String> getFormatters() {return formatters != null ? formatters : Collections.emptyList();}
    public void setFormatters(List<String> formatters) {this.formatters = formatters;}

    public String getDefaultFormatter() {return defaultFormatter;}
    public void setDefaultFormatter(String defaultFormatter) {this.defaultFormatter = defaultFormatter;}

    public String getItemTemplate() {return itemTemplate;}
    public void setItemTemplate(String itemTemplate) {this.itemTemplate = itemTemplate;}

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public Optional<String> findUriTemplate() {
        return Optionals.ofString(uriTemplate);
    }

    public Optional<String> findComment() {
        return Optionals.ofString(comment);
    }

    public Optional<String> findExampleRequestPath() {
        return Optionals.ofString(exampleRequestPath);
    }

    public Optional<String> findDefaultViewer() {
        return Optionals.ofString(defaultViewer);
    }

    public Optional<String> findDefaultFormatter() {
        return Optionals.ofString(defaultFormatter);
    }
    public Optional<String> findItemTemplate() {
        return Optionals.ofString(itemTemplate);
    }

    public Optional<String> findSelector() {
        return Optionals.ofString(selector);
    }

    public Optional<String> findDefaultPageSize() {
        return Optionals.ofString(defaultPageSize);
    }
}
