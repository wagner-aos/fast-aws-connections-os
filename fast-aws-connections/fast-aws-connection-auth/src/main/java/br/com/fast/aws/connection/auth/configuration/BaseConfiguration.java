package br.com.fast.aws.connection.auth.configuration;

/**
 * @author Wagner.Alves
 */
public class BaseConfiguration {

    protected String user;
    protected String password;
    protected boolean useEndpoint;
    protected String host;
    protected String port;

    protected String getUser() {
        return user;
    }

    protected void setUser(String user) {
        this.user = user;
    }

    protected String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
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

    protected String getPort() {
        return port;
    }

    protected void setPort(String port) {
        this.port = port;
    }

}
