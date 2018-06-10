package br.com.fast.aws.connection.commons.interfaces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.fast.aws.connection.commons.enumeration.DateFormatEnum;

/**
 * @author Wagner.Alves
 */
public interface JSONConvertable<T> {

    /**
     * Converts object to json without NULL attributes
     * 
     * @return String json
     */
    public default String toJSON() {
        Gson gson = new GsonBuilder().setDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_SSS.getFormato()).create();
        return gson.toJson(this);
    }

    /**
     * Converts object to json without NULL attributes with ISO 8601 DateFormat.
     * 
     * @return String json
     */
    public default String toJSONWith8601DateFormat() {
        Gson gson = new GsonBuilder().setDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_SSS_8601.getFormato()).create();
        return gson.toJson(this);
    }

    /**
     * Converts object to json with NULL attributes
     * 
     * @return String json
     */
    public default String toJSONWithNullAttributes() {
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_SSS.getFormato()).create();
        return gson.toJson(this);
    }

    /**
     * Converts object to json with NULL attributes with ISO 8601 DateFormat.
     * 
     * @return String json
     */
    public default String toJSONWithNullAttributesAnd8601DateFormat() {
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_SSS_8601.getFormato()).create();
        return gson.toJson(this);
    }

    /**
     * Create an object from a json string
     * 
     * @param json
     * @param dtoClass
     * @return Object
     */
    public default T createFromJSON(String json, Class<T> dtoClass) {
        Gson gson = new GsonBuilder().setDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_SSS.getFormato()).create();
        return gson.fromJson(json, dtoClass);
    }

    /**
     * Create an object from a json string with ISO 8601 DateFormat.
     * 
     * @param json
     * @param dtoClass
     * @return Object
     */
    public default T createFromJSONwith8601DateFormat(String json, Class<T> dtoClass) {
        Gson gson = new GsonBuilder().setDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_SSS_8601.getFormato()).create();
        return gson.fromJson(json, dtoClass);
    }

    /**
     * Static method create an object from a json string
     * 
     * @param json
     * @param dtoClass
     * @return Object
     */
    public static <T> T createFromJsonStatic(String json, Class<T> dtoClass) {
        Gson gson = new GsonBuilder().setDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_SSS.getFormato()).create();
        return gson.fromJson(json, dtoClass);
    }

    /**
     * Static method create an object from a json string
     * 
     * @param json
     * @param dtoClass
     * @param formatter
     * @return
     */
    public static <T> T createFromJsonStaticWithDateFormatter(String json, Class<T> dtoClass, DateFormatEnum formatter) {
        Gson gson = new GsonBuilder().setDateFormat(formatter.getFormato()).create();
        return gson.fromJson(json, dtoClass);
    }

    /**
     * Static method create an object from a json string with ISO 8601 DateFormat.
     * 
     * @param json
     * @param dtoClass
     * @return Object
     */
    public static <T> T createFromJsonStaticWith8601DateFormat(String json, Class<T> dtoClass) {
        Gson gson = new GsonBuilder().setDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_SSS_8601.getFormato()).create();
        return gson.fromJson(json, dtoClass);
    }

}
