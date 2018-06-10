package br.com.fast.aws.connection.auth.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.internal.AllProfiles;
import com.amazonaws.auth.profile.internal.BasicProfile;
import com.amazonaws.auth.profile.internal.BasicProfileConfigLoader;
import com.amazonaws.auth.profile.internal.ProfileAssumeRoleCredentialsProvider;
import com.amazonaws.auth.profile.internal.ProfileStaticCredentialsProvider;
import com.amazonaws.auth.profile.internal.securitytoken.STSProfileCredentialsServiceLoader;
import com.amazonaws.profile.path.AwsProfileFileLocationProvider;

/**
 * Classe para obtenção de credenciais a partir de profile default do arquivo .aws/credentials
 * e roles configuradas no .aws/config
 * 
 * @author Wagner Alves
 */
public class RoleBasedCredentialsProfile implements AWSCredentialsProvider {

    private static final Logger LOG = Logger.getLogger(RoleBasedCredentialsProfile.class.getName());

    private final AWSCredentialsProvider provider;
    @SuppressWarnings("unused")
    private final String awsProfile;

    public RoleBasedCredentialsProfile(String awsProfile) {
        super();
        this.awsProfile = awsProfile;
        this.provider = getDefaultCredentials(awsProfile);
    }

    private AWSCredentialsProvider getDefaultCredentials(String awsProfile) {

        final String profileName = awsProfile;
        try {
            final AllProfiles allProfiles = new AllProfiles(Stream.concat(
                    BasicProfileConfigLoader.INSTANCE.loadProfiles(
                            AwsProfileFileLocationProvider.DEFAULT_CONFIG_LOCATION_PROVIDER.getLocation()).getProfiles().values().stream(),
                    BasicProfileConfigLoader.INSTANCE.loadProfiles(
                            AwsProfileFileLocationProvider.DEFAULT_CREDENTIALS_LOCATION_PROVIDER.getLocation()).getProfiles().values()
                            .stream())
                    .map(profile -> new BasicProfile(profile.getProfileName().replaceFirst("^profile ", ""), profile.getProperties()))
                    .collect(Collectors.toMap(profile -> profile.getProfileName(), profile -> profile,
                            (left, right) -> {
                                final Map<String, String> properties = new HashMap<>(left.getProperties());
                                properties.putAll(right.getProperties());
                                return new BasicProfile(left.getProfileName(), properties);
                            })));

            final BasicProfile profile = Optional.ofNullable(allProfiles.getProfile(profileName))
                    .orElseThrow(() -> new RuntimeException(String.format("Profile '%s' not found in %s",
                            profileName, allProfiles.getProfiles().keySet())));
            if (profile.isRoleBasedProfile()) {
                LOG.log(Level.FINE, ">>> AWS Credentials Role Based has been read ");
                return new ProfileAssumeRoleCredentialsProvider(STSProfileCredentialsServiceLoader.getInstance(), allProfiles, profile);
            } else {
                LOG.log(Level.FINE, ">>> AWS Credentials Static Profile has been read ");
                return new ProfileStaticCredentialsProvider(profile);
            }

        } catch (Exception e) {
            LOG.log(Level.FINE, ">>> AWS Credentials not found for Role or Profile Based: {0}", e.getMessage());
        }
        return null;

    }

    @Override
    public AWSCredentials getCredentials() {
        if (this.provider == null) {
            LOG.log(Level.FINE, ">>> RoleBased Credentials Provider is null");
            return new BasicAWSCredentials("XXX", "XXX");
        }
        return this.provider.getCredentials();
    }

    @Override
    public void refresh() {
        // Not implemented
    }

}
