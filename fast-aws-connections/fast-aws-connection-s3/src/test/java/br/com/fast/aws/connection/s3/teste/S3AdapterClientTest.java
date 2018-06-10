package br.com.fast.aws.connection.s3.teste;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;

import br.com.fast.aws.connection.s3.S3AdapterClient;
import br.com.fast.aws.connection.s3.S3AdapterConfiguration;

public class S3AdapterClientTest {

    private static boolean useEndpoint = false;
    private static String bucketName = "fast-aws-connections-bucket";

    private S3AdapterClient client;
    private String conteuDoArquivo;

    @Before
    public void setUp() {

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setClientExecutionTimeout(10000);

        // Client
        client = new S3AdapterConfiguration()
                .withClientConfiguration(clientConfiguration)
                .withAwsProfile("dev")
                .withAwsRegion("us-east-1")
                .withUseEndpoint(useEndpoint)
                .withAmazonS3Client();

        conteuDoArquivo = "{teste:S3}";

    }

    @Test
    public void deveCriarBucketEenviarArquivo() throws IOException {

        // Enviar arquivo
        client.enviaParaBucket(bucketName, conteuDoArquivo, "testeS3.txt");

        // Buscar
        S3Object s3Object = client.obtemDoBucket(bucketName, "testeS3.txt");

        // Ler o conteudo do arquivo
        BufferedReader reader = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()));
        String linha = reader.readLine();

        assertEquals(conteuDoArquivo, linha);

    }

    @Test
    public void deveCriarBucketEenviarObjetoTipoFile() throws IOException {

        File file = createFile(conteuDoArquivo.getBytes(), "arquivo-teste-s3");
        String fileKey = client.generateKeyForfile();

        // Enviar arquivo
        client.enviaFileParaBucket(bucketName, fileKey, file);

        // Buscar
        S3Object s3Object = client.obtemDoBucket(bucketName, fileKey);

        // Ler o conteudo do arquivo
        BufferedReader reader = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()));
        String linha = reader.readLine();

        assertEquals(conteuDoArquivo, linha);

    }

    @Test(expected = AmazonS3Exception.class)
    public void deveDeletarArquivoDoBucket() throws IOException {
        // Criar
        String fileName = "testeS3.txt";

        client.removeDoBucket(bucketName, fileName);

        client.obtemDoBucket(bucketName, fileName);

    }

    @Test(expected = AmazonS3Exception.class)
    public void deveCriarDiretorioNoBucketEnviarArquivoEDeletarFolder() throws IOException {

        String folderName = "arquivos";
        String fileName = folderName.concat("/").concat("testeS3.txt");

        // Criar folder
        client.createFolder(bucketName, folderName);

        // Enviar arquivo para o diretorio
        client.enviaParaBucket(bucketName, conteuDoArquivo, fileName);

        // Remover folder
        client.removeFolder(bucketName, folderName);

        client.obtemDoBucket(bucketName, fileName);

    }

    @Test(expected = AmazonS3Exception.class)
    public void deveDeletarBucket() throws IOException {
        // Criar
        String bucketName = "bucket-aws-test-delete";
        client.criaBucket(bucketName);
        // Deletar
        client.deletaBucket(bucketName);

        client.obtemDoBucket(bucketName, "qualquer-arquivo.txt");

    }

    @Test
    public void deveEsvaziarBucket() {

        // Criar
       
        // Enviar arquivo
        client.enviaParaBucket(bucketName, conteuDoArquivo, "testeS3_01.txt");
        client.enviaParaBucket(bucketName, conteuDoArquivo, "testeS3_02.txt");
        client.enviaParaBucket(bucketName, conteuDoArquivo, "testeS3_03.txt");
        client.enviaParaBucket(bucketName, conteuDoArquivo, "testeS3_04.txt");
        client.enviaParaBucket(bucketName, conteuDoArquivo, "testeS3_05.txt");

        sleep(2000);

        // Esvaziar Bucket
        client.esvaziaBucket(bucketName);

    }

    @Test
    public void verificaSeExisteBucket() {
        boolean existeBucket = client.existeBucket(bucketName);
        assertEquals(true, existeBucket);
    }

    @Test
    public void listarBuckets() {
        client.listarBucketNames();
        client.listarBuckets();
    }

    private void sleep(int time) {
        try {
            System.out.println("Sleeping for " + time + " miliseconds...");
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the file.
     *
     * @param bytes
     *            the bytes
     * @return the file
     */
    public static File createFile(byte[] bytes, String fileName) {
        File file = null;
        FileOutputStream fos = null;
        try {
            file = File.createTempFile(fileName, null, null);
            fos = new FileOutputStream(file);
            fos.write(bytes);
        } catch (Exception e) {
            System.out.println("Erro createFile: " + e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    System.out.println("Erro ao fechar arquivo: " + e);
                }
            }
        }

        return file;

    }
}
