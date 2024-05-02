package io.github.thegazette.tilda.web.config.properties;

import java.util.List;
import java.util.Optional;

public class FormatterProperties {
    private List<String> mimeTypes;
    private String stylesheet;


    public Optional<List<String>> findMimeTypes() {
        return mimeTypes == null || mimeTypes.isEmpty() ? Optional.empty() : Optional.of(mimeTypes);
    }

    public boolean isStylesheetPresent() {
        return !(stylesheet == null || stylesheet.isEmpty());
    }

    public List<String> getMimeTypes() {
        return mimeTypes;
    }

    public void setMimeTypes(List<String> mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    public String getStylesheet() {
        return stylesheet;
    }

    public void setStylesheet(String stylesheet) {
        this.stylesheet = stylesheet;
    }
}
