<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <html>
            <body>
                <h2>Lista delle Persone</h2>
                <ul>
                    <xsl:for-each select="persone/persona">
                        <li>
                            <xsl:value-of select="concat(nome, ' ', cognome, ' ', eta)"/>
                            - ID: <xsl:value-of select="@id"/>
                        </li>
                    </xsl:for-each>
                </ul>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
