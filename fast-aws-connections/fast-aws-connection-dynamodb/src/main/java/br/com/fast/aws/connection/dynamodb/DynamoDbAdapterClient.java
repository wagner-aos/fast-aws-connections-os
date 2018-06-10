package br.com.fast.aws.connection.dynamodb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper.FailedBatch;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.ConsistentReads;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.TableNameOverride;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperTableModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.datamodeling.ScanResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.Select;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.fast.aws.connection.commons.enumeration.TipoECEnum;
import br.com.fast.aws.connection.dynamodb.pagination.LastKeyUtils;
import br.com.fast.aws.connection.dynamodb.pagination.PaginatedResult;

/**
 * @author Wagner Alves
 */
public class DynamoDbAdapterClient<T> {

    private static Integer DEFAULT_PAGINATION_SIZE = 7;

    private AmazonDynamoDB dbClient;
    private DynamoDBMapper dbMapper;
    private String tableName;

    /**
     * DynamoDbAdapterClient Constructor
     * 
     * @param dbClient
     * @param tableName
     */
    public DynamoDbAdapterClient(AmazonDynamoDB dbClient, String tableName) {
        this.dbClient = dbClient;
        this.tableName = tableName;
        init();
    }

    /**
     * Configure the DynamoDBMapper
     */
    private void init() {
        DynamoDBMapperConfig.Builder builder = new DynamoDBMapperConfig.Builder();
        builder.setTableNameOverride(new TableNameOverride(getTableName()));
        builder.setConsistentReads(ConsistentReads.CONSISTENT);
        dbMapper = new DynamoDBMapper(dbClient, builder.build());
    }

    /**
     * Saves the object given into DynamoDB, using the default configuration.
     * 
     * @param entity
     * @return
     */
    public T insert(T entity) {
        dbMapper.save(entity);
        return entity;
    }

    /**
     * Saves the objects given using one or more calls to the AmazonDynamoDB.batchWriteItem(BatchWriteItemRequest) API.
     * No version checks are performed, as required by the API.
     * This method ignores any SaveBehavior set on the mapper, and always behaves as if SaveBehavior.CLOBBER was
     * specified,
     * as the AmazonDynamoDB.batchWriteItem() request does not support updating existing items. *
     * This method fails to save the batch if the size of an individual object in the batch exceeds 400 KB.
     * For more information on batch restrictions see, http://docs.aws.amazon
     * .com/amazondynamodb/latest/APIReference/API_BatchWriteItem.html
     * 
     * @param obj
     * @return
     */
    public List<FailedBatch> batchInsert(T obj) {
        return dbMapper.batchSave(obj);
    }

    public List<FailedBatch> batchInsert(List<T> entities) {
        return dbMapper.batchSave(entities);
    }

    /**
     * Deletes the given object from its DynamoDB table using the default configuration.
     * 
     * @param entity
     */
    public void delete(T entity) {
        dbMapper.delete(entity);
    }

    /**
     * Delete all objects from a table.
     * 
     * @param entity
     */
    public void deleteAll(Class<T> entity) {
        batchDelete(findAll(entity));
    }

    /**
     * Delete a list of objects from a table.
     * 
     * @param entities
     */
    public void batchDelete(List<T> entities) {
        dbMapper.batchDelete(entities);
    }

    /**
     * Loads an object with the hash key given, using the default configuration.
     * 
     * @param entity
     * @param hashKey
     * @return
     */
    public T load(Class<T> entity, Object hashKey) {
        return dbMapper.load(entity, hashKey);
    }

    /**
     * Returns all entities with Scan.
     * 
     * @param entity
     * @return
     */
    public List<T> findAll(Class<T> entity) {
        DynamoDBScanExpression expression = new DynamoDBScanExpression()
                .withConsistentRead(false)
                .withSelect(Select.ALL_ATTRIBUTES);

        return dbMapper.scan(entity, expression);
    }

    /**
     * Scans through an Amazon DynamoDB table and returns the matching results as an unmodifiable list of instantiated
     * objects, using the default configuration.
     * 
     * @param entity
     * @param expression
     * @return
     */
    public List<T> scan(Class<T> entity, DynamoDBScanExpression expression) {
        return dbMapper.scan(entity, expression);
    }

    /**
     * Scans through an Amazon DynamoDB table and return all the list of objects without instantiation
     * 
     * @param scanRequest
     * @return
     */
    public List<Map<String, AttributeValue>> scanUntyped(ScanRequest scanRequest) {
        ScanResult result = dbClient.scan(scanRequest);
        return result.getItems();
    }

