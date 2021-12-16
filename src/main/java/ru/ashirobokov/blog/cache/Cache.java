package ru.ashirobokov.blog.cache;

public interface Cache<K, V> {
    /**
     * Put the value with specified key
     * @param key
     * @param value
     */
    void put(K key, V value);

    /**
     * Get the value for specified key
     * @param key
     * @return value
     */
    V get(K key);

    /**
     * Remove the value with specified key
     * @param key
     * @return
     */
    V remove(Object key);

    /**
     * Set null value for specified key
     * @param key
     */
    void clear(K key);

    /**
     * Clear the cache
     */
    void clear();

    /**
     *
     * @return cache size
     */
    int size();

}
