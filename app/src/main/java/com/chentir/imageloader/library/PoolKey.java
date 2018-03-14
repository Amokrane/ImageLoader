package com.chentir.imageloader.library;

import android.support.annotation.NonNull;

public class PoolKey implements Comparable<PoolKey> {
    private final int byteCount;

    public PoolKey(int byteCount) {
        this.byteCount = byteCount;
    }

    public int getByteCount() {
        return byteCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PoolKey poolKey = (PoolKey) o;

        return byteCount == poolKey.byteCount;
    }

    @Override
    public int hashCode() {
        return byteCount;
    }

    @Override
    public int compareTo(@NonNull PoolKey otherObj) {
        if (byteCount < otherObj.getByteCount()) {
            return -1;
        } else if (byteCount == otherObj.getByteCount()) {
            return 0;
        } else {
            return 1;
        }
    }
}