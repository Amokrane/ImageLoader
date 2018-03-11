package com.chentir.imageloader.library;

/**
 * Constants defining default library configurations
 */
public class Configuration {
    /** In case of a download failure, retry every 200 ms */
    public final static long LINEAR_RETRY_DELAY_MILLIS = 200;

    /** Only keep 20 images at most in the cache (this value should be defined experimentally, on an average device) */
    /** TODO: How about using SoftReference for caching? Can PhantomReference be also considered? */
    public static final int MAX_CACHE_SIZE = 20;
}
