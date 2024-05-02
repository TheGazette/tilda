package io.github.thegazette.tilda.core.api.formatter;

import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;


import java.io.*;
import java.util.Collections;
import java.util.List;

public final class Xslt implements Formatter {
    private final String iri;
    private final String name;
    private final List<String> mimeTypes;
    private final String prefix;
    private final String stylesheet;
    private final List<MediaType> mediaTypes;
    private final MediaType mediaType;


    public Xslt(String iri, String name, List<String> mimeTypes, String prefix, String stylesheet, MediaType mediaType) {
        this.iri = iri;
        this.name = name;
        this.mimeTypes = mimeTypes;
        this.prefix = prefix;
        this.stylesheet = stylesheet;
        this.mediaTypes =  mimeTypes == null || mimeTypes.isEmpty()
                ? Collections.emptyList()
                : mimeTypes.stream().map(MediaType::parseMediaType).toList();
        this.mediaType = mediaType;
    }

    private static String loadStylesheet(final String stylesheet) {
        try(var file = new FileInputStream(ResourceUtils.getFile("classpath:" + stylesheet))) {
            return new String(file.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String iri() {
        return iri;
    }

    @Override
    public String name() {
        return name;
    }

    public List<String> mimeTypes() {
        return mimeTypes;
    }

    @Override
    public List<MediaType> mediaTypes() {
        return mediaTypes;
    }

    @Override
    public MediaType mediaType() {
        return mediaType;
    }

    public String stylesheet() {
        return stylesheet;
    }

    public String prefix() {
        return prefix;
    }
}
