package com.chentir.imageloader.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class DownloadImageTask implements Callable<Void> {
    private final ImageLoadingRequest imageLoadingRequest;
    private final ImageLoadingCallback imageLoadingCallback;

    public DownloadImageTask(@NonNull ImageLoadingRequest imageLoadingRequest,
        ImageLoadingCallback imageLoadingCallback) {
        this.imageLoadingRequest = imageLoadingRequest;
        this.imageLoadingCallback = imageLoadingCallback;
    }

    @Override
    public Void call() throws Exception {
        Request request = new Request.Builder().url(imageLoadingRequest.getImageUrl()).build();
        ImageLoaderModule imageLoaderModule = ImageLoaderModule.getInstance();
        OkHttpClient okHttpClient = imageLoaderModule.getOkHttpClient();

        Response response = null;
        Bitmap bitmap;

        try {
            response = okHttpClient.newCall(request).execute();

            if (response == null || !response.isSuccessful()) {
                imageLoadingCallback.onError(imageLoadingRequest, new IOException());
            } else {
                InputStream inputStream = response.body().byteStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                BitmapFactory.Options options = new BitmapFactory.Options();

                options.inSampleSize = 1;
                options.inJustDecodeBounds = true;

                //  Mark the inputstream so as to allow resetting it later. This is necessary for decoding the stream twice.
                if (bufferedInputStream.markSupported()) {
                    bufferedInputStream.mark(Integer.MAX_VALUE);
                }

                // First call to decodeStream, to set the width and height on the BitmapFactory.Options object
                BitmapFactory.decodeStream(bufferedInputStream, null, options);

                BitmapPool bitmapPool = ImageLoaderModule.getInstance().getBitmapPool();

                if (bitmapPool.hasBeenInitialized()) {
                    bitmap = bitmapPool.take();
                    if (bitmapPool.canUseForInBitmap(bitmap, options)) {
                        options.inBitmap = bitmap;
                    }
                }

                options.inJustDecodeBounds = false;
                bufferedInputStream.reset();

                // Second call to decodeStream, used to create the actual Bitmap
                bitmap = BitmapFactory.decodeStream(bufferedInputStream, null, options);
                imageLoadingCallback.onSuccess(imageLoadingRequest, bitmap);
                bitmapPool.putBack(bitmap);
            }
        } catch (IOException e) {
            imageLoadingCallback.onError(imageLoadingRequest, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return null;
    }
}