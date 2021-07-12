package kz.diaspora.app.utils

import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

object Utils {

    fun dpToPx(dp: Float): Float {
        val metrics = Resources.getSystem().displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun pxToDp(px: Float): Float {
        val metrics = Resources.getSystem().displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun hideKeyboard(view: View) {
        val imm = ContextCompat.getSystemService(view.context, InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}