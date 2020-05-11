package com.byfrunze.memshelper.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.byfrunze.memshelper.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Implementation of App Widget functionality.
 */
open class MemWidget : AppWidgetProvider() {

    private val SYNC_CLICKED = "memwidget_update_action"
    private val WAITING_MESSAGE = "Wait for MEM"

    val APP_PREFERENCES = "mysettings"
    val APP_PREFERENCES_QUOTE = "quote"

    val RB_ADVICE = "rb_advice"
    val RB_PROG = "rb_prog"
    val RB_CHACK = "rb_chack"
    val RB_MOT = "rb_motivation"

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        val remoteViews = RemoteViews(context.packageName, R.layout.mem_widget)
        val watchWidget = ComponentName(context, MemWidget::class.java)
        // There may be multiple widgets active, so update all of them

        remoteViews.setOnClickPendingIntent(
            R.id.ll_widget,
            getPendingSelfIntent(context, SYNC_CLICKED)
        )
        appWidgetManager.updateAppWidget(watchWidget, remoteViews)

        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun getPendingSelfIntent(
        context: Context?,
        action: String?
    ): PendingIntent? {
        val intent = Intent(context, javaClass)
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (SYNC_CLICKED == intent?.action) {
            context?.let {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val remoteViews = RemoteViews(context.packageName, R.layout.mem_widget)
                val watchWidget = ComponentName(context, MemWidget::class.java)
                remoteViews.setTextViewText(R.id.appwidget_text_eng, WAITING_MESSAGE)
                appWidgetManager.updateAppWidget(watchWidget, remoteViews)

                val settings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
                when (settings.getString(APP_PREFERENCES_QUOTE, "none")) {
                    RB_ADVICE -> {
                        RequestWidget().loadQuotesAdvice()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                remoteViews.setTextViewText(R.id.appwidget_text_eng, it.slip.advice)
                                translate(
                                    view = remoteViews,
                                    manager = appWidgetManager,
                                    txt = it.slip.advice,
                                    watchWidget = watchWidget
                                )
                                appWidgetManager.updateAppWidget(watchWidget, remoteViews)

                            }, {
                                Log.i("WIDGET", it.localizedMessage)
                            })
                    }
                    RB_CHACK -> {
                        RequestWidget().loadQuotesChack()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                remoteViews.setTextViewText(R.id.appwidget_text_eng, it.value)
                                translate(
                                    view = remoteViews,
                                    manager = appWidgetManager,
                                    txt = it.value,
                                    watchWidget = watchWidget
                                )
                                remoteViews.setTextViewText(R.id.appwidget_text_author, "Чак Норрис")
                                appWidgetManager.updateAppWidget(watchWidget, remoteViews)

                            }, {
                                Log.i("WIDGET", it.localizedMessage)
                            })
                    }
                    RB_MOT -> {
                        RequestWidget().loadQuotesMot()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                remoteViews.setTextViewText(R.id.appwidget_text_eng, it.quoteText)
                                translate(
                                    view = remoteViews,
                                    manager = appWidgetManager,
                                    txt = it.quoteText,
                                    watchWidget = watchWidget
                                )
                                remoteViews.setTextViewText(R.id.appwidget_text_author, it.quoteAuthor)
                                appWidgetManager.updateAppWidget(watchWidget, remoteViews)

                            }, {
                                Log.i("WIDGET", it.localizedMessage)
                            })
                    }
                    RB_PROG -> {
                        RequestWidget().loadQuotesProg()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                remoteViews.setTextViewText(R.id.appwidget_text_eng, it.en)
                                translate(
                                    view = remoteViews,
                                    manager = appWidgetManager,
                                    txt = it.en,
                                    watchWidget = watchWidget
                                )
                                remoteViews.setTextViewText(R.id.appwidget_text_author, it.author)
                                appWidgetManager.updateAppWidget(watchWidget, remoteViews)
                            }, {
                                Log.i("WIDGET", it.localizedMessage)
                            })
                    }
                    else -> {
                        remoteViews.setTextViewText(R.id.appwidget_text_eng, "Error")
                        remoteViews.setTextViewText(R.id.appwidget_text_ru, "Error")
                        remoteViews.setTextViewText(R.id.appwidget_text_author, "Error")
                        appWidgetManager.updateAppWidget(watchWidget, remoteViews)

                    }
                }

            }

        }

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
    appWidgetId: Int
) {
    val APP_PREFERENCES = "mysettings"
    val APP_PREFERENCES_QUOTE = "quote"

    val RB_ADVICE = "rb_advice"
    val RB_PROG = "rb_prog"
    val RB_CHACK = "rb_chack"
    val RB_MOT = "rb_motivation"
    // Construct the RemoteViews object
    val remoteViews = RemoteViews(context.packageName, R.layout.mem_widget)
    val settings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    when (settings.getString(APP_PREFERENCES_QUOTE, "none")) {
        RB_ADVICE -> {
            RequestWidget().loadQuotesAdvice()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    remoteViews.setTextViewText(R.id.appwidget_text_eng, it.slip.advice)
                    translate(
                        view = remoteViews,
                        manager = appWidgetManager,
                        txt = it.slip.advice,
                        appWidgetId = appWidgetId
                    )
                    appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
                }, {
                    Log.i("WIDGET", it.localizedMessage)
                })
        }
        RB_CHACK -> {
            RequestWidget().loadQuotesChack()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    remoteViews.setTextViewText(R.id.appwidget_text_eng, it.value)
                    translate(
                        view = remoteViews,
                        manager = appWidgetManager,
                        txt = it.value,
                        appWidgetId = appWidgetId
                    )
                    remoteViews.setTextViewText(R.id.appwidget_text_author, "Чак Норрис")
                    appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
                }, {
                    Log.i("WIDGET", it.localizedMessage)
                })
        }
        RB_MOT -> {
            RequestWidget().loadQuotesMot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    remoteViews.setTextViewText(R.id.appwidget_text_eng, it.quoteText)
                    translate(
                        view = remoteViews,
                        manager = appWidgetManager,
                        txt = it.quoteText,
                        appWidgetId = appWidgetId
                    )
                    remoteViews.setTextViewText(R.id.appwidget_text_author, it.quoteAuthor)
                    appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
                }, {
                    Log.i("WIDGET", it.localizedMessage)
                })
        }
        RB_PROG -> {
            RequestWidget().loadQuotesProg()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    remoteViews.setTextViewText(R.id.appwidget_text_eng, it.en)
                    translate(
                        view = remoteViews,
                        manager = appWidgetManager,
                        txt = it.en,
                        appWidgetId = appWidgetId
                    )
                    remoteViews.setTextViewText(R.id.appwidget_text_author, it.author)
                    appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
                }, {
                    Log.i("WIDGET", it.localizedMessage)
                })
        }
        else -> {
            remoteViews.setTextViewText(R.id.appwidget_text_eng, "Error")
            remoteViews.setTextViewText(R.id.appwidget_text_ru, "Error")
            remoteViews.setTextViewText(R.id.appwidget_text_author, "Error")
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }
    appWidgetManager.updateAppWidget(appWidgetId, remoteViews)

    // Instruct the widget manager to update the widget
}

fun translate(
    view: RemoteViews, manager: AppWidgetManager,
    txt: String, appWidgetId: Int
) {
    RequestWidget().loadTranslate(txt)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            view.setTextViewText(R.id.appwidget_text_ru, it.text[0])
            manager.updateAppWidget(appWidgetId, view)

        }, {
            Log.i("WIDGET", it.localizedMessage)
        })
}

fun translate(
    view: RemoteViews, manager: AppWidgetManager,
    txt: String, watchWidget: ComponentName
) {
    RequestWidget().loadTranslate(txt)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            view.setTextViewText(R.id.appwidget_text_ru, it.text[0])
            manager.updateAppWidget(watchWidget, view)

        }, {
            Log.i("WIDGET", it.localizedMessage)
        })
}