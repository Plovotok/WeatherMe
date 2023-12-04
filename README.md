# WeatherMe
Android weather application with current weather and forecast
# Screenshots

<img src="/media/weather_fragment_screenshot.png" width="400"/> <img src="/media/add_location_fragment_screenshot.png" width="400"/> 

# Recording
<img src="/media/weather_fragment_rec.gif" width="400" />

## Widget

<img src="/media/widget.png" width="400"/> 

# Installation

clone repository using git clone:
```
git clone https://github.com/Plovotok/WeatherMe.git
```

# Configuration
Step 2. Create and paste your weatherapi.com API-key and MapTiler API-key

<a href="https://www.weatherapi.com/"> <img src="https://cdn.weatherapi.com/v4/images/weatherapi_logo.png">
</a>

<a href="https://www.maptiler.com/"> <img src="https://cloud.maptiler.com/static/img/logo/maptiler-logo-adaptive-cloud.svg">
</a>

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

# Design and technologies

## Design
[👉 Design idea 👈](https://www.figma.com/community/file/1249443729401540968/google-weather-app-redesign)

## Libraries
- Ktor-client
- Room
- Hilt
- Navigation Component
- Lottie
- Glide
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
- [WeatherView](https://github.com/MatteoBattilana/WeatherView)
- [Bouncy](https://github.com/valkriaine/Bouncy)

## Custom View
- [SunStateView](https://github.com/Plovotok/WeatherMe/blob/master/app/src/main/java/ru/plovotok/weatherme/presentation/custom/SunStateView.kt)

## Home Widget
- [HomeWeatherWidget](https://github.com/Plovotok/WeatherMe/blob/master/app/src/main/java/ru/plovotok/weatherme/widget/HomeWeatherWidget.kt)

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
