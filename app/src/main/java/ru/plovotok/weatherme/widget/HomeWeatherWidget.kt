package ru.plovotok.weatherme.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.R
import ru.plovotok.weatherme.WeatherService
import ru.plovotok.weatherme.presentation.base.PrecipRate
import ru.plovotok.weatherme.presentation.base.ScreenWeather
import ru.plovotok.weatherme.presentation.base.TypeOfPrecip
import ru.plovotok.weatherme.presentation.base.defineWeatherByCondition
import javax.inject.Inject

/**
 * Implementation of App Widget functionality.
 */
@AndroidEntryPoint
class HomeWeatherWidget : AppWidgetProvider() {
    companion object {
        const val EXTRA_LOAD_FROM_WEB = "EXTRA_LOAD_FROM_WEB"
    }

    var loadFromWeb: Boolean = false

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    @Inject
    lateinit var myWeatherService : WeatherService

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
//        Toast.makeText(context, myWeatherService.toString(), Toast.LENGTH_SHORT).show()
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, myWeatherService)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
//        for (appWidgetId in appWidgetIds) {
//            deleteTitlePref(context, appWidgetId)
//        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    weatherService: WeatherService
) {
    CoroutineScope(Dispatchers.Main).launch {
        val forecast = weatherService.getForecast()
        val current = forecast?.current
        val condition = forecast?.current?.condition

        val screenWeather = if (current != null && condition != null) {
            defineWeatherByCondition(condition.code, current.isDay)
        } else {
            ScreenWeather(R.drawable.sunny_day, R.drawable.header_background, TypeOfPrecip.CLEAR, PrecipRate.CLEAR)
        }

//        val widgetText = loadTitlePref(context, appWidgetId)
        val temp = "${forecast?.current?.temp ?: 0}°"
        val location = forecast?.location?.name ?: "Moscow"
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.home_weather_widget)

//        Toast.makeText(context, "UPDATE", Toast.LENGTH_SHORT).show()
//        val myIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, context, HomeWidget::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_IMMUTABLE)
//        val pendingIntent = PendingIntent.getActivity(
//            context,
//            0,
//            myIntent,
//            PendingIntent.FLAG_IMMUTABLE
//        )
        views.setOnClickPendingIntent(R.id.home_widget_refresh_button, WidgetUtils().requestWidgetUpdatePendingIntent(context, true))

        views.setTextViewText(R.id.home_widget_temp_text, temp)
        views.setTextViewText(R.id.home_widget_location, location)

        views.setImageViewResource(R.id.home_widget_cond_icon, screenWeather.iconResource)
//        views.

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)

    }


}