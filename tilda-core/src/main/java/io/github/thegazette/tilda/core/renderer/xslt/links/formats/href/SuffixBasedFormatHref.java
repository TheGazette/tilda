package io.github.thegazette.tilda.core.renderer.xslt.links.formats.href;

import org.springframework.web.util.UriComponentsBuilder;

public interface SuffixBasedFormatHref extends FormatHref {
    SuffixBasedFormatHref SUFFIX_BASED_FORMAT_HREF = (inputContext, format) -> {
        final var req = inputContext.request();

        //req.headers().asHttpHeaders().forEach((k, vs) -> logger.warn("{}, {}", k, vs));

        //req.servletRequest().getHeaderNames().asIterator().forEachRemaining(s -> logger.warn("{}", s));

        /*
        For reasons I do not yet understand, trying to get X-TILDA-EFFECTIVE-REQUEST-URI
        via req.headers does not work, and we need to go into to the servlet request
         */

        //var effective = req.headers().firstHeader("X-TILDA-EFFECTIVE-REQUEST-URI");
        var effective = req.servletRequest().getHeader("X-TILDA-EFFECTIVE-REQUEST-URI");

        var reqUri = effective != null ? effective : req.uri().toString();

        var u = UriComponentsBuilder.fromHttpUrl(reqUri).build();
        var p = u.getPath();
        var l = req.requestPath().subPath(req.requestPath().elements().size() - 1);
        var s = l.value().split("\\.");
        if (s.length > 1) {
            var np =  p.replace("." + s[1], "." + format.name());
            return UriComponentsBuilder.fromHttpUrl(reqUri).port(-1).replacePath(np).toUriString();
        }

        return reqUri + "." + format.name();
    };
}
