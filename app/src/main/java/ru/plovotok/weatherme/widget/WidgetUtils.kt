package ru.plovotok.weatherme.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent

class WidgetUtils {

    private fun widgetUpdateIntent(context: Context, loadFromWeb: Boolean): Intent {
        val provider = ComponentName(context, HomeWeatherWidget::class.java)
        val widgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(provider)

        val intent = Intent(
            AppWidgetManager.ACTION_APPWIDGET_UPDATE,
            null,
            context,
            HomeWeatherWidget::class.java
        )

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds)
        intent.putExtra(HomeWeatherWidget.EXTRA_LOAD_FROM_WEB, loadFromWeb)
        return intent
    }

    fun requestWidgetUpdate(context: Context, loadFromWeb: Boolean) {
        val intent = widgetUpdateIntent(context, loadFromWeb)
        context.sendBroadcast(intent)
    }

    fun requestWidgetUpdatePendingIntent(context: Context, loadFromWeb: Boolean): PendingIntent {
        val intent = widgetUpdateIntent(context, loadFromWeb)
        return PendingIntent.getBroadcast(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}