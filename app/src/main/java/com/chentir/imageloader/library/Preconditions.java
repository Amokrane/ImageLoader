package com.chentir.imageloader.library;

import android.os.Looper;

class Preconditions {
    private Preconditions() {
    }

    public static <T> T checkNotNull(T target, String errorMessage) {
        if (target == null) {
            throw new NullPointerException(errorMessage);
        }

        return target;
    }

    public static void ensureMainThread() {
        if (!Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
            throw new IllegalStateException("The current execution path was not running on the Main Thread");
        }
    }
}
