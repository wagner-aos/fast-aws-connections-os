| Data | Autor | e-mail | Versao |
| --- | --- | --- | --- |
| 10/02/2017 | Wagner Alves (aka Barão) | wagner.aos.sa@gmail.com | 2.10.73 | 

# Fast AWS Connections API

É uma API de adapters desenvolvida para integração com alguns serviços da AWS [Amazon AWS Cloud](https://aws.amazon.com) (DynamoDB, SQS, SNS, Kinesis, S3, Lambda, Redshift, PinPoint) e HornetQ, Redis.

## O que a API fornece:

> Fornece conexões e abstrações para os serviços citados acima, centralizando todo codigo de infraestrutura e facilitando a interação com os recursos e/ou serviços.

## INFORMAÇÕES IMPORTANTES:

> AWS Credentials: NÃO é obrigatório e nem coerente passar o VALOR das chaves de acesso da AWS no arquivo de propriedades 'application.properties' do Spring ou em qualquer outro arquivo de configuração, porem deve-se se manter as chaves em branco para garantir uma possível necessidade de se passar as chaves por properties para testes.

> Modulo Auth: As conexões estão configuradas com um provider para pegar o valor diretamente das variáveis de ambiente OU do arquivos 'credentials' e 'config' que fica localizado na pasta: usuario/.aws/ (quando se tem o [AWS CLI](http://docs.aws.amazon.com/pt_br/cli/latest/userguide/cli-chap-getting-started.html) configurado na máquina).

> Spring: Os exemplos abaixo foram feitos utilizando Spring, mas podem ser utilizados tranquilamente com outras implementações. Ex: AWS Lambdas em Java, EJB, REST API, etc...

> DynamoDB: necessita de uma definição de Entidade para que seja passada para o cliente que por sua vez utiliza DbMapper.

> Esse versão foi feita para distribuição Open Source.

## Exemplo do arquivo 'credentials' em: usuario/.aws/credentials

```
[default]
aws_access_key_id = AKIAJ2TPF...
aws_secret_access_key = fjGtNv3Jidr9dYzS7...
region = us-east-1

```

## Exemplo do arquivo 'config' em: usuario/.aws/config 
Quando se utiliza credenciais por Role.

```
[default]
output = json
region = us-east-1
[profile dev]
output = json
role_arn = arn:aws:iam::{{ACCOUNT_ID}}:role/IamRoleAdmin
source_profile = default
region = us-east-1
[profile hml]
output = json
role_arn = arn:aws:iam::{{ACCOUNT_ID}}:role/IamRoleAdmin
source_profile = default
region = us-east-1
[profile ppd]
output = json
role_arn = arn:aws:iam::{{ACCOUNT_ID}}:role/IamRoleInfraRO
source_profile = default
region = us-east-1

```
## Ordem de obtenção de credenciais AWS (Modulo Auth):

> O Modulo Auth do Fast Connections obtem credencial AWS na seguinte ordem:

```
 1-Propriedades passadas pelo ClientConfiguration 'application.properties'
 2-Variáveis de Ambiente
 3-Roles configuradas por profile nos arquivos .aws/credentials e .aws/config
 4-Static Profile, por profile do arquivo .aws/credentials

```

## Configurar variáveis de ambiente para DEV (OPCIONAL):

> Linux, macOS, or Unix
```
$ export AWS_ACCESS_KEY_ID=AKIAJ2TPF...

$ export AWS_SECRET_ACCESS_KEY=fjGtNv3Jidr9dYzS7...

$ export AWS_REGION=us-west-1
```

> Windows
```
set AWS_ACCESS_KEY_ID=AKIAJ2TPF...

set AWS_SECRET_ACCESS_KEY=fjGtNv3Jidr9dYzS7...

set AWS_REGION=us-west-1
```

## Links úteis:

[Variáveis de ambiente](http://docs.aws.amazon.com/pt_br/cli/latest/userguide/cli-environment.html)

[Arquivos de Configuração de Credencial](http://docs.aws.amazon.com/pt_br/cli/latest/userguide/cli-config-files.html)


## Utilização nos serviços (exemplo com Spring):

Exemplo AWS DynamoDB:

Inserindo dependencia:
 
> 1- No pom.xml parent do microserviço, seção 'properties' insira:

```
<properties> 
  <!-- Fast AWS Connections -->
  <fast-aws-connection.version>2.10.73</fast-aws-connection.version>
  ...
  ...
</properties> 
```

> 2 - Na seção 'dependencies' insira:

```
<dependencies>
    <!-- AWS DynamoDB-->
    <dependency>
        <groupId>br.com.fast.aws.connections.dynamodb</groupId>
        <artifactId>fast-aws-connection-dynamodb</artifactId>
        <version>${fast-aws-connection.version}</version>
    </dependency> 
</dependencies>    
```

## Exemplo de Adapters:

> ATENÇÃO: Chaves de propriedade ".endpoint.use" no arquivo 'application.properties'
do Spring.

```
endpoint.use=false - conexão AWS
endpoint.use=true  - conexão Local (Utilizando Docker)

```

Properties:

```
aws.region=us-east-1
aws.profile=dev

aws.dynamodb.endpoint.use=false
aws.dynamodb.endpoint.host=dynamodb
aws.dynamodb.endpoint.port=8000
aws.dynamodb.table.name=NomeDaTabela

```

# DynamoDB

Configuracao:

```
@Configuration
public class DynamoDbConfiguration {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.profile}")
    private String awsProfile;

    @Value("${aws.dynamodb.endpoint.use}")
    private boolean useEndpoint;

    @Value("${aws.dynamodb.endpoint.host}")
    private String dynamoEndpointHost;

    @Value("${aws.dynamodb.endpoint.port}")
    private Integer dynamoEndpointPort;

    @Value("${aws.dynamodb.table.name}")
    private String tableName;

    @Bean(name = "passagemEntity")
    public DynamoDbAdapterClient<PassagemEntity> amazonDynamoDBClient() {
        return new DynamoDbAdapterConfiguration<PassagemEntity>()
                .withAwsRegion(awsRegion)
                .withAwsProfile(awsProfile)
                .withHost(dynamoEndpointHost)
                .withPort(dynamoEndpointPort)
                .withTableName(tableName)
                .withUseEndpoint(useEndpoint)
                .withAmazonDynamoDBClient();
    }

}
```

Caso queira criar a tabela adicione o @Bean abaixo na sua classe de @Configuration.

```
    @Bean
    public Table dynamoTableCreate(@Qualifier("passagemEntity") DynamoDbAdapterClient<PassagemEntity> client) {
        client.createTable(tableName, PassagemEntity.class);
        return null;
    }
```

Adapter:

```
@Component
public class DynamoDbPassagemHandler{

    @Autowired
    @Qualifier(value = "passagemEntity")
    private DynamoDbAdapterClient<PassagemEntity> dbClient;

    @Override
    public String inserirMensagemProcessada(PassagemEntity entity) {
        String hashKey;
        Try{ 
            hashKey = (String) client.insert(entity);
        } catch (Exception e) {
            log.error("Erro: ", e);
            throw e;
        }
        return hashKey;
    }

}
   
```

Adapter utilizando DynamoDB Query:

```
@Component
public class DynamoDbPassagemHandler{

    @Autowired
    @Qualifier(value = "gestaoIdentificadorEntity")
    private DynamoDbAdapterClient<GestaoIdentificadorEntity> dbClient;

    @Override
    public List<GestaoIdentificadorEntity> buscaPeloNumeroDaPlaca(String numeroPlaca) {
        
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":placa", new AttributeValue().withS(numeroPlaca));

        DynamoDBQueryExpression<GestaoIdentificadorEntity> queryExpression = new DynamoDBQueryExpression<GestaoIdentificadorEntity>()
                .withKeyConditionExpression("placa = :placa")
                .withIndexName("_GIPlacaIndex")
                .withConsistentRead(false)
                .withScanIndexForward(false)
                .withExpressionAttributeValues(eav)
                .withSelect(Select.ALL_ATTRIBUTES);

        @SuppressWarnings({ "unchecked", "rawtypes" })
        PaginatedQueryList resultList = client.executeQuery(GestaoIdentificadorEntity.class, queryExpression);

        return resultList;

    }
}
   
```

# AWS Lambda

Adapter para invocação síncrona direta para lambdas por meio de request payloads.

Com a lambda integrada via proxy, fazemos mapeamento do request para o evento de entrada 
da lambda da seguinte forma:

> Possíveis Atributos de Payload:
```
{
    "resource": "Resource path",
    "path": "Path parameter",
    "httpMethod": "Incoming request's method name"
    "headers": {Incoming request headers}
    "queryStringParameters": {query string parameters }
    "pathParameters":  {path parameters}
    "stageVariables": {Applicable stage variables}
    "requestContext": {Request context, including authorizer-returned key-value pairs}
    "body": "A JSON string of the request payload."
    "isBase64Encoded": "A boolean flag to indicate if the applicable request payload is Base64-encode"
}
```

> Exemplo de Payload:

```
{
    "pathParameters": {
        "estado": "SP"
    }, 
    "httpMethod": "GET"
}

```

Configuracao:

```
@Configuration
public class AWSLambdaConfiguration {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.profile}")
    private String awsProfile;

    @Value("${aws.access.key}")
    private String awsAccessKey;
   
    @Value("${aws.secret.key}")
    private String awsSecretKey;

    @Value("${aws.dynamodb.endpoint.use}")
    private boolean useEndpoint;

    @Bean
    public LambdaAdapterClient lambdaAdapterClient() {
        return new new LambdaAdapterConfiguration()
                .withAwsProfile(awsProfile)
                .withAwsRegion(awsRegion)
                .withAwsAccessKey(awsAccessKey)
                .withAwsSecretKey(awsSecretKey)
                .withUseEndpoint(useEndpoint)
                .withAWSLambdaClient();
    }
}
```

Adapter:

```
@Component
public class LambdaHandler{

    @Autowired
    private LambdaAdapterClient lambdaClient;

    @Override
    public String consultarCidadesPorEstado(String estado) throws Exception{
        
        String environment = "dev"
        
        functionName = "api-cep-endereco-" + environment + "-cidadesPorEstado";
        jsonPayload = "{\"pathParameters\": {\"estado\": "+ estado + "}, \"httpMethod\": \"GET\"}";

        Try{ 
            String json = client.invoke(functionName, jsonPayload);
            return json;
        } catch (Exception e) {
            log.error("Erro: ", e);
            throw e;
        }
    }

}
   
```


# JSONConvertable

A biblioteca fast-aws-connections fornece uma interface JSONConvertable que contem um comportamento de conversão Object/JSON utilizando Google GSON,
portanto é essencial que os DTOs utilizados nos servicos implementem essa interface:

Para utiliza-la adicione a dependencia no pom.xml. 
> Obs: todos os modulos do Fast AWS Connections já possuem a dependência abaixo!

```
<dependencies>
    <dependency>
        <groupId>br.com.fast.aws.connections.commons</groupId>
        <artifactId>fast-aws-connection-commons</artifactId>
        <version>${fast-aws-connection.version}</version>
    </dependency> 
</dependencies>
```

Interface JSONConvertable:
```
public interface JSONConvertable<T> {

    public default String toJSON() {
        Gson gson = new GsonBuilder().setDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_SSS.getFormato()).create();
        return gson.toJson(this);
    }

    public default String toJSONWithNullAttributes() {
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_SSS.getFormato()).create();
        return gson.toJson(this);
    }

    public default T createFromJSON(String json, Class<T> dtoClass) {
        Gson gson = new GsonBuilder().setDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_SSS.getFormato()).create();
        return gson.fromJson(json, dtoClass);
    }

    public static <T> T createFromJsonStatic(String json, Class<T> dtoClass) {
        Gson gson = new GsonBuilder().setDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_SSS.getFormato()).create();
        return gson.fromJson(json, dtoClass);
    }

}
```

Conversao DTO para JSON:
```
String passagemDtoJson = new PassagemDTO().toJSON();
```

Conversao JSON para DTO:

com instancia auxiliar:
```
PassagemDTO dto = new PassagemDTO().createFromJSON(json, PassagemDTO.class);
```
ou de forma estática:
```
PassagemDTO dto = JSONConvertable.createFromJsonStatic(json, PassagemDTO.class);
```

Exemplo de DTO com a interface:
```
public class PassagemProcessadaDTO implements JSONConvertable<PassagemProcessadaDTO>{

    private String atividade;
    private Integer idContaEC;
    private String idTransacao;
    private Long idTransacaoOrigem;

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }

    public Integer getIdContaEC() {
        return idContaEC;
    }

    public void setIdContaEC(Integer idContaEC) {
        this.idContaEC = idContaEC;
    }

    public String getIdTransacao() {
        return idTransacao;
    }

    ...
}

```

# DateFormatter
Classe utilitária com vários metodos estáticos de formatação de data, a maioria com UTC.

Exemplos:
```
String format = DateFormatter.format(date, DateFormatEnum.YYYY_MM_DD_HH_MM_SS_SSS);
Saida: 2017-08-17 19:32:19.498

String format = DateFormatter.formatUTC(date, "dd/MM/YYYY HH:mm:ss:SSS");
Saida: 17/08/2017 19:32:19:498

String formatZoneOff = DateFormatter.formatZoneOffUTC(date);
Saida: Quinta-feira, 17 de Agosto de 2017

String formatShort = DateFormatter.formatShortUTC(date);
Saida: "17/08/17"

String formatLong = DateFormatter.formatLongUTC(date);
Saida: "17 de Agosto de 2017"

```

# SQS

Configuration:
```
@Configuration
@EnableJms
public class SQSConfiguration {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.profile}")
    private String awsProfile;

    @Value("${aws.access.key}")
    private String awsAccessKey;
   
    @Value("${aws.secret.key}")
    private String awsSecretKey;

    @Value("${aws.sqs.endpoint.use}")
    private boolean sqsUseEndpoint;

    @Value("${aws.sqs.endpoint.host}")
    private String sqsHost;

    @Value("${aws.sqs.endpoint.port}")
    private Integer sqsPort;

    @Value("${aws.sqs.queue.inbound.name}")
    private String sqsQueueInbound;

    @Value("${aws.sqs.queue.outbound.name}")
    private String sqsQueueOutbound;

    @Value("${aws.sqs.queue.concurrency}")
    private String concurrency;

    @Value("${aws.sqs.queue.number.messages.receive}")
    private String numberOfMessagesToReceive;

    @Value("${aws.sqs.queue.visibility.timeout}")
    private String visibilityTimeout;

    @Value("${aws.sqs.queue.wait.time.seconds}")
    private String waitTimeSeconds;


    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        return new SqsAdapterConfiguration()
                .withAwsRegion(awsRegion)
                .withAwsProfile(awsProfile)
                .withAwsAccessKey(awsAccessKey)
                .withAwsSecretKey(awsSecretKey)
                .withHost(sqsHost)
                .withPort(sqsPort)
                .withSqsInboundQueue(sqsQueueInbound)
                .withUseEndpoint(sqsUseEndpoint)
                .withConcurrency(concurrency)
                .withSQSListener();
    }

    @Bean
    public SqsAdapterClient sqsClient() {
        return new SqsAdapterConfiguration()
                .withAwsRegion(awsRegion)
                .withAwsProfile(awsProfile)
                .withAwsAccessKey(awsAccessKey)
                .withAwsSecretKey(awsSecretKey)
                .withHost(sqsHost)
                .withPort(sqsPort)
                .withSqsInboundQueue(sqsQueueOutbound)
                .withUseEndpoint(sqsUseEndpoint)
                .withNumberOfMessagesToReceive(numberOfMessagesToReceive)
                .withVisibilityTimeout(visibilityTimeout)
                .withWaitTimeSeconds(waitTimeSeconds)
                .withConcurrency(concurrency)
                .withSQSClient();
    }

}
```

Listener:
```
@Component
public class PassagemListener{

    private static final Logger log = LoggerFactory.getLogger(PassagemListener.class);

    @Value("${aws.sqs.queue.inbound.name}")
    private String inboundQueueName;

    @Autowired
    private CoreService coreService;

    @JmsListener(destination = "${aws.sqs.queue.inbound.name}")
    public void receberPassagem(String message) throws Exception {
        try {
            PassagemDTO dto = (PassagemDTO) new PassagemDTO().createFromJSON(json, PassagemDTO.class);
            log.info("Mensagem {} recebida da fila {}, enviando para processamento", dto.toJSON(), inboundQueueName);
            coreService.validarPassagem(dto);
        } catch (Exception e) {
            log.error("Erro: ", e);
            throw e;
        }
    }
}
```

Producer:
```
@Component
public class PassagemProducer{

    private static final Logger log = LoggerFactory.getLogger(PassagemProducer.class);

    @Autowired
    private SqsAdapterClient client;

    public void enviarPassagem(String mensagem, String nomeFila) throws Exception {
        try {
            log.info("Enviando mensagem {} para a fila {}, mensagem, nomeFila);
            client.enviar(mensagem, nomeFila);
        } catch (Exception e) {
            log.error("Erro: ", e);
            throw e;
        }
    }
}

```

# SNS

Configuration:
```
@Configuration
@EnableJms
public class SNSConfiguration {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.profile}")
    private String awsProfile;

    @Value("${aws.access.key}")
    private String awsAccessKey;
   
    @Value("${aws.secret.key}")
    private String awsSecretKey;

    @Value("${aws.sns.endpoint.use}")
    private boolean snsUseEndpoint;

    @Value("${aws.sns.endpoint.host}")
    private String snsHost;

    @Value("${aws.sns.endpoint.port}")
    private Integer snsPort;

    @Value("${aws.sns.topic.arn}")
    private String topicARN;

    @Bean
    public SnsAdapterClient snsClient() {
        return new SnsAdapterConfiguration()
                .withAwsRegion(awsRegion)
                .withAwsProfile(awsProfile)
                .withAwsAccessKey(awsAccessKey)
                .withAwsSecretKey(awsSecretKey)
                .withHost(host)
                .withPort(port)
                .withUseEndpoint(useEndpoint)
                .withTopicARN(topicARN)
                .withSNSClient();
    }
```    

Publisher:
```
@Component
public class PassagemPublisher{

    private static final Logger log = LoggerFactory.getLogger(PassagemPublisher.class);

    @Autowired
    private SnsAdapterClient client;

    public void enviarPassagem(String mensagem, String nomeTopico) throws Exception {
        try {
            log.info("Enviando mensagem {} para o topico {}", mensagem, nomeTopico);
            client.publish(mensagem, nomeTopico);
        } catch (Exception e) {
            log.error("Erro: ", e);
            throw e;
        }
    }
}
```

# Redis


Properties:

```
aws.redis.host=redis
aws.redis.port=6379
aws.redis.endpoint.use=false
aws.redis.map.sequencial.name=SequencialEnviarPassagem
aws.redis.map.sequencial.init=0
```

Configuration:

```
@Configuration
public class RedisConfiguration {

    @Value("${aws.redis.endpoint.host}")
    private String host;

    @Value("${aws.redis.endpoint.port}")
    private int port;

    @Value("${aws.redis.endpoint.use}")
    private boolean useEndpoint;

    @Value("${aws.redis.map.sequencial.name}")
    private String redisMap;

    @Value("${aws.redis.map.sequencial.init}")
    private String sequentialInit;
 
    @Bean
    public RedisAdapterClient<String, String> redisClient() {

        return new RedisAdapterConfiguration<String, String>()
                  .withHost(host)
                  .withPort(port)
                  .withUseEndpoint(useEndpoint)
                  .withSequentialInit(sequentialInit)
                  .withRedisMap(redisMap)
                  .withRedisClient();
    }
 
}                
```


Adapter:
```
@Component
public class RedisSequencialHandler{

    private static final Logger log = LoggerFactory.getLogger(RedisSequencialHandler.class);

    @Autowired
    private RedisAdapterClient<String, String> client;

    public void gerarSequencial(String concessionariaId) throws Exception {
        try {
            String sequencial = client.gerarSequencial(concessionariaId);
            log.info("Gerado sequencial {} para a concessionaria {}", sequencial, concessionariaId);
        } catch (Exception e) {
            log.error("Erro: ", e);
            throw e;
        }
    }
}

```

# HornetQ 2.3.12.Final:

IMPORTANTE: para Integração com HornetQ utilizar a versão 1.3.8.RELEASE do Spring Boot!!!

Inserindo dependencia:
 
> 1- No pom.xml do serviço, seção 'properties' insira:

```
<properties> 
  <!-- Fast AWS Connections -->
  <fast-aws-connection.version>2.1.9-SNAPSHOT</fast-aws-connection.version>
   
  <hornetq.version>2.3.12.Final</hornetq.version>
  ...
</properties> 
```

> 2- Na seção 'dependencies' insira:

```
<!-- Fast AWS Connections -->
<dependency>
    <groupId>br.com.fast.aws.connections.commons</groupId>
    <artifactId>fast-aws-connection-commons</artifactId>
    <version>${fast-aws-connection.version}</version>
</dependency>

<dependency>
    <groupId>br.com.fast.aws.connections.hornetq</groupId>
    <artifactId>fast-aws-connection-hornetq</artifactId>
    <version>${fast-aws-connection.version}</version>
</dependency>     
```

Configuration:
```
@Configuration
@EnableJms
public class HornetQConfiguration {
  
    @Value("${spring.hornetq.user}")
    private String user;

    @Value("${spring.hornetq.password}")
    private String password;

    @Value("${spring.hornetq.queue.inbound.name}")
    private String queueInbound;

    @Value("${spring.hornetq.topic.outbound.name}")
    private String topicOutbound;

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) throws JMSException {
        return new HornetqAdapterConfiguration()
                .withUser(user)
                .withPassword(password)
                .withTopicName(queueInbound)
                .withPubSubDomain(false)
                .withSessionTransacted(true)
                .withConnectioFactory(connectionFactory)
                .withListener();
    }

    @Bean
    public JmsTemplate jmsTopicProducer(ConnectionFactory connectionFactory) throws JMSException {
        return new HornetqAdapterConfiguration()
                .withUser(user)
                .withPassword(password)
                .withConnectioFactory(connectionFactory)
                .withPubSubDomain(true)
                .withTopicName(topicOutbound)
                .withJmsTemplate();
    }

    @Bean
    public ConnectionFactory connectionFactory() throws JMSException {
        return new HornetqAdapterConfiguration()
                .withHost(host)
                .withPort(port)
                .connectionFactory();
    }

}    
```
Listener:
```
@Component
public class PassagemListener implements HornetQInboundPort{

    private static final Logger log = LoggerFactory.getLogger(PassagemListener.class);

    @Autowired
    private CoreService coreService;

    @JmsListener(destination = "${spring.hornetq.queue.inbound.name}")
    public void processaMessagem(Message message, Session session) {
        log.info("Recebendo mensagem da fila passagens...");

        try {
            if (message instanceof BytesMessage) {
                BytesMessage byteMessage = (BytesMessage) message;

                byte[] byteRecebido = new byte[(int) byteMessage.getBodyLength()];
                byteMessage.readBytes(byteRecebido);

                Long time = message.getJMSTimestamp();
                Date dataPostagem = new Date(time);

                Passagem passagem = deserealizeProtoBin(byteRecebido);
                MensagemDto dto = new DtoParse().parseToMensagemDTO(passagem, dataPostagem);

                coreService.processaPassagem(dto);

                sessionCommit(session);
            }
        } catch (Exception e) {
            log.error("Erro: ", e);
            sessionRollback(session);
        }
    }

    private Passagem deserealizeProtoBin(byte[] proto) {
        try {
            return Passagem.parseFrom(proto);
        } catch (InvalidProtocolBufferException e) {
            log.error("Erro: ", e);
            return null;
        }
    }

    private void sessionCommit(Session session) {
        try {
            session.commit();
        } catch (JMSException e) {
            log.error("Erro: ", e);
        }
    }

    private void sessionRollback(Session session) {
        try {
            session.rollback();
        } catch (JMSException e) {
            log.error("Erro: ", e);
        }
    }

}
```

Producer:
```
@Component
public class PassagemProducer{

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${spring.hornetq.topic.outbound.name}")
    private String topicOutbound;

    @Override
    public void enviar(DTO dto) {
      log.info("Enviando mensagem {} para o topico da concessionaria", dto.toJSON());
      PassagemProcessada proto = ParseUtil.toPassagemProcessada(mensagemProcessadaDto);
      
      String nomeTopico = topicOutbound.concat(String.format("%05d", mensagemProcessadaDto.getConcessionariaId()));

      jmsTemplate.convertAndSend(nomeTopico, proto.toByteArray());

    }
}

```

# Kinesis:

Configuration:
```
@Configuration
public class KinesisConfiguration {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.profile}")
    private String awsProfile;

    @Value("${aws.access.key}")
    private String awsAccessKey;
   
    @Value("${aws.secret.key}")
    private String awsSecretKey;

    @Value("${aws.kinesis.endpoint.use}")
    private Boolean useEndPoint;

    @Value("${aws.kinesis.endpoint.host}")
    private String host;

    @Value("${aws.kinesis.endpoint.port}")
    private Integer port;

    @Value("${aws.kinesis.application.name}")
    private String applicationName;

    @Value("${aws.kinesis.inbound.stream.name}")
    private String inboundStreamName;

    @Value("${aws.kinesis.outbound.stream.name}")
    private String outboundStreamName;

    @Value("${aws.kinesis.max.records.read.per.shard}")
    private Integer maxRecordsPerShard;

    @Value("${aws.kinesis.idle.time.between.reads.millis}")
    private long idleTimeBetweenReadsInMillis;

    @Value("${aws.kinesis.task.backoff.time.millis}")
    private long taskBackoffTimeInMillis;

    @Value("${aws.kinesis.initial.position.stream}")
    private String initialPositionInStream;

    @Bean(name = "KinesisAdapterClientInbound")
    public KinesisAdapterClient consumer() {
        return new KinesisAdapterConfiguration()
                .withAwsRegion(awsRegion)
                .withAwsProfile(awsProfile)
                .withAwsAccessKey(awsAccessKey)
                .withAwsSecretKey(awsSecretKey)
                .withPort(port)
                .withHost(host)
                .withApplicationName(applicationName)
                .withStreamName(inboundStreamName)
                .withMaxRecordsPerShard(maxRecordsPerShard)
                .withIdleTimeBetweenReadsInMillis(idleTimeBetweenReadsInMillis)
                .withInitialPositionInStream(InitialPositionInStream.valueOf(initialPositionInStream))
                .withTaskBackoffTimeInMillis(taskBackoffTimeInMillis)
                .withKinesisConsumerClient();
    }

    @Bean(name = "KinesisAdapterClientOutbound")
    public KinesisAdapterClient producer() {
        return new KinesisAdapterConfiguration()
                .withAwsRegion(awsRegion)
                .withAwsProfile(awsProfile)
                .withAwsAccessKey(awsAccessKey)
                .withAwsSecretKey(awsSecretKey)
                .withStreamName(outboundStreamName)
                .withKinesisProducerClient();
    }

}
```

Consumer:
```
@Component
public class Consumer{

    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    @Qualifier("KinesisAdapterClientInbound")
    private KinesisAdapterClient client;

    @Autowired
    private IRecordProcessorFactory processorFactory;

    @EventListener(ContextRefreshedEvent.class)
    public void consumer() {

        new Worker.Builder()
                .recordProcessorFactory(processorFactory)
                .config(client.getConfig())
                .build()
                .run();
    }

}

```
Factory para o consumer:
```
@Component
public class ProcessorFactory implements IRecordProcessorFactory {

    private static final Logger log = LoggerFactory.getLogger(ProcessorFactory.class);
    private static final int NUM_RETRIES = 10;

    @Value("${aws.kinesis.task.backoff.time.millis}")
    private long taskBackoffTimeInMillis;

    @Autowired
    private CoreService coreService;

    @Override
    public IRecordProcessor createProcessor() {
        return this.new RecordProcessor();
    }

    private class RecordProcessor implements IRecordProcessor {

        private String kinesisShardId;

        @Override
        public void initialize(InitializationInput initializationInput) {
            log.info("Iniciando com ShardId: {}", initializationInput.getShardId());
            kinesisShardId = initializationInput.getShardId();
        }

        @Override
        public void processRecords(ProcessRecordsInput processRecordsInput) {
            List<Record> records = processRecordsInput.getRecords();
            log.info("Processando {} records do shardId: {}", records.size(), kinesisShardId);

            try {
                coreService.processarPassagem(records.stream()
                        .map(Record::getData)
                        .map(record -> new PassagemDTO()
                                      .createFromJSON(new String(record.array(), StandardCharsets.UTF_8)), PassagemDTO.class)
                        .collect(Collectors.groupingBy(PassagemDto::getConcessionariaId)));

                checkpoint(processRecordsInput.getCheckpointer());
                log.info("Fim de processamento da shard '{}'", kinesisShardId);
            } catch (Exception e) {
                log.error("Erro durante o envio da mensagem ou execução do Checkpoint!!!", e);
            }
        }

        @Override
        public void shutdown(ShutdownInput shutdownInput) {
            log.info("Shutting down da shard '{}', motivo: {}", kinesisShardId, shutdownInput.getShutdownReason());
            if (shutdownInput.getShutdownReason() == ShutdownReason.TERMINATE) {
                checkpoint(shutdownInput.getCheckpointer());
            }
        }

        /**
         * Checkpoint with retries.
         * 
         * @param checkpointer
         */
        private void checkpoint(IRecordProcessorCheckpointer checkpointer) {
            log.info("Checkpointing shard", kinesisShardId);
            for (int i = 0; i < NUM_RETRIES; i++) {
                try {
                    checkpointer.checkpoint();
                    break;
                } catch (ShutdownException se) {
                    // Ignore checkpoint if the processor instance has been shutdown (fail over).
                    log.info("caught shutdown exception skipping checkpoint", se);
                    break;
                } catch (ThrottlingException e) {
                    // Backoff and re-attempt checkpoint upon transient failures
                    if (i >= (NUM_RETRIES - 1)) {
                        log.error("checkpoint failed after {} attempts.", i + 1, e);
                        break;
                    } else {
                        log.info("Transient issue when checkpointing attempt {} of {}", i + 1, NUM_RETRIES, e);
                    }
                } catch (InvalidStateException e) {
                    // This indicates an issue with the DynamoDB table (check for table, provisioned IOPS).
                    log.error("cannot save checkpoint dinamodb table", e);
                    break;
                }

                try {
                    Thread.sleep(taskBackoffTimeInMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.debug("interrupted sleep", e);
                }
            }
        }
    }

}
```

Producer:
```
@Component
public class Producer{

    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    @Autowired
    @Qualifier("KinesisAdapterClientOutbound")
    private KinesisAdapterClient client;

    public void envia(DTO dto, String concessionariaId) {
        log.info("Enviando passagem: {} para stream: {}", dto.toJSON(), client.getStreamName());
        client.enviar(dto.toJSON(), concessionariaId);     

    }

}
```

## Change log:

| Data | Autor | Descrição | Versao |
| --- | --- | --- | --- |
| 19/05/2018 | Wagner Alves (aka Barão) | Exposed for Open Source | 2.10.73 |
| 20/02/2018 | Wagner Alves (aka Barão) | AWS PinPoint Adapter | 2.1.13-SNAPSHOT | 
| 25/01/2018 | Wagner Alves (aka Barão) | Role Based Credentials | 2.1.9-SNAPSHOT |
| 10/03/2017 | Wagner Alves (aka Barão) | AWS Lambda Adapter | 2.1.9-SNAPSHOT | 

## Restrições:

- Testes de integração não são habilitados por default, pois necessitam de criação prévia de recursos na AWS.

## TO DO:

- Converter este READ ME para o inglẽs.
- Configurar maven surefire plugin para testes separados por módulo.
- Criação automática de recursos na AWS para testes.

## I hope you enjoy it!

- Implementem qualquer adapter conforme sua necessidade adicionando novos módulos e seguindo os padrões... ;-)

