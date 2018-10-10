# PCars21OpenweatherServer

Build with

mvn package

You need to add this entry to your hosts file - /etc/hosts (linux) or c:\Windows\System32\drivers\etc\hosts (windows):
127.0.0.1 api.openweathermap.org

This redirects pCars2's openweather API queries to the local system.

Run the app with
java -jar weather-api-0.1.0-spring-boot.jar


To run as a proxy server to the real weather API, you need the IP address of the weather server (get it from 'ping -a api.openweathermap.org' or 'nslookup api.openweathermap.org').
You'll also need an API key - this can be obtained from openweathermap.org.

java -jar weather-api-0.1.0-spring-boot.jar --weather.proxy.enabled=true --weather.proxy.url=http://[weather server IP address] --weather.proxy.user.appId=[your app id]