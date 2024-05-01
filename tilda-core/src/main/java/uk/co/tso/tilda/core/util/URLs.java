package uk.co.tso.tilda.core.util;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public interface URLs {
    static boolean isURL(String value) {
        try {
            var url = new URL(value);
            url.toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException ignored) {
        }
        return false;
    }
}