    /**
     * Polymorphic scan, scans through an Amazon DynamoDB table and return all the list of objects without
     * instantiation and converts to a specific type.
     * 
     * @param parentEntity
     * @param scanRequest
     * @return
     * @throws ClassNotFoundException
     */
    @SuppressWarnings({ "rawtypes" })
    public List scanPolymorphic(Class parentEntity, ScanRequest scanRequest) {

        Package packageName = parentEntity.getPackage();

        scanRequest.withTableName(tableName);
        ScanResult scanResultList = dbClient.scan(scanRequest);

        List<T> convertedList = new ArrayList<>();

        parseObjects(packageName, scanResultList);
        return convertedList;

    }

    /**
     * Polymorphic scan, scans through an Amazon DynamoDB table and return all the list of objects without
     * instantiation and converts to a specific type.
     * 
     * @param parentEntity
     * @param scanRequest
     * @return
     * @throws ClassNotFoundException
     */
    @SuppressWarnings({ "rawtypes" })
    public List scanPolymorphicPaginated(Class parentEntity, ScanRequest scanRequest) {
        List<T> convertedList = new ArrayList<>();

        Package packageName = parentEntity.getPackage();
        scanRequest.withTableName(tableName);

        do {
            ScanResult scanResultList = dbClient.scan(scanRequest);
            scanRequest.withExclusiveStartKey(scanResultList.getLastEvaluatedKey());
            convertedList.addAll(parseObjects(packageName, scanResultList));
        } while (scanRequest.getExclusiveStartKey() != null);

        return convertedList;
    }

