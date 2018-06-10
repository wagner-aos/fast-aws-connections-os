package br.com.fast.aws.connection.commons.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Auxiliar class fro parsing JSON Objects
 * 
 * @author Wagner Alves
 */
public class JSONParser {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";

    private JSONParser() {
    }

    /**
     * Creates the object list from json list.
     *
     * @param <T>
     *            the generic type
     * @param jsonList
     *            the json list
     * @param clazz
     *            the clazz
     * @return the list
     */
    public static <T> List<T> createFromJsonList(List<String> jsonList, Class<T> clazz) {
        List<T> objList = new ArrayList<>();
        jsonList.forEach(item -> objList.add((T) createFromJSON(item, clazz)));
        return objList;
    }

    /**
     * Creates the object list from json array.
     *
     * @param <T>
     *            the generic type
     * @param jsonArray
     *            the json array
     * @param clazz
     *            the clazz
     * @return the list
     */
    public static <T extends Object> List<T> createFromJsonArray(String jsonArray, Class<T> clazz) {
        Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
        Type type = new ListParameterizedType(clazz);
        return gson.fromJson(jsonArray, type);
    }

    private static class ListParameterizedType implements ParameterizedType {

        private Type type;

        private ListParameterizedType(Type type) {
            this.type = type;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.reflect.ParameterizedType#getActualTypeArguments()
         */
        @Override
        public Type[] getActualTypeArguments() {
            return new Type[] { type };
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.reflect.ParameterizedType#getRawType()
         */
        @Override
        public Type getRawType() {
            return ArrayList.class;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.reflect.ParameterizedType#getOwnerType()
         */
        @Override
        public Type getOwnerType() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((type == null) ? 0 : type.hashCode());
            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ListParameterizedType other = (ListParameterizedType) obj;
            if (type == null) {
                if (other.type != null)
                    return false;
            } else if (!type.equals(other.type))
                return false;
            return true;
        }
    }

    public static <T> T createFromJSON(String json, Class<T> dtoClass) {
        Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
        return gson.fromJson(json, dtoClass);
    }

    /*
     * From object to json.
     *
     * @param obj
     * the obj
     * 
     * @return the string
     * 
     * @see com.google.gson.Gson#toJson(Object)
     */
    public static String toJSON(Object obj) {
        Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
        return gson.toJson(obj);
    }

    public static String toJSONPretty(Object obj) {
        Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).setPrettyPrinting().create();
        return gson.toJson(obj);
    }

    public static String toYAML(Object obj) {
        Yaml yaml = new Yaml();

        String json = toJSONPretty(obj);
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) yaml.load(json);
        return yaml.dumpAsMap(map);
    }

    public static String toYAML(String json) {
        Yaml yaml = new Yaml();

        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) yaml.load(json);
        return yaml.dumpAsMap(map);
    }

}
