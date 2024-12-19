package voloshyn.android.data.repository.logs

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DataStringResProviderImpl @Inject constructor(@ApplicationContext private val context: Context) :
    DataStringResourceProvider {
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