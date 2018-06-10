package br.com.fast.aws.connection.commons.enumeration;

/**
 * @author Wagner.Alves
 */
public enum DateFormatEnum {

    YYYY_MM_DD("yyyy-MM-dd"),
    DD_MM_YYYY("dd-MM-yyyy"),
    HH_MM_SS("HH:mm:ss"),
    HH_MM_SS_SSS("HH:mm:ss.SSS"),
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    YYYY_MM_DD_HH_MM_SS_8601("yyyy-MM-dd'T'HH:mm:ssZ"),
    YYYY_MM_DD_HH_MM_SS_SSS("yyyy-MM-dd HH:mm:ss.SSS"),
    YYYY_MM_DD_HH_MM_SS_SSS_8601("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private String formato;

    DateFormatEnum(String formato) {
        this.formato = formato;
    }

    public String getFormato() {
        return formato;
    }

}
