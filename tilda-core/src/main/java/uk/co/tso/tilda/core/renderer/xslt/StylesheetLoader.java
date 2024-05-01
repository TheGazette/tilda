package uk.co.tso.tilda.core.renderer.xslt;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

final class StylesheetLoader implements Function<String, byte[]> {

    private final String prefix;

    private StylesheetLoader(String prefix) {
        this.prefix = prefix;
    }

    static StylesheetLoader from(final String prefix) {
        return new StylesheetLoader(prefix);
    }


    @Override
    public byte[] apply(String stylesheet) {
        try (InputStream resource = new ClassPathResource(prefix + stylesheet).getInputStream()) {
            var loaded = new String(resource.readAllBytes());
            return loaded.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
