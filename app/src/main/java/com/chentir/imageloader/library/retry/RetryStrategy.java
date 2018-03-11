package com.chentir.imageloader.library.retry;

import android.support.annotation.NonNull;
import com.chentir.imageloader.library.ImageLoadingRequest;

public interface RetryStrategy {
    /**
     * Calculate the delay for the retry strategy
     */
    long calculateNextRetryTimeInMs(@NonNull ImageLoadingRequest imageLoadingRequest);
}
