# PCars2OpenweatherServer

The is a Java (spring boot) application that acts as a webserver for openweather api requests. This is a work-around for pCars2's expired API key, and is also
able to serve its own weather.

It works by intercepting calls to api.openweather.org (using the system's hosts file) and either responding itself or relaying the request to the real
server but with a different (working) API key.


## Building and running

There's a pre-built uber-jar file (jar with all dependencies) in the /build folder, or you can build it yourself with

```
mvn package
```
You need to add this entry to your hosts file - /etc/hosts (linux) or c:\Windows\System32\drivers\etc\hosts (windows):

```
127.0.0.1 api.openweathermap.org
```
This redirects pCars2's openweather API queries to the local system.


### Standalone mode

Run the app with

```
java -jar weather-api-0.1.0-spring-boot.jar
```
The app will serve its own weather data in this mode (current conditions and forecast).


### Proxy mode

To run as a proxy server to the real weather API, you need the IP address of the weather server (get it from 'ping -a api.openweathermap.org' or 'nslookup api.openweathermap.org').
If you use the URL here (api.openweathermap.org) it'll get redirected by the system hosts file back to the app.

You'll also need an API key - this can be obtained for free from openweathermap.org. They limit the number of queries so I recommend you don't share your key.

```
java -jar weather-api-0.1.0-spring-boot.jar --weather.proxy.enabled=true --weather.proxy.url=http://[weather server IP address] --weather.proxy.user.appId=[your app id]
```

### Weather generation

In standalone mode, the app will create its own weather automatically if none is set. It does this per-location, creating an 8 hour weather set for the location requested.
Note that currently this doesn't take the type of location (desert, Scotland, etc) into account but will eventually.

You can create your own conditions by invoking the endpoint (as a GET request - use Curl or just put the URL into a browser):

```
http://localhost/weather/create/slots?slotLength=20&slot=RAIN&slot=LIGHT_RAIN&slot=OVERCAST etc etc
```

slotLength is in minutes, slot can be one of:
```
CLEAR, SCATTERED_CLOUD, CLOUD, THICK_CLOUD, OVERCAST, LIGHT_DRIZZLE, DRIZZLE, HEAVY_DRIZZLE, LIGHT_RAIN, RAIN, HEAVY_RAIN, VERY_HEAVY_RAIN, THUNDERSTORM, HAZE, MIST, FOG
```
The app will scale temperature and wind according to the slot type.

This will create the sequence of weather that will apply to every location. If you want to create weather for a specific location only, specify
the latitude and longitude on the URL - 

```
http://localhost/weather/create/slots?latitude=12.34&longitude=56.78&slotLength=20&slot=RAIN&slot=LIGHT_RAIN&slot=OVERCAST
```

If you do this, the app will only return this weather sequence when the game requests weather for this location (the latitude / longitude match is to 2 decimal places).


You can create more specific weather by sending a JSON payload as a POST to

http://localhost/weather/create/condition

Again, lat and lon are optional parameters and work the same way as for the slots endpoint. The payload (currently...) looks like this:
(temps are in kelvin, precipitation is 0 [dry] - 1 [monsoon], pressure is in mmHg, humidity is %, wind speed is in m/s, direction is in degrees and visibility is in metres)

```
{
	"minutesBetweenSamples": 20,
	"conditions": [{
		"temperature": 273,
		"precipitation": 0.5,
		"pressure": 1000,
		"humidity": 50,
		"clouds": 20,
		"windSpeed": 10,
		"windDirection": 100,
		"visibility": 10000
	}, {
		"temperature": 274,
		"precipitation": 0.8,
		"pressure": 980,
		"humidity": 50,
		"clouds": 20,
		"windSpeed": 10,
		"windDirection": 100,
		"visibility": 10000
	}, {
		"temperature": 275,
		"precipitation": 1,
		"pressure": 990,
		"humidity": 100,
		"clouds": 20,
		"windSpeed": 10,
		"windDirection": 100,
		"visibility": 5000
	}, {
		"temperature": 276,
		"precipitation": 0.5,
		"pressure": 1000,
		"humidity": 50,
		"clouds": 20,
		"windSpeed": 10,
		"windDirection": 100,
		"visibility": 2000
	}, {
		"temperature": 277,
		"precipitation": 0.5,
		"pressure": 1000,
		"humidity": 50,
		"clouds": 20,
		"windSpeed": 10,
		"windDirection": 100,
		"visibility": 1000
	}]
}
```