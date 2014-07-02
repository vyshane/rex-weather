# RexWeather

RexWeather is a sample Android Studio project demonstrating the use of
Retrofit and RxJava to interact with web services.

![RexWeather Screenwhot](http://www.node.mu/images/rexweather.png)


## Retrofit, by Square

[Retrofit](http://square.github.io/retrofit) is a REST client for Android and
Java. It allows you to turn a REST API into a Java interface by using
annotations to describe the HTTP requests. It can then generate an
implementation of the interface for you. This means that you can go from
`GET /users/{userId}/posts` to `webService.fetchUserPosts(userId)` in a few
lines of code. Retrofit is very easy to use and it integrates well with RxJava.

## RxJava, by Netflix

[RxJava](https://github.com/Netflix/RxJava) is a Java implementation of Rx, the
Reactive Extensions library from the .NET world. It allows you to compose
asynchronous and event-based programs in a declarative manner. Let's say that
you want to implement something like this:

* Start observing our current location. When a location change happens, in
  parallel,
    * Send a web service request A
    * Send a web service request B
        * Using the result from B, send a web service request C
* When the results for both A and C are back, update the UI

RxJava allows you to write out your program pretty much as above. It abstracts
out concerns about things like threading, synchronization, thread-safety,
concurrent data structures, and non-blocking I/O. You can tell RxJava to
observe the location changes and perform the web service requests in a
background thread, and pass you the final results in the UI thread. If any
exceptions are thrown at any point in the chain of events, you get told about
it in one convenient place. You're not drowning in a spaghetti of onSuccess and
onError callbacks. You're building pipelines.

