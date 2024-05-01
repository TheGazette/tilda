package uk.co.tso.tilda.web.config.properties;

public class ServerProperties {
    public static class XslProperties {
        private String prefix = "stylesheets/";

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }
    }

    private XslProperties xsl = new XslProperties();


    public XslProperties getXsl() {
        return xsl;
    }

    public void setXsl(XslProperties xsl) {
        this.xsl = xsl;
    }
}
