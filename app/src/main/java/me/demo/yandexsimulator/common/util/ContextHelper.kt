package me.demo.yandexsimulator.common.util

import android.app.Activity
import android.content.ContextWrapper
import android.content.res.Resources
import android.view.View

object ContextHelper {
    fun invokeActivityFromView(view: View): Activity? {
        var activity: Activity? = null
        var context = view.context
        while (context is ContextWrapper) {
            if (context is Activity) activity = context
            context = context.baseContext
        }
        return activity
    }

}