<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0">
    <xsl:param name="label"/>
    <xsl:template match="/">
        <result format="{/result/@format}" version="{/result/@version}"
            href="{/result/@href}">
            <label><xsl:value-of select="$label"/></label>
            <primaryTopic href="{/result//primaryTopic/@href}">
               <hasEntry>
                   <xsl:for-each select="/result//hasEntry/item">
                    <item href="{./@href}">
                        <xsl:if test=".//hasSurname">
                            <hasSurname>
                                <xsl:copy-of select=".//hasSurname/@*"/>
                            </hasSurname>
                        </xsl:if>
                        <xsl:copy-of select=".//hasForenames"/>
                    </item>
                   </xsl:for-each>
               </hasEntry>
            </primaryTopic>
        </result>
    </xsl:template>
</xsl:stylesheet>