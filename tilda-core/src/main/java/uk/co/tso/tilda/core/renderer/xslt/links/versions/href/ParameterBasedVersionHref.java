package uk.co.tso.tilda.core.renderer.xslt.links.versions.href;

public interface ParameterBasedVersionHref extends VersionHref {
    ParameterBasedVersionHref PARAMETER_BASED_VERSION_HREF = (inputContext, viewer) -> {
        return inputContext.request().uri().toString();
    };
}
