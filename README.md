# URL shortner

This application is a simple URL shortener using Java Spring MVC and Redis.

## Requirements

Minimum:

* JDK 8
* Redis (unecessary if you have docker)

Recommended:

* Docker
* Gradle
* curl
* ApacheBench (for benchmarking)

## Run

### Using docker-compose

By far the easiest way, you can use `docker-compose` to run the service. First,
you need to build it:

```
$ ./gradlew docker
```

Then, you can run it:

```
$ docker-compose up
```

This will run Redis, 3 instances of the application and haproxy load balancer.

### Using plain docker

You can also use plain docker to run redis:

```
$ docker run --name redis -p 6379:6379 -d redis:alpine
```

Then you can build and run the application:

```
$ ./gradlew build
$ ./gradlew bootRun
```

You can also run the service with docker:

```
$ ./gradlew docker
$ docker run --name urlshortener -p 8080:8080 -d --link redis nporcel/url-shortener
```

### Without docker

You can also use plain old java to run the service. First, make sure you have
a `redis` database running as the service will not start if it cannot connect
to redis. You can follow the [Quickstart
guide](https://redis.io/topics/quickstart). Then, change the Redis hostname and
port in `src/main/resources/application.properties`. Finally, build and run the
application:

```
./gradlew build
./gradlew bootRun
```

## Use

To test the application, you can use this script to fill some data:

```
$ ./fill.sh
```

This will fill the cache with to DuckDuckGo image search results for different
animals. After running this command, you can to <http://localhost:8080/2AAAAAA>
and enjoy some cat pictures.

You can also manually insert an url using curl:

```
$ curl -H "Content-Type: application/json" -d'{"url":"http://www.example.com"}' localhost:8080/shorten_url
{"shortened_url":"http://localhost:8080/BAAAAAA"}
```

Then you can visit <http://localhost:8080/BAAAAAA> in your browser.

## Test

To execute the tests, run the following command:

```
$ ./gradlew test
```

## Scaling

There are 3 design components that allow the application to scale. The
scalability model chosen take the assumption that there are few POST requests
to create new shorten URL and many GET requests. In the current desing, 2 of
the 3 components allow POST requests scaling so even if the assumptions are
false it should not be an issue.

### Load balancing

We can run multiple instances of the services to allow load balancing. The
`docker-compose.yml` will run 3 instances of the service by default with
a round robin load balancer. This could theoretically be increased without
limits.

The bottleneck might become the load balancer, so IP or client side load
balancing could be used to increase the performance even further.

### Caching

Each web service has a local cache to store the most recent entries. The cache
size can be set using the `urlshortener.cache.size` setting in
`src/main/resources/application.properties`. Having a dedicated cache per
backend and round robin load balancing allow the most popular URLs to be cached
by all backends instead of just one in case of URL based load balancing. This
should allow us to scale even if a single URL becomes very popular.

### Redis

We use Redis as a key-value store for its high performance. It is used with
append only persistent file written each time a new key is written when using
`docker-compose`. This avoids data loss in case of failure, removing the need
for regular database without any major drawback. However, using writing data to
disk slow down the write process so it could be disabled in case of bottleneck.
However, doing so will result in complete data loss if Redis crashes.

A better way is to use [redis
cluster](https://redis.io/topics/cluster-tutorial). This could scale the read
and write requests and allow better persistence. If enough replica are setup,
persistence could even be removed considering the data stored are not critical
and we can expect to be able to create new nodes faster than they crash.

## Benchmarking

These benchmark have been performed on a single machine after running
`docker-compose up`. They use ApacheBench. The port 9090 is directly targeting
one of the backends while the port 8080 is using haproxy load balancing on
3 backends.

First, targeting the backend:

```
$ ab -n 100000 -c 50 http://localhost:9090/2AAAAAA
Requests per second:    10699.77 [#/sec] (mean)
Time per request:       4.673 [ms] (mean)
```

Then using haproxy:

```
$ ab -n 100000 -c 50 http://localhost:8080/2AAAAAA
Requests per second:    5893.32 [#/sec] (mean)
Time per request:       8.484 [ms] (mean)
```

In both cases, we have more than 1000 requests per second.

Running all backends on a single machine leads to performance degradation when
using haproxy. This degradation might be due to the delay introduced by the
load balancer to the requests. A single backend is able to use all core of the
machine, yielding better performance than multiple backends behind a proxy.
