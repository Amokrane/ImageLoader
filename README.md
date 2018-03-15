# ImageLoader

ImageLoader is a library that simplifies the problem of loading remote images within an Android application. This code base is strictly for learning purposes. Great libraries exist to solve this problem, like Glide (https://github.com/bumptech/glide), Picasso (https://github.com/square/picasso) or Fresco (https://github.com/facebook/fresco).
    
## Features
* Asynchronous loading of images
* In memory caching of already downloaded images
* A pool of Bitmap objects to reduce memory footprint
* System Memory awareness, by trimming cache memory usage when the system starts running low on memory
* Automatic retries, with exponential backoff, when failing to download images


## How to use it

This is the one liner piece of code that can be written to load a remote image: 

```java
ImageLoader imageLoader = ImageLoader.getInstance(aCcontext).load(anImageView, "http://link_to_some_image_resource");
```

It is recommended to request the ImageLoader to cancel pending requests when leaving the scope of the foreground component 
that is  currently loading the Images.
 
To do that simply use:

```java
ImageLoader.getInstance(aCcontext).cancelRequest(anImageView);
```

## Future improvements

This library is far from being complete. This is a non-exhaustive list of improvements that could be considered:

* **Configurability**: There is no way to configure ImageLoader before using it. While there are merits
 in offering a turn key experience via a set of default configuration values, it is always a good idea to let power users 
 customize certain parameters like: the retry strategy, the caching strategy, a placeholder etc.
     
* **Sampling big images**: For now, there is no support for loading big images that don't fit into memory. Sampling the
 source image can be used to cope with this problem.
  
* **Disk cache**: The only available cache strategy for now is in-memory caching. An additional layer of cache can be 
 offered when entries are evicted from the memory cache or if the entries are garbage collected.

* **Better memory management** The current implementation assumes that Bitmap objects are allocated exclusively on the heap. However, this is only true for devices running Android versions from API level 11 (Android 3.0) to API level 25 (Android 7.1). Outside this range, the backing pixel data are allocated on native memory. The implication of that is the necessity of recycling bitmaps manually in the latter case. This documentation page explains this very well: (https://developer.android.com/topic/performance/graphics/manage-memory.html)
  
* **A better Bitmap pool**: The current Bitmap pooling strategy can be improved. First, the capacity of the pool should not
be fixed but calculated dynamically as a factor of memory availability on the device. Additionally, if the bitmap pool
can no longer take Bitmaps, it would be interesting to evict the least recently used size-grouped Bitmaps.
   
* **More testing**: The current code base does not have any unit tests. 

## Note
Don't use this in production. Instead use more battle-tested libraries like Glide (https://github.com/bumptech/glide), Picasso (https://github.com/square/picasso) or Fresco (https://github.com/facebook/fresco).
