# RexWeather

RexWeather is a sample Android Studio project demonstrating the use of
Retrofit and RxJava to interact with web services. 
[Link to the blog post](https://vyshane.com/2014/07/02/using-retrofit-and-rxjava-to-interact-with-web-services-on-android/).

![RexWeather Screenwhot](https://vyshane.com/images/rexweather.png)


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


## Web Service Calls

The app uses [OpenWeatherMap](http://openweathermap.org/) to fetch the current
weather as well as the seven day forecast.

Because of the new policy of [OpenWeatherMap](http://openweathermap.org/),
we need to provide API key to the requests we made. You can see the detailed information
in [here](http://openweathermap.org/faq#error401). Please insert your API key that you get
after register from [OpenWeatherMap](http://openweathermap.org/) to WeatherService.java

### Current Weather

The current weather can be obtained by making a call similar to the following:

```
http://api.openweathermap.org/data/2.5/weather?lat=-31.9522&lon=115.8589&units=metric
```

The returned JSON looks like this:

```javascript
{
  "coord": {
    "lon": 115.86,
    "lat": -31.95
  },
  "sys": {
    "message": 0.1843,
    "country": "AU",
    "sunrise": 1404602233,
    "sunset": 1404638729
  },
  "weather": [
    {
      "id": 802,
      "main": "Clouds",
      "description": "scattered clouds",
      "icon": "03n"
    }
  ],
  "base": "cmc stations",
  "main": {
    "temp": 13,
    "pressure": 1015,
    "humidity": 71,
    "temp_min": 13,
    "temp_max": 13
  },
  "wind": {
    "speed": 3.1,
    "deg": 300
  },
  "rain": {
    "3h": 0
  },
  "clouds": {
    "all": 40
  },
  "dt": 1404657000,
  "id": 6692202,
  "name": "South Perth",
  "cod": 200
}
```

### 7 Day Forecast

The 7 day forecast can be obtained like so:

```
http://api.openweathermap.org/data/2.5/forecast/daily?lat=-31.9522&lon=115.8589&mode=json&units=metric&cnt=7
```

And the returned JSON looks like this:

```javascript
{
  "cod": "200",
  "message": 0.0094,
  "city": {
    "id": 2063523,
    "name": "South Perth",
    "coord": {
      "lon": 115.833328,
      "lat": -31.933331
    },
    "country": "AU",
    "population": 0
  },
  "cnt": 7,
  "list": [
    {
      "dt": 1404619200,
      "temp": {
        "day": 13,
        "min": 13,
        "max": 13.31,
        "night": 13.31,
        "eve": 13,
        "morn": 13
      },
      "pressure": 1016.99,
      "humidity": 100,
      "weather": [
        {
          "id": 501,
          "main": "Rain",
          "description": "moderate rain",
          "icon": "10d"
        }
      ],
      "speed": 7.25,
      "deg": 225,
      "clouds": 92,
      "rain": 4
    },
  ]

  // Six more entries ...

}
```
