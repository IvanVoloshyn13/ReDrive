package com.example.redrive.presentation.logs

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.RefuelLog
import com.example.redrive.R
import com.example.redrive.databinding.RefuelLogItemBinding

class RefuelLogsAdapter(private val listener: LogItemClickListener) :
    ListAdapter<RefuelLog, RefuelLogsViewHolder>(RefuelLogsDiffCallback()), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefuelLogsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RefuelLogItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return RefuelLogsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RefuelLogsViewHolder, position: Int) {
        val log = getItem(position)
        holder.binding.root.tag = log.id
        holder.binding.apply {
            tvAvgConsumption.text = log.avgConsumption.first
            tvAvgConsumptionUnit.text = log.avgConsumption.second
            tvItemRefuelMainInfo.text =
                spannableText(log.odometerReading, log.date, this.tvItemRefuelMainInfo.context)
            tvDistance.text = log.travelledDistance
            tvFuelAmount.text = log.fuelAmount
            tvPayment.text = log.payment
            tvFuelPricePerUnit.text = log.pricePerUnit
        }
    }

    override fun onClick(v: View) {
        listener.onItemClick(v.tag as Long)
    }

    //Maybe in helper class, divide odometerReading by thousands
    private fun spannableText(odometerReading: String, date: String, context: Context): Spannable {
        val message = context.getString(R.string.refuel_message, odometerReading, date)
        val spannable = SpannableString(message)
        val typefaceSemibold = ResourcesCompat.getFont(context, R.font.poppins_semibold)
        val odometerPartStart = message.indexOf(odometerReading)
        val odometerPartEnd = odometerPartStart + odometerReading.length
        spannable.setSpan(
            CustomTypefaceSpan("", typefaceSemibold),
            odometerPartStart,
            odometerPartEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val datePartStart = message.indexOf(date)
        val datePartEnd = datePartStart + date.length
        spannable.setSpan(
            CustomTypefaceSpan("", typefaceSemibold),
            datePartStart,
            datePartEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    interface LogItemClickListener {
        fun onItemClick(itemId: Long)
    }
}

class RefuelLogsViewHolder(val binding: RefuelLogItemBinding) :
    RecyclerView.ViewHolder(binding.root)

class RefuelLogsDiffCallback : DiffUtil.ItemCallback<RefuelLog>() {
    override fun areItemsTheSame(oldItem: RefuelLog, newItem: RefuelLog): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RefuelLog, newItem: RefuelLog): Boolean {
        return oldItem == newItem
    }

}

class CustomTypefaceSpan(family: String, private val typeface: Typeface?) : TypefaceSpan(family) {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        typeface?.let { ds.typeface = it }
    }

    override fun updateMeasureState(paint: TextPaint) {
        super.updateMeasureState(paint)
        typeface?.let { paint.typeface = it }
    }
}