package voloshyn.android.redrive.presentation.tabs.logs

import android.text.SpannableString

interface StringResourceProvider {
    fun fromResToText(input:String,stringId:Int):String

    fun avgFuelConsumptionFormatting(input:CharSequence,stringId:Int):SpannableString
}