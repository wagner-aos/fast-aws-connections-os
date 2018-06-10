package br.com.fast.aws.connection.redshift;

import com.amazonaws.services.redshift.AmazonRedshift;

/**
 * @author Wagner.Alves
 */
public class RedshiftAdapterClient {

    @SuppressWarnings("unused")
	private AmazonRedshift redshiftClient;

    /**
     * RedshiftAdapterClient constructor
     */
    public RedshiftAdapterClient(AmazonRedshift redshiftClient) {
        super();
        this.redshiftClient = redshiftClient;

    }

    public String emDesenvolvimento() {
        return "Coming Soon!";
    }

}
