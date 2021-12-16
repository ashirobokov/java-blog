package ru.ashirobokov.blog.cache;

import lombok.extern.slf4j.Slf4j;
import ru.ashirobokov.blog.model.Article;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

/***
 *   Cache eviction policies :
 *   1. Based on cache size (defaultCacheSize);
 *   2. Based on cached time (defaultCachedTime) in seconds;
 *
 *
 */
@Slf4j
public class CacheImpl implements Cache<Object, Object> {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
    private ConcurrentMap<Object, Object> cache = new ConcurrentHashMap<>();
//    private List<Point> cacheStructure = new LinkedList<>();
    private Queue<Point> cacheStructure = new ConcurrentLinkedQueue<>();
    private int defaultCacheSize = 5;
    private int defaultCachedTime = 15; // 3200

    /**
     * Using Singleton static Holder pattern for cache
     */
    private CacheImpl() {
        cacheSizeCheck();
        cachedTimeCheck();
    }

    public static CacheImpl getCache() {
        return CacheImpl.Holder.INSTANCE;
    }

    @Override
    public void put(Object key, Object value) {
        log.info("[Cache] put Object {} with key = {}", value, key);
        cacheStructure.add(new Point(key, System.currentTimeMillis()));
        cache.put(key, value);
    }
//  Cache eviction policies

    @Override
    public Object get(Object key) {
        log.info("[Cache] get by key = {}", key);
        return cache.get(key);
    }

    @Override
    public Object remove(Object key) {
        log.info("[Cache] remove Object with key = {}", key);
        cacheStructure.remove(key);
        return cache.remove(key);
    }

    @Override
    public void clear(Object key) {
        log.info("[Cache] clear Object for key = {}", key);
        cache.put(key, null);
    }

    @Override
    public void clear() {
        log.info("[Cache] clear");
        cacheStructure.clear();
        cache.clear();
    }

    @Override
    public int size() {
        log.info("[Cache] cache size");
        return cache.size();
    }

    private final void cacheSizeCheck() {
        log.info("[Cache-Size-Policy] Cache Size Check started at {}", System.currentTimeMillis());
        final Runnable sizeChecker = () -> {
            int currentCacheSize = cache.size();
            if (currentCacheSize > defaultCacheSize) {
                log.info("[Cache-Size-Policy] Current Cache Size exeeded the allowed cache size. Removing data from cache");
                Point point = cacheStructure.stream().findFirst().get();
                cache.remove(point.getKey());
                cacheStructure.remove(point);
                log.info("[Cache-Size-Policy] Cache Point {} was removed", point);
            } else {
                log.debug("[Cache-Size-Policy] Current Cache Size is not exeeded yet");
            }
        };

        final ScheduledFuture<?> checkerHandle =
                scheduler.scheduleAtFixedRate(sizeChecker, 0, 5, SECONDS);
    }

    private final void cachedTimeCheck() {
        log.info("[Cache-Time-Policy] Cache Time Check started at {}", System.currentTimeMillis());
        final Runnable timeChecker = () -> {
            try {
                long currentTime = System.currentTimeMillis();
                long evictionTime = currentTime - defaultCachedTime * 1000;
                log.debug("[DEBUG] Cache Structure = {}, currentTime = {}, evictionTime = {}", cacheStructure.toString(), currentTime, evictionTime);
                cacheStructure.stream()
                        .takeWhile(point -> point.getSavedTime() < evictionTime)
                        .forEach(point -> {
                                    log.info("[Cache-Time-Policy] Cache Point {} was removed after {} sec",
                                            point, TimeUnit.MILLISECONDS.toSeconds(point.getSavedTime()));
                                    Article article = (Article) cache.remove(point.getKey());
                                    log.debug("[DEBUG] Removed from cache {}", article);
                                    boolean removedOk = cacheStructure.remove(point);
                                    log.debug("[DEBUG] Removed from cacheStructure {}", removedOk);
                                }
                        );
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        final ScheduledFuture<?> checkerHandle =
                scheduler.scheduleAtFixedRate(timeChecker, 0, 5, SECONDS);

    }

    private static class Holder {
        static final CacheImpl INSTANCE = new CacheImpl();

    }

}
