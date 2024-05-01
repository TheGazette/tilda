package uk.co.tso.tilda.web.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "tilda")
public class TildaProperties {

    private Map<String, APIProperties> apis;
    private Map<String, EndpointProperties> endpoints;
    private Map<String, FormatterProperties> formatters;
    private Map<String, ViewerProperties> viewers;
    private Map<String, SelectorProperties> selectors;

    private ServerProperties server;

    public APIProperties api(final String name) {
        if (apis == null || apis.get(name) == null)
            return new APIProperties();

        return apis.get(name);
    }

    public Map<String, APIProperties> getApis() {
        return apis;
    }

    public void setApis(Map<String, APIProperties> apis) {
        this.apis = apis;
    }

    public EndpointProperties endpoint(final String name) {
        if (endpoints == null || endpoints.get(name) == null)
            return new EndpointProperties();

        return endpoints.get(name);
    }

    public Map<String, EndpointProperties> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(Map<String, EndpointProperties> endpoints) {
        this.endpoints = endpoints;
    }

    public Map<String, FormatterProperties> getFormatters() {
        return formatters != null ? formatters : Collections.emptyMap();
    }

    public void setFormatters(Map<String, FormatterProperties> formatters) {
        this.formatters = formatters;
    }

    public ViewerProperties viewer(String name) {
        if (viewers == null || !viewers.containsKey(name))
            return new ViewerProperties();

        return viewers.get(name);
    }

    public Map<String, ViewerProperties> getViewers() {
        return viewers != null ? viewers : Collections.emptyMap();
    }

    public void setViewers(Map<String, ViewerProperties> viewers) {
        this.viewers = viewers;
    }

    public SelectorProperties selector(String name) {
        if ((selectors == null) || (selectors.get(name) == null))
                return new SelectorProperties();

        return selectors.get(name);
    }

    public Map<String, SelectorProperties> getSelectors() {
        return selectors != null ? selectors : Collections.emptyMap();
    }

    public void setSelectors(Map<String, SelectorProperties> selectors) {
        this.selectors = selectors;
    }

    public ServerProperties getServer() {
        return server != null ? server : new ServerProperties();
    }

    public void setServer(ServerProperties server) {
        this.server = server;
    }
}
