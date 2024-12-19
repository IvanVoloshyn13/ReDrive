package voloshyn.android.data.repository.logs

import android.text.SpannableString

interface DataStringResourceProvider {
    fun fromResToText(input:String,stringId:Int):String

    fun avgFuelConsumptionFormatting(input:CharSequence,stringId:Int):SpannableString
}