package br.com.fast.aws.connection.dynamodb.pagination;

import com.amazonaws.services.dynamodbv2.document.ItemUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class LastKeyUtils {

    private static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * @param lastKey um base64 do encode da úlima chave
     * @return O mapa que representa a chave do registro no DynamoDb
     */
    public static Map<String, AttributeValue> decodeLastKey(String lastKey) {
        if (lastKey != null && !lastKey.isEmpty()) {
            byte[] lastKeyDecoded = Base64.getDecoder().decode(lastKey.getBytes(DEFAULT_CHARSET));
            TypeToken<Map<String, AttributeValue>> typeToken = new TypeToken<Map<String, AttributeValue>>(){};
            return new Gson().fromJson(new String(lastKeyDecoded, DEFAULT_CHARSET), typeToken.getType());
        }
        return null;
    }

    /**
     * @param lastKey o mapa que representa a chave do registro no DynamoDb
     * @return uma {@link String} em base64 com o conteúdo em json de uma chave
     */
    public static String encodeLastKey(Map<String, AttributeValue> lastKey) {
        byte[] lastKeyBytes = ItemUtils.toItem(lastKey).toJSON().getBytes(DEFAULT_CHARSET);
        return Base64.getEncoder().encodeToString(lastKeyBytes);
    }

}
