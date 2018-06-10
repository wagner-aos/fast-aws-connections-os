package br.com.fast.aws.connection.auth.teste;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Utils class for handling environment variables
 * 
 * @author Wagner Alves
 */
public class EnvironmentUtils {

    public static void setEnvironmentVariable(String key, String value) {

        Class<?> processEnvironment;
        try {
            processEnvironment = Class.forName("java.lang.ProcessEnvironment");
            Field unmodifiableMapField = getAccessibleField(processEnvironment, "theUnmodifiableEnvironment");
            Object unmodifiableMap = unmodifiableMapField.get(null);
            injectIntoUnmodifiableMap(key, value, unmodifiableMap);

            Field mapField = getAccessibleField(processEnvironment, "theEnvironment");
            @SuppressWarnings("unchecked")
            Map<String, String> map = (Map<String, String>) mapField.get(null);
            map.put(key, value);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

    }

    public static void unSetEnvironmentVariable(String key) {

        Class<?> processEnvironment;
        try {
            processEnvironment = Class.forName("java.lang.ProcessEnvironment");
            Field mapField = getAccessibleField(processEnvironment, "theEnvironment");
            @SuppressWarnings("unchecked")
            Map<String, String> map = (Map<String, String>) mapField.get(null);
            map.remove(key);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

    }

    private static Field getAccessibleField(Class<?> clazz, String fieldName)
            throws NoSuchFieldException {

        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    @SuppressWarnings("unchecked")
    private static void injectIntoUnmodifiableMap(String key, String value, Object map)
            throws ReflectiveOperationException {

        @SuppressWarnings("rawtypes")
        Class unmodifiableMap = Class.forName("java.util.Collections$UnmodifiableMap");
        Field field = getAccessibleField(unmodifiableMap, "m");
        Object obj = field.get(map);
        ((Map<String, String>) obj).put(key, value);
    }

    public static void printVariables() {
        ProcessBuilder pb = new ProcessBuilder("CMD.exe", "/C", "SET"); // SET prints out the environment variables
        pb.redirectErrorStream(true);
        @SuppressWarnings("unused")
        Map<String, String> env = pb.environment();

        try {
            Process process;
            process = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // SETTING
        // setEnvironmentVariable("AWS_ACCESS_KEY_ID", "xxxxxxxxxxx");
        // setEnvironmentVariable("AWS_SECRET_ACCESS_KEY", "YYYYYYYYYYYYYY");

        // UNSETTING
        // unSetEnvironmentVariable("AWS_ACCESS_KEY_ID");
        // unSetEnvironmentVariable("AWS_SECRET_ACCESS_KEY");

        printVariables();
    }

}
