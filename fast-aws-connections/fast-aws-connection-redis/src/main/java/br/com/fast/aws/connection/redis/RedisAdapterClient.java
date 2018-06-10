package br.com.fast.aws.connection.redis;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Wagner.Alves
 * @param <K>
 * @param <V>
 */
public class RedisAdapterClient<K, V> {

    private static final Logger log = LoggerFactory.getLogger(RedisAdapterClient.class);
 
    private RedissonClient redissonClient;
    private String redisMap;
    private Long sequentialInit;
    private boolean lock;
    private long lockLeaseTime;

    /**
     * RedisAdapterClient constructor
     * 
     * @param redissonClient
     * @param redisMap
     */
    public RedisAdapterClient(RedissonClient redissonClient, String redisMap, Long sequentialInit, boolean lock, long lockLeaseTime) {
        this.redissonClient = redissonClient;
        this.redisMap = redisMap;
        this.sequentialInit = sequentialInit;
        if(this.sequentialInit == null || this.sequentialInit < 0) {
        	this.sequentialInit = 0L;
        }
        this.lock = lock;
        this.lockLeaseTime = lockLeaseTime;
        if(this.lockLeaseTime < 0) {
        	this.lockLeaseTime = 0;
        }
    }

    /**
     * Metodo que gera um novo numero sequencial.
     * Caso não exista gera o sequencial a partir da propriedade "sequentialInit".
     * Caso exista o sequencial, retorna N+1.
     * -
     * sequentialInit default = 0L
     * 
     * @param String
     */
    @SuppressWarnings("unchecked")
    public synchronized long gerarSequencial(K concessionariaId) throws Exception {

        log.info("Buscando Sequencial no Map Redisson: {}", redisMap);
        RMap<K, V> sequencialMap = redissonClient.getMap(redisMap);
        
        if(lock) {
			log.info("Solicitando o lock do sequencial por {} segundos, para concessionaria {}", lockLeaseTime,
	        concessionariaId);
	        RLock sequencialkeyLock = sequencialMap.getLock(concessionariaId);
	        sequencialkeyLock.lock(lockLeaseTime, TimeUnit.SECONDS);
        }

        Long sequencial = (Long) sequencialMap.get(concessionariaId);

        if (sequencial == null) {
            sequencialMap.put(concessionariaId, (V) sequentialInit);
        } else {
            ++sequencial;
            sequencialMap.put(concessionariaId, (V) sequencial);
        }

        sequencial = (Long) sequencialMap.get(concessionariaId);
        log.info("Sequencial gerado com numero: {}, para Concessionaria {}", sequencial, concessionariaId);

        return sequencial;
    }

    /**
     * Inserts an object on Redis map
     * 
     * @param id
     * @param json
     * @return
     */
    public V inserir(K id, V json) {
        RMap<K, V> map = redissonClient.getMap(redisMap);
        return map.put(id, json);
    }

    /**
     * Gets an object from Redis map
     * 
     * @param id
     * @return String
     */
    public V buscarPorId(K id) {
        RMap<K, V> map = redissonClient.getMap(redisMap);
        return map.get(id);
    }

    /**
     * Removes an object of Redis map
     * 
     * @param id
     */
    public void deletar(K id) {
        RMap<K, V> map = redissonClient.getMap(redisMap);
        map.remove(id);
    }

    /**
     * Shutdown a redis client
     */
    public void shutdown() {
        redissonClient.shutdown();
    }

    /**
     * Removes all objects of a Redis map
     */
    public boolean tearDown() {
        
    	RMap<K, V> map = redissonClient.getMap(redisMap);
    	
        clearMap(map);
        
        if(map.isEmpty()) {
        	System.out.println(">>> Redis-TearDown executado!");
        	return true;
        }
        
        return false;
    }
    

    /**
     * Removes all objects of a Redis map
     * 
     * @param mapName
     */
    public boolean tearDown(String mapName) {
    	
        RMap<K, V> map = redissonClient.getMap(mapName);
        clearMap(map);
        
        if(map.isEmpty()) {
        	System.out.println(">>> Redis-TearDown executado!");
        	return true;
        }
        
        return false;
    }

	private void clearMap(RMap<K, V> map) {
		
		if (map.isExists()) {
        	System.out.println(">>> Redis-TearDown, limpando " + map.size() + " registros.");
        	map.forEach((key, value) -> {
            	map.remove(key);
        	});
        }
	}

    public RMap<K, V> getMap() {
        return redissonClient.getMap(redisMap);
    }

    public RMap<K, V> getMap(String mapName) {
        return redissonClient.getMap(mapName);
    }

    public String getRedisMapName() {
        return redisMap;
    }

}
