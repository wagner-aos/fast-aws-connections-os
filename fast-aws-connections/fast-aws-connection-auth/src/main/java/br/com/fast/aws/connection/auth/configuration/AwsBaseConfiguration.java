package br.com.fast.aws.connection.auth.configuration;

import br.com.fast.aws.connection.auth.chain.AWSAuth;

/**
 * @author Wagner.Alves
 */
public class AwsBaseConfiguration extends AWSAuth {

    protected String awsAccessKey;
    protected String awsSecretKey;
    protected String awsRegion;
    protected String awsProfile;
    protected boolean useEndpoint;
    protected String host;
    protected Integer port;

    protected String getAwsAccessKey() {
        return awsAccessKey;
    }

    protected void setAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
    }

    protected String getAwsSecretKey() {
        return awsSecretKey;
    }

    protected void setAwsSecretKey(String awsSecretKey) {
        this.awsSecretKey = awsSecretKey;
    }

    protected String getAwsRegion() {
        return awsRegion;
    }

    protected void setAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
    }

    public String getAwsProfile() {
        return awsProfile;
    }

    public void setAwsProfile(String awsProfile) {
        this.awsProfile = awsProfile;
    }

    protected boolean isUseEndpoint() {
        return useEndpoint;
    }

    protected void setUseEndpoint(boolean useEndpoint) {
        this.useEndpoint = useEndpoint;
    }

    protected String getHost() {
        return host;
    }

    protected void setHost(String host) {
        this.host = host;
    }

    protected Integer getPort() {
        return port;
    }

    protected void setPort(Integer port) {
        this.port = port;
    }

    protected String resolveEndpoint() {
        if (host != null & port != null)
            return "http://".concat(host).concat(":").concat(port.toString());

        return null;
    }

    protected void validateBaseConfiguration() {

        if (this.awsRegion == null) {
            throw new IllegalArgumentException("AWS Region não pode ser nulo");
        }
        if (this.useEndpoint && this.host == null) {
            throw new IllegalArgumentException("Host não pode ser nulo");
        }
        if (this.useEndpoint && this.port == null) {
            throw new IllegalArgumentException("Port não pode ser nulo");
        }

    }

}
