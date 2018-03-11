- Should we have a bounded queue or an unbounded one?
- We need to have know when to eject images from the disk cache.
- Should we use Store for the disk cache or should we handle this ourselves?
- Provide a builder to customize the image loader (retry strategy, cache configuration...)
- Adaptive bitrate?
- Parallelize downloading of images using the multiple cores?
- Is Bitmap the best format to cache the images? Should we have it returned everywhere? Or Should it be parameterized?
- Think about downsizing and resampling
- Placeholder? Download a low res image first?

- Execution policy:
* Do we need strong sequential execution guarantees?
* Use a DelayQueue? It lets you take an element only if its delay has expired. This will allow us to handle retries. The pb of DelayQueue is that it's unbounded.
* Since the tasks are largely IO bounds, does it make sense to have concurrency? Yes, if threads can be scheduled on different CPU cores. No if not.
* Do we need to submit a task and wait for its result (with Future or Callable and CompletionService) or do we just let the task have a side effect on a shared datastructure or disk? What happens for example during cancellations? Also, how do we notify upstream that we are done downloading the image?
* How do we dimension the bounded queue?