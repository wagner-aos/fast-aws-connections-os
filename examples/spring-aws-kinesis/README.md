| Data | Autor | Descrição | Versao |
| --- | --- | --- | --- |
| 19/05/2018 | Wagner Alves (aka Barão) | Spring AWS Kinesis Stream | 1.0.0-SNAPSHOT | 

# Spring AWS Kinesis Example

> Funcionamento básico: ao iniciar, o exemplo envia mensagens para a stream e as consome criando um looping para teste da stream.

## Descrição:

> Exemplo de utilização do Fast AWS Connections com adapter de AWS Kinesis.


## Algumas configurações do application.properties do exemplo:
Não é necessário alterá-las para a execução inicial.

> esvazia os objetos da stream
```
aws.kinesis.purge.stream=false
```
> enviar mensagens ao comecar o teste
```
aws.kinesis.producer.send.messages.at.starting=false
```
> comeca a produzir mensagens depois de n milisegundos
```
aws.kinesis.producer.send.messages.start.after.seconds=180000
```
> quantidade de objetos para trafegar na stream
```
aws.kinesis.producer.send.message.initial.amount=1
```
> intervalo de envio para stream
```
aws.kinesis.producer.send.message.interval=0
```

# Preparação para executar o exemplo:

> Ter as credencias da AWS configuradas na máquina.
```
    AWS_ACCESS_KEY_ID
    AWS_SECRET_ACCESS_KEY
    AWS_REGION
```

> Deve se criar uma stream na AWS com o seguinte nome: 'KinesisStreamKcl'.

> Ter acesso as dependências do Fast AWS Connections Kinesis.

> Realizar a compilação com: 
```
    mvn clean package
```

## Como executar o exemplo via linha de comando:

> Execução com envio de mensagens ao iniciar:
```
    mvn spring-boot:run -Daws.kinesis.producer.send.messages.at.starting=true
```

> Execução com envio de mensagens ao iniciar com quantidade específica:
```
    mvn spring-boot:run -Daws.kinesis.producer.send.messages.at.starting=true -Daws.kinesis.producer.send.message.initial.amount=10
```

> Execução com um intervalo específico para o reenvio de mensagens:
```
    mvn spring-boot:run -Daws.kinesis.producer.send.message.interval=3000
```

> Execução com as propriedades padrão:
```
    mvn spring-boot:run 
```

## Remover os objetos da stream (PURGE)
> Consome os objetos da stream e não reenvia, até esvaziar a mesma!
```
    mvn spring-boot:run -Daws.kinesis.purge.stream=true
```


## IMPORTANTE (AWS Credentials):

> AWS Credentials: NÃO é obrigatório e nem coerente passar o VALOR das chaves de acesso da AWS no arquivo de propriedades 'application.properties' do Spring ou em qualquer outro arquivo de configuração, porem deve-se se manter as chaves em branco para garantir uma possível necessidade de se passar as chaves por properties para testes.

> Modulo Auth: As conexões estão configuradas com um provider para pegar o valor diretamente das variáveis de ambiente OU do arquivos 'credentials' e 'config' que fica localizado na pasta: usuario/.aws/ (quando se tem o [AWS CLI](http://docs.aws.amazon.com/pt_br/cli/latest/userguide/cli-chap-getting-started.html) configurado na máquina).

## AWS Credentials Role Based:

> 1- Pode se conseguir um token credentials por meio da execução do script:
```
    . aws-session-token.sh <environment>
```
Atenção: o script acima depende do módulo NPM 'aws-assume' que pode ser instalado da seguinte forma:
```
    npm install aws-assume -g
```

> 2- Pode inserir as credenciais diretamente no arquivo 'application.properties'.
```
#####-AWS Credentials-#####
aws.region=us-east-1
aws.profile=dev
aws.access.key=
aws.secret.key=
```

## Exemplo do arquivo 'credentials' em: usuario/.aws/credentials

```
[default]
aws_access_key_id = AKIAJ2TPF...
aws_secret_access_key = fjGtNv3Jidr9dYzS7...
region = us-east-1

```

## Exemplo do arquivo 'config' em: usuario/.aws/config
> Utizado só quando se tem credenciais Role Based

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
## Ordem de obtenção de credenciais AWS:

 O módulo Auth do Fast AWS Connections obtem credencial AWS na seguinte ordem:

```
 1-Propriedades passadas pelo ClientConfiguration 'application.properties'
 2-Variáveis de Ambiente
 3-Roles configuradas por profile nos arquivos .aws/credentials e .aws/config
 4-Static Profile, por profile do arquivo .aws/credentials

```

## Configurar variáveis de ambiente para DEV (OPCIONAL):

> Linux, macOS, or Unix
```
$ export AWS_ACCESS_KEY_ID=RCKIAJ2TPF...

$ export AWS_SECRET_ACCESS_KEY=fjGtNv3J...

$ export AWS_REGION=us-west-1
```

> Windows
```
set AWS_ACCESS_KEY_ID=RDKIAJ2TPF...

set AWS_SECRET_ACCESS_KEY=fjGtNv3J...

set AWS_REGION=us-west-1
```

## Links úteis:

[Variáveis de ambiente](http://docs.aws.amazon.com/pt_br/cli/latest/userguide/cli-environment.html)

[Arquivos de Configuração de Credencial](http://docs.aws.amazon.com/pt_br/cli/latest/userguide/cli-config-files.html)

[Amazon AWS Kinesis KCL](https://docs.aws.amazon.com/streams/latest/dev/developing-consumers-with-kcl.html)


