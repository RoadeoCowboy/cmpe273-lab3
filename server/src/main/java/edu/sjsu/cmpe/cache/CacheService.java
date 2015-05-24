package edu.sjsu.cmpe.cache;

import java.util.concurrent.ConcurrentHashMap;

import edu.sjsu.cmpe.cache.repository.ChronicleMapCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import edu.sjsu.cmpe.cache.api.resources.CacheResource;
import edu.sjsu.cmpe.cache.config.CacheServiceConfiguration;
import edu.sjsu.cmpe.cache.domain.Entry;
import edu.sjsu.cmpe.cache.repository.CacheInterface;

public class CacheService extends Service<CacheServiceConfiguration> {

    private final Logger log = LoggerFactory.getLogger(getClass());
    public String serverName = "";

    public static void main(String[] args) throws Exception {
        CacheService cacheService = new CacheService();
        String serverName = args[1];
        cacheService.serverName = serverName;
        cacheService.run(args);
    }

    @Override
    public void initialize(Bootstrap<CacheServiceConfiguration> bootstrap) {
        bootstrap.setName("cache-server");
    }

    @Override
    public void run(CacheServiceConfiguration configuration,
            Environment environment) throws Exception {
        /** Cache APIs */
        ConcurrentHashMap<Long, Entry> map = new ConcurrentHashMap<Long, Entry>();
        CacheInterface cache = new ChronicleMapCache(this.serverName);
        environment.addResource(new CacheResource(cache));
        log.info("Loaded resources");

    }
}
