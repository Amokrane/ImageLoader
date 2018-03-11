package com.chentir.imageloader.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
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
        Bitmap bitmap = null;

        try {
            response = okHttpClient.newCall(request).execute();

            // FIXME: response could be null
            if (!response.isSuccessful()) {
                imageLoadingCallback.onError(imageLoadingRequest, new IOException());
            } else {
                InputStream inputStream = response.body().byteStream();
                BitmapFactory.Options options = new BitmapFactory.Options();


                BitmapPool bitmapPool = ImageLoaderModule.getInstance().getBitmapPool();

                if(bitmapPool.hasBeenInitialized()) {
                    bitmap = bitmapPool.take();
                    if(bitmapPool.canUseForInBitmap(bitmap, options)) {
                        options.inBitmap = bitmap;
                    }
                }

                bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                bitmapPool.add(bitmap);
                imageLoadingCallback.onSuccess(imageLoadingRequest, bitmap);
            }
        } catch (IOException e) {
            imageLoadingCallback.onError(imageLoadingRequest, e);
        } finally {
            if (response != null) {
                response.close();
            }

            //if(bitmap != null) {
            //    bitmap.recycle();
            //}
        }

        return null;
    }
}