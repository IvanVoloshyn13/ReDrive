package voloshyn.android.redrive.presentation.tabs.logs

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import javax.inject.Inject

class StringResProviderImpl @Inject constructor(private val context: Context) :
    StringResourceProvider {
    override fun fromResToText(input: String, stringId: Int): String {
        val format = context.getString(stringId)
        return "$input $format"
    }

    override fun avgFuelConsumptionFormatting(input: CharSequence, stringId: Int): SpannableString {
        val unit=context.getString(stringId)
        val spannable = SpannableString("$input\n$unit")
        spannable.setSpan(
            RelativeSizeSpan(0.5f),
            input.length+1 ,
            spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

      return  spannable
    }
}