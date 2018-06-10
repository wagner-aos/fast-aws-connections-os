package br.com.fast.aws.connection.dynamodb.teste.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;

import br.com.fast.aws.connection.dynamodb.teste.entity.CadastroTecnicoTesteEntity;

public class TableCreator {

    private static String awsAccessKey = "AKIAIR2WH2TKMX4ODDBQ";
    private static String awsSecretKey = "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";
    private static String awsRegion = "us-east-1";
    private static boolean useEndpoint = false;
    private static String host = "dynamodb";
    private static Integer port = 8000;
    private static String tableName = "CadastroTecnico-saida-dev";

    public static void main(String[] args) {

        if (!isAtivoGestaoIdentificador(getAmazonDynamoDBClient())) {

            AmazonDynamoDB dbClient = getAmazonDynamoDBClient();

            DynamoDBMapper dbMapper = new DynamoDBMapper(dbClient);
            CreateTableRequest createTableRequest = dbMapper.generateCreateTableRequest(CadastroTecnicoTesteEntity.class);
            createTableRequest.setTableName(tableName);
            createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(10L, 10L));

            // Seta o ProvisionedThroughput para todos os indexes da tabela
            /*  List<GlobalSecondaryIndex> listaIndex = createTableRequest.getGlobalSecondaryIndexes();
            for (GlobalSecondaryIndex index : listaIndex) {
                index.setProvisionedThroughput(new ProvisionedThroughput(10L, 10L));
                index.setProjection(new Projection().withProjectionType(ProjectionType.ALL));
            }*/

            dbClient.createTable(createTableRequest);

            while (!isAtivoGestaoIdentificador(getAmazonDynamoDBClient())) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

    private static boolean isAtivoGestaoIdentificador(AmazonDynamoDB dbClient) {
        try {
            DescribeTableResult describeTable = dbClient.describeTable(tableName);
            String tableStatus = describeTable.getTable().getTableStatus();
            return "ACTIVE".equals(tableStatus);
        } catch (Exception e) {
            return false;
        }
    }

    public static AmazonDynamoDB getAmazonDynamoDBClient() {

        AmazonDynamoDB dbClient;

        if (useEndpoint) {
            String dynamoEndpoint = "http://".concat(host).concat(":").concat(port.toString());

            AWSCredentials credentials = new BasicAWSCredentials("x", "x");
            AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
            EndpointConfiguration endpointConfiguration = new EndpointConfiguration(dynamoEndpoint, "");

            dbClient = AmazonDynamoDBClientBuilder.standard()
                    .withCredentials(credentialsProvider)
                    .withEndpointConfiguration(endpointConfiguration)
                    .build();
        } else {

            AWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
            AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);

            dbClient = AmazonDynamoDBClientBuilder.standard()
                    .withCredentials(credentialsProvider)
                    .withRegion(awsRegion)
                    .build();
        }

        return dbClient;
    }

}