    @SuppressWarnings("unchecked")
    private List<T> parseObjects(Package packageName, ScanResult scanResultList) {
        List<T> convertedList = new ArrayList<>();
        for (Map<String, AttributeValue> item : scanResultList.getItems()) {

            String className = packageName.getName().concat(".").concat(TipoECEnum.valueOf(item.get("tipoEC").getS()).getEntityName());
            Class<?> classType;
            try {
                classType = Class.forName(className);
                Object object = dbMapper.marshallIntoObject(classType, item);
                convertedList.add((T) object);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return convertedList;
    }

    /**
     * Scans through an Amazon DynamoDB table and returns a paginated list of matching results.
     * 
     * @param entity
     * @param expression
     * @return
     */
    public List<T> scanPaginated(Class<T> entity, DynamoDBScanExpression expression) {

        List<T> lista = new ArrayList<>();

        do {
            ScanResultPage<T> scanPage = dbMapper.scanPage(entity, expression);
            lista.addAll(scanPage.getResults());
            expression.setExclusiveStartKey(scanPage.getLastEvaluatedKey());
        } while (expression.getExclusiveStartKey() != null);

        return lista;
    }

    /**
     * Realiza um scan no DynamoDb, trazendo resultados paginados.
     *
     * @param clazz A classe do objeto mapeado
     * @param expression Scan expression do DynamoDb
     * @param lastKey O valor em base64 da chave retornada na última pesquisa
     * @param limit A quantidade de registros a serem retornados
     * @param <T> O tipo do objeto mapeado
     * @return {@link PaginatedResult} com os registros encontrados, e última chave no formato base64
     */
    public <T> PaginatedResult<List<T>> scanPaginated(Class<T> clazz, DynamoDBScanExpression expression, String lastKey, Integer limit) {
        limit = Optional.ofNullable(limit).orElse(DEFAULT_PAGINATION_SIZE);
        return this.doExecute(clazz, expression, lastKey, limit, this.scanFn());
    }

    /**
     * Scans through an Amazon DynamoDB table and returns a single page of matching results.
     * 
     * @param entity
     * @param expression
     * @return
     */
    public List<T> scanPage(Class<T> entity, DynamoDBScanExpression expression) {
        List<T> lista = new ArrayList<>();
        ScanResultPage<T> scanPage = dbMapper.scanPage(entity, expression);
        lista.addAll(scanPage.getResults());
        return lista;

    }

    /**
     * Scans through an Amazon DynamoDB table and returns a single page of matching results using a filter predicate.
     * 
     * @param entity
     * @param expression
     * @param predicate
     * @return
     */
    public List<T> scanPage(Class<T> entity, DynamoDBScanExpression expression, Predicate<T> predicate) {

        List<T> lista = new ArrayList<>();

        do {
            ScanResultPage<T> scanPage = dbMapper.scanPage(entity, expression);
            lista.addAll(scanPage.getResults().stream().filter(predicate).collect(Collectors.toList()));
            expression.setExclusiveStartKey(scanPage.getLastEvaluatedKey());
        } while (expression.getExclusiveStartKey() != null);

        return lista;
    }

    /**
     * Queries an Amazon DynamoDB table and returns the matching results as an unmodifiable list of instantiated
     * objects, using the default configuration.
     * 
     * @param entity
     * @param queryExpression
     * @return
     */
    public PaginatedQueryList<T> executeQuery(Class<T> entity, DynamoDBQueryExpression<T> queryExpression) {
        return dbMapper.query(entity, queryExpression);
    }

    /**
     * Query through an Amazon DynamoDB table and returns a paginated list of matching results.
     * 
     * @param entity
     * @param expression
     * @return
     */
    public List<T> queryPaginated(Class<T> entity, DynamoDBQueryExpression<T> expression) {

        List<T> lista = new ArrayList<>();

        do {
            QueryResultPage<T> queryPage = dbMapper.queryPage(entity, expression);
            lista.addAll(queryPage.getResults());
            expression.setExclusiveStartKey(queryPage.getLastEvaluatedKey());
        } while (expression.getExclusiveStartKey() != null);

        return lista;
    }

    /**
     * Realiza uma query no DynamoDb, trazendo resultados paginados.
     *
     * @param clazz A classe do objeto mapeado
     * @param expression Query expression do DynamoDb
     * @param lastKey O valor em base64 da chave retornada na última pesquisa
     * @param limit A quantidade de registros a serem retornados
     * @param <T> O tipo do objeto mapeado
     * @return {@link PaginatedResult} com os registros encontrados, e última chave no formato base64
     */
    public <T> PaginatedResult<List<T>> queryPaginated(Class<T> clazz, DynamoDBQueryExpression<T> expression, String lastKey, Integer limit) {
        limit = Optional.ofNullable(limit).orElse(DEFAULT_PAGINATION_SIZE);
        return this.doExecute(clazz, expression, lastKey, limit, this.queryFn());
    }

    /**
     * The CreateTable operation adds a new table to your account. In an AWS account, table names must be unique within
     * each region. That is, you can have two tables with same name if you create the tables in different regions.
     * 
     * @param tableName
     * @param entityClass
     */
    public void createTable(String tableName, Class<?> entityClass) {

        CreateTableRequest createTableRequest = dbMapper.generateCreateTableRequest(entityClass);
        createTableRequest.setTableName(tableName);
        createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(10L, 10L));

        // Seta o ProvisionedThroughput para todos os indexes da tabela
        List<GlobalSecondaryIndex> listaIndex = createTableRequest.getGlobalSecondaryIndexes();

        if (listaIndex != null) {
            for (GlobalSecondaryIndex index : listaIndex) {
                index.setProvisionedThroughput(new ProvisionedThroughput(10L, 10L));
                index.setProjection(new Projection().withProjectionType(ProjectionType.ALL));
            }
        }

        dbClient.createTable(createTableRequest);

        while (!isActiveTable(tableName)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

    /**
     * Describes a table and returns a Json Prettify
     * 
     * @param tableName
     * @return
     */
    public String describeTableToJSON(String tableName) {
        DescribeTableResult describeTable = dbClient.describeTable(tableName);
        TableDescription table = describeTable.getTable();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(table);
    }

    /**
     * Describes a table and returns a YAML
     * 
     * @param tableName
     * @return
     */
    public String describeTableToYAML(String tableName) {
        Yaml yaml = new Yaml();
        String json = describeTableToJSON(tableName);
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) yaml.load(json);
        return yaml.dump(map);
    }
    
    @SuppressWarnings("rawtypes")
   	public List queryPolymorphic(Class parentEntity, QueryRequest queryRequest) {

           Package packageName = parentEntity.getPackage();

           queryRequest.withTableName(tableName);
           QueryResult queryResultList = dbClient.query(queryRequest);

           return parseQueryObjects(packageName, queryResultList);

       }

    @SuppressWarnings("unchecked")
    private List<T> parseQueryObjects(Package packageName, QueryResult queryResultList) {
        List<T> convertedList = new ArrayList<>();
        for (Map<String, AttributeValue> item : queryResultList.getItems()) {

            String className = packageName.getName().concat(".").concat(TipoECEnum.valueOf(item.get("tipoEC").getS()).getEntityName());
            Class<?> classType;
            try {
                classType = Class.forName(className);
                Object object = dbMapper.marshallIntoObject(classType, item);
                convertedList.add((T) object);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return convertedList;
    }
    /**
     * The DeleteTable operation deletes a table and all of its items. After a DeleteTable request, the specified table
     * is in the DELETING state until DynamoDB completes the deletion. If the table is in the ACTIVE state, you can
     * delete it. If a table is in CREATING or UPDATING states, then DynamoDB returns a ResourceInUseException. If the
     * specified table does not exist, DynamoDB returns a ResourceNotFoundException. If table is already in the DELETING
     * state, no error is returned.
     * 
     * @param tableName
     * @param entityClass
     */
    public void deleteTable(String tableName, Class<?> entityClass) {
        if (tableExistsAndIsActive(tableName)) {
            DeleteTableRequest deleteTableRequest = dbMapper.generateDeleteTableRequest(entityClass);
            dbClient.deleteTable(deleteTableRequest);
        }
    }

    private boolean isActiveTable(String tableName) {
        try {
            DescribeTableResult describeTable = dbClient.describeTable(tableName);
            String tableStatus = describeTable.getTable().getTableStatus();
            return "ACTIVE".equals(tableStatus);
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * Verify if table exists and is active.
     * @param tableName the table name
     * @return true if table exists and is active in dynamod.
     */
    public boolean tableExistsAndIsActive(String tableName) {
        return tableExists(tableName) && isActiveTable(tableName);
    }
    
    private boolean tableExists(String tableName) {
        ListTablesResult listTableResult = dbClient.listTables();
        List<String> tableNames = listTableResult.getTableNames();

        return !tableNames.isEmpty() && tableNames.contains(tableName);
    }

    /**
     * Evaluates the specified scan expression and returns the count of matching items,
     * without returning any of the actual item data, using the default configuration.
     * 
     * @param entity
     * @param expression
     * @return
     */
    public int countScan(Class<T> entity, DynamoDBScanExpression expression) {
        return dbMapper.count(entity, expression);
    }

    /**
     * Evaluates the specified query expression and returns the count of matching items,
     * without returning any of the actual item data, using the default configuration.
     * 
     * @param entity
     * @param expression
     * @return
     */
    public int countQuery(Class<T> entity, DynamoDBQueryExpression<T> expression) {
        return dbMapper.count(entity, expression);
    }

    /**
     * Returns the table name
     * 
     * @return tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Exposes the dbMapper to be used at the client side, for operations specific to the client and
     * that are not possible in this class context
     * 
     * @return
     */
    public DynamoDBMapper getDbMapper() {
        return dbMapper;
    }

    /**
     * Realiza a execução genérica de uma busca no DynamoDb.
     * @param clazz A classe do objeto mapeado
     * @param expression Scan expression do DynamoDb
     * @param lastKey O valor em base64 da chave retornada na última pesquisa
     * @param limit A quantidade de registros a serem retornados
     * @param fn A função a ser aplicada, executando uma query ou um scan
     * @param <T> O tipo do objeto mapeado
     * @return {@link PaginatedResult} com os registros encontrados, e última chave no formato base64
     */
    private <T> PaginatedResult<List<T>> doExecute(Class<T> clazz, Object expression, String lastKey, Integer limit, BiFunction<Class<T>, Object, PaginatedList<T>> fn) {
        limit = Optional.ofNullable(limit).orElse(DEFAULT_PAGINATION_SIZE);
        LastKeyUtils.decodeLastKey(lastKey);

        PaginatedList<T> dynamoQueryResult = fn.apply(clazz, expression);
        List<T> paginatedResults = dynamoQueryResult.subList(0, limit);
        T lastItem = paginatedResults.get(paginatedResults.size() - 1);

        return transform(paginatedResults, getKeyFromItem(clazz, lastItem));
    }

    /**
     * Realiza o mapeamento dos registros encontrados para um
     * @param results Retorno de resultados do DynamoDb
     * @param lastEvaluatedKey Mapa com a chave do último registro encontrado
     * @param <T> O tipo do objeto mapeado
     */
    private <T> PaginatedResult<List<T>> transform(List<T> results, Map<String, AttributeValue> lastEvaluatedKey) {
        PaginatedResult<List<T>> result = new PaginatedResult<>();
        result.setResult(results);
        if (lastEvaluatedKey != null) {
            result.setLastKey(LastKeyUtils.encodeLastKey(lastEvaluatedKey));
        }
        return result;
    }

    /**
     * Obtém o mapa de {@link AttributeValue} de um objeto a partir dos metadados da tabela
     * @param clazz A classe do objeto mapeado
     * @param item O objeto do qual será obtida a chave
     * @param <T> O tipo do objeto mapeado
     */
    private <T> Map<String, AttributeValue> getKeyFromItem(Class<T> clazz, T item) {
        DynamoDBMapperTableModel<T> tableModel = dbMapper.getTableModel(clazz);
        return tableModel.convertKey(item);
    }

    /**
     * @return A função que realiza a execução de um Scan
     */
    private <T> BiFunction<Class<T>, Object, PaginatedList<T>> scanFn() {
        return (clazz, expression) -> {
            DynamoDBScanExpression scanExpression = (DynamoDBScanExpression) expression;
            scanExpression.setLimit(null);
            return dbMapper.scan(clazz, scanExpression);
        };
    }

    /**
     * @return A função que realiza a execução de uma Query
     */
    private <T> BiFunction<Class<T>, Object, PaginatedList<T>> queryFn() {
        return (clazz, expression) -> {
            DynamoDBQueryExpression<T> queryExpression = (DynamoDBQueryExpression<T>) expression;
            queryExpression.setLimit(null);
            return dbMapper.query(clazz, queryExpression);
        };
    }
}
