package br.com.fast.aws.connection.dynamodb.pagination;

/**
 * Objeto que encapsula o resultado de uma paginação no DynamoDb
 * @param <T> o tipo do objeto de retorno
 */
public class PaginatedResult<T> {

    private T result;

    private String lastKey;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getLastKey() {
        return lastKey;
    }

    public void setLastKey(String lastKey) {
        this.lastKey = lastKey;
    }

}
