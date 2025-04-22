package com.example.redrive.core.logTextFormatter

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.TypefaceSpan
import androidx.core.content.res.ResourcesCompat
import com.example.redrive.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogSpannableTextCreator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val messageProvider: RefuelMessageFromResProvider
) {
    private val typefaceSemibold by lazy {
        ResourcesCompat.getFont(context, R.font.poppins_semibold)
    }

    fun spannableText(odometerReading: String, date: String): Spannable {
        val message = messageProvider.getMessage(odometerReading, date)
        val builder = SpannableStringBuilder(message)
        listOf(odometerReading, date).forEach { part ->
            val start = message.indexOf(part).takeIf { it >= 0 } ?: return@forEach
            val end = start + part.length
            builder.setSpan(
                CustomTypefaceSpan(typefaceSemibold),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return builder
    }
}

private class CustomTypefaceSpan(private val typeface: Typeface?) :
    TypefaceSpan("") {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        typeface?.let { ds.typeface = it }
    }

    override fun updateMeasureState(paint: TextPaint) {
        super.updateMeasureState(paint)
        typeface?.let { paint.typeface = it }
    }
}