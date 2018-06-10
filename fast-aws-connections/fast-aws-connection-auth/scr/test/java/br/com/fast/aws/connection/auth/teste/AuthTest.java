package br.com.fast.aws.connection.auth.teste;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;

import br.com.fast.aws.connection.auth.provider.ApplicationPropertiesCredentialsProvider;
import br.com.fast.aws.connection.auth.provider.RoleBasedCredentialsProfile;

public class AuthTest {

    private String awsProfile;

    @Before
    public void setUp() {
        awsProfile = "dev";
    }

    @Test
    public void deveConfigurarAuthCredentialPropertyFile() {

        String awsAccessKey = "FAKEV64Z7PDCSU24342423424";
        String awsSecretKey = "FAKEEma7ggqzxtBpM/234234243243";

        AWSCredentials credentials = new ApplicationPropertiesCredentialsProvider(awsAccessKey, awsSecretKey).getCredentials();

        printContent("TEST OUTPUT: Keys from Property File:");
        printContent("aws_access_key_id=" + credentials.getAWSAccessKeyId());
        printContent("aws_secret_access_key=" + credentials.getAWSSecretKey());
        printContent("------------------------------------------------------------------------");
    }

    @Test
    public void develancarExcecaoAoConfigurarAuthCredentialPropertyFile() {

        String awsAccessKey = "";
        String awsSecretKey = "";

        AWSCredentials credentials = new ApplicationPropertiesCredentialsProvider(awsAccessKey, awsSecretKey).getCredentials();

        printContent("TEST OUTPUT: Keys from Property File:");
        printContent("aws_access_key_id=" + credentials.getAWSAccessKeyId());
        printContent("aws_secret_access_key=" + credentials.getAWSSecretKey());
        printContent("------------------------------------------------------------------------");
    }

    @Test
    public void deveConfigurarAuthCredentialByEnvironmentVariables() {

        AWSCredentials credentials = new EnvironmentVariableCredentialsProvider().getCredentials();

        printContent("TEST OUTPUT: Keys from Environment Variables:");
        printContent("aws_access_key_id=" + credentials.getAWSAccessKeyId());
        printContent("aws_secret_access_key=" + credentials.getAWSSecretKey());
        printContent("------------------------------------------------------------------------");

    }

    @Test
    public void deveLancarExcecaoAoConfigurarAuthCredentialByEnvironmentVariables() {

        AWSCredentials credentials = new EnvironmentVariableCredentialsProvider().getCredentials();

        printContent("TEST OUTPUT: Keys from Environment Variables:");
        printContent("aws_access_key_id=" + credentials.getAWSAccessKeyId());
        printContent("aws_secret_access_key=" + credentials.getAWSSecretKey());
        printContent("------------------------------------------------------------------------");

    }

    @Test
    public void deveConfigurarAuthCredentialbyRoles() {

        AWSCredentials credentials = new RoleBasedCredentialsProfile(awsProfile).getCredentials();

        printContent("TEST OUTPUT: Keys from Property Role Based:");
        printContent("aws_access_key_id=" + credentials.getAWSAccessKeyId());
        printContent("aws_secret_access_key=" + credentials.getAWSSecretKey());
        printContent("------------------------------------------------------------------------");

    }

    @Test
    public void deveLancarExcecaoAoConfigurarAuthCredentialbyRoles() {

        AWSCredentials credentials = new RoleBasedCredentialsProfile("wagnerProfile").getCredentials();

        printContent("TEST OUTPUT: Keys from Property Role Based:");
        printContent("aws_access_key_id=" + credentials.getAWSAccessKeyId());
        printContent("aws_secret_access_key=" + credentials.getAWSSecretKey());
        printContent("------------------------------------------------------------------------");

    }

    private void printContent(String content) {
        System.out.println(content);
    }

    @SuppressWarnings("unused")
    private void setAWSEnvironmentVariables() {
        EnvironmentUtils.setEnvironmentVariable("AWS_ACCESS_KEY_ID", "ENV_VAR_AKIAJ2TPFJYSFEUEOTUER");
        EnvironmentUtils.setEnvironmentVariable("AWS_SECRET_ACCESS_KEY", "ENV_VAR_asdhf984723as/d0123k4iodsfsa");
        EnvironmentUtils.printVariables();
    }

    @SuppressWarnings("unused")
    private void unSetAWSEnvironmentVariables() {
        EnvironmentUtils.unSetEnvironmentVariable("AWS_ACCESS_KEY_ID");
        EnvironmentUtils.unSetEnvironmentVariable("AWS_SECRET_ACCESS_KEY");
        EnvironmentUtils.printVariables();
    }

}
