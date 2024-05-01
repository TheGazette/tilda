package uk.co.tso.tilda.core.renderer.xslt.links.versions.href;

import org.springframework.web.util.UriComponentsBuilder;

public interface SuffixBasedVersionHref extends VersionHref {
    SuffixBasedVersionHref SUFFIX_BASED_VERSION_HREF = (inputContext, viewer) -> {
        var b = UriComponentsBuilder.fromUriString(inputContext.request().uri().toString());
        b.port(-1);
        b.replaceQueryParam("_view", viewer.name());
        return b.toUriString();
    };
}
