package com.chentir.imageloader.library.retry;

import android.support.annotation.NonNull;
import com.chentir.imageloader.library.ImageLoadingRequest;
import java.util.HashMap;
import java.util.Map;

public class ExponentialBackoff implements RetryStrategy {

    private static final int BASE_TIME_IN_SECONDS = 2;

    private final Map<ImageLoadingRequest, Integer> retryHistory = new HashMap<>();

    /**
     * Calculate the next retry time according to an exponential backoff strategy
     *
     * @impl this method also modifies the {@link #retryHistory} map to keep track of the last exponent used for
     * calculating the latest retry time for a given {@code imageUrl}
     */
    @Override
    public long calculateNextRetryTimeInMs(@NonNull ImageLoadingRequest imageLoadingRequest) {
        long nextRetryTime;
        Integer lastExponent = retryHistory.get(imageLoadingRequest);

        if (lastExponent == null) {
            nextRetryTime = (long) Math.pow(BASE_TIME_IN_SECONDS, 0);
            retryHistory.put(imageLoadingRequest, 1);
        } else {
            nextRetryTime = (long) Math.pow(BASE_TIME_IN_SECONDS, lastExponent);
            lastExponent += 1;
            retryHistory.put(imageLoadingRequest, lastExponent);
        }

        return nextRetryTime * 1000;
    }
}
