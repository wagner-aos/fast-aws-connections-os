package br.com.fast.aws.connection.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import br.com.fast.aws.connection.auth.configuration.BaseConfiguration;

/**
 * @author Wagner.Alves
 * @param <K>
 * @param <V>
 */
public class RedisAdapterConfiguration<K, V> extends BaseConfiguration {

    private String redisMap;
    private long sequentialInit;
    private boolean lock;
    private long lockLeaseTime;

    public RedisAdapterConfiguration<K, V> withUser(String user) {
        this.user = user;
        return this;
    }

    public RedisAdapterConfiguration<K, V> withPassword(String password) {
        this.password = password;
        return this;
    }

    public RedisAdapterConfiguration<K, V> withHost(String host) {
        this.host = host;
        return this;
    }

    public RedisAdapterConfiguration<K, V> withPort(String port) {
        this.port = port;
        return this;
    }

    public RedisAdapterConfiguration<K, V> withUseEndpoint(boolean useEndpoint) {
        this.useEndpoint = useEndpoint;
        return this;
    }

    public String getRedisMap() {
        return redisMap;
    }

    public RedisAdapterConfiguration<K, V> withRedisMap(String redisMap) {
        this.redisMap = redisMap;
        return this;
    }

    public RedisAdapterConfiguration<K, V> withSequentialInit(long sequentialInit) {
		this.sequentialInit = sequentialInit;
		return this;
	}

	public RedisAdapterConfiguration<K, V> withLock(boolean lock) {
		this.lock = lock;
		return this;
	}

	public RedisAdapterConfiguration<K, V> withLockLeaseTime(long lockLeaseTime) {
		this.lockLeaseTime = lockLeaseTime;
		return this;
	}

	/**
     * Returns a RedisAdapterClient
     * 
     * @return
     */
    public RedisAdapterClient<K, V> withRedisClient() {

        if (this.host == null) {
            throw new IllegalArgumentException("Host não pode ser nulo");
        }
        if (this.port == null) {
            throw new IllegalArgumentException("Port não pode ser nulo");
        }
        if (this.redisMap == null) {
            throw new IllegalArgumentException("RedisMap name não pode ser nulo");
        }

        Config config = new Config();
        config.useReplicatedServers().addNodeAddress(host.concat(":").concat(port));
        RedissonClient redissonClient = Redisson.create(config);

        return new RedisAdapterClient<>(redissonClient, redisMap, sequentialInit, lock, lockLeaseTime);
    }

}
