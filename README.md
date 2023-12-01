# WeatherMe
Android weather application with current weather and forecast
# Screenshots and recordings

![weather_fragment_screenshot](https://github.com/Plovotok/WeatherMe/tree/master/app/src/main/assets/screenshots/weather_fragment_screenshot.png)
![add_location_fragment_screenshot](https://github.com/Plovotok/WeatherMe/tree/master/app/src/main/assets/screenshots/add_location_fragment_screenshot.png)
![widget_screenshot](https://github.com/Plovotok/WeatherMe/tree/master/app/src/main/assets/screenshots/widget.png)

# Recording
![screen_recording](https://github.com/Plovotok/WeatherMe/tree/master/app/src/main/assets/recs/weather_fragment_rec.gif)

# Installation

clone repository using git clone:
```
git clone https://github.com/Plovotok/WeatherMe.git
```

# Configuration
Step 2. Paste your weatherapi.com API-key and MapTiler API_key
![weather_api_logo](https://cdn.weatherapi.com/v4/images/weatherapi_logo.png)
<a href="https://https://cdn.weatherapi.com"> <img src="https://cdn.weatherapi.com/v4/images/weatherapi_logo.png">
</a>
MapTiler: https://cloud.maptiler.com
Find object Constants:
```
    const val API_KEY = "YOUR_WEATHER_API_KEY"
    const val MAP_TILER_KEY = "YOUR_MAPTILER_KEY"
```

Also find html-files inside assets folder and replace
```
    maptilersdk.config.apiKey = 'YOUR_MAPTILER_KEY';
```

# Start
Run the app and add your favourite location by click '+' button. By default Moscow location is being used
# License
```
Copyright 2023 Eugene Plovotok

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
