package br.com.fast.aws.connection.s3.teste;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;

import br.com.fast.aws.connection.s3.S3AdapterClient;
import br.com.fast.aws.connection.s3.S3AdapterConfiguration;

public class MainTeste {

    private static boolean useEndpoint = false;
    private static String host = "s3";
    private static Integer port = 8000;
    private static String bucketName = "Bucket-fast-aws-connections";
    private static S3AdapterClient client;

    private static String conteuDoArquivo;

    public static void main(String[] args) throws IOException {

        client = new S3AdapterConfiguration()
        		.withAwsRegion("us-east-1")
        		.withAwsProfile("dev")
                .withUseEndpoint(useEndpoint)
                .withHost(host)
                .withPort(port)
                .withAmazonS3Client();

        conteuDoArquivo = "{teste:S3}";

        Bucket bucket = client.criaBucket(bucketName);

        System.out.println("Bucket criado: " + bucket.getName());

        // Enviar para o bucket
        client.enviaParaBucket(bucket.getName(), conteuDoArquivo, "testeS3.txt");

        // Buscar
        S3Object s3Object = client.obtemDoBucket(bucket.getName(), "testeS3.txt");

        // Ler o conteudo
        BufferedReader reader = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()));
        while (true) {
            String line = reader.readLine();
            if (line == null)
                break;

            System.out.println("    " + line);
        }

    }

}
