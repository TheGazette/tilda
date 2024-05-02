package io.github.thegazette.tilda.core.renderer.simple.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.thegazette.tilda.core.util.URLs;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SimpleXMLWriter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void write(Map<String, Object> results, Writer io) {
        try {
            final var output = XMLOutputFactory.newInstance();
            final var writer = output.createXMLStreamWriter(io);

            var format = results.getOrDefault("format", "linked-data-api").toString();
            var version = results.getOrDefault("version", "0.2").toString();
            var data = data(results);
            //var href = data.get("_about").toString();
            startElement(writer, "result");
            attribute(writer, "format", format);
            attribute(writer, "version", version);
            //attribute(writer, "href", href);

            write(writer, data);


            endElement(writer);


            writer.flush();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }


    private void write(XMLStreamWriter writer, Map<String, Object> data) {
        for (var e : data.entrySet()) {
            if (e.getKey().equals("_about")) {
                var href = data.get("_about").toString();
                attribute(writer, "href", href);
            } else {
                write(writer, e.getKey(), e.getValue());
            }
        }
    }

    private void write(final XMLStreamWriter writer, final String label, final Object value) {
        startElement(writer, label);
        attribute(writer, "label", label); //Legislation does this

        if (value instanceof Map<?,?>) {
            write(writer, (Map<String, Object>) value);
        }
        else if (value instanceof List<?>) {
            for (var item : (List<Object>) value) {
                startElement(writer, "item");
                if (item instanceof Map map)
                    write(writer, (Map<String, Object>)map);
                else if (item.toString().startsWith("http://") || item.toString().startsWith("https://"))
                    attribute(writer, "href", item.toString());
                else
                    characters(writer, item.toString());
                endElement(writer);
            }
        } else {
            var val = value.toString();
            if (URLs.isURL(val)) {
                attribute(writer, "href", val);
            } else {
                characters(writer, value.toString());
            }
        }

        endElement(writer);
    }


    private Map<String, Object> data(Map<String, Object> results) {
        var data = results.get("results");
        return (Map<String, Object>) Objects.requireNonNull(data);
    }

    private static void startElement(XMLStreamWriter writer, String localName) {
        try {
            writer.writeStartElement(localName);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private static void characters(XMLStreamWriter writer, String localName) {
        try {
            writer.writeCharacters(localName);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private static void emptyElement(XMLStreamWriter writer, String localName) {
        try {
            writer.writeEmptyElement(localName);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private static void endElement(XMLStreamWriter writer) {
        try {
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private static void attribute(XMLStreamWriter writer, String localName, String value) {
        try {
            writer.writeAttribute(localName, value);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

}
