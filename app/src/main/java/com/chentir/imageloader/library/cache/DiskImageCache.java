package com.chentir.imageloader.library.cache;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

class DiskImageCache implements ImageCache {

    @Override
    public Bitmap fetch(String url) {
        return null;
    }

    @Override
    public void clear() {
        // TODO
    }

    @Override
    public void onAdjustCapacity(@NonNull SystemResourceAvailability systemResourceAvailability) {
        // TODO
    }
}
