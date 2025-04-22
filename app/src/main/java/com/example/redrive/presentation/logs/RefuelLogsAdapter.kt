package com.example.redrive.presentation.logs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.log.RefuelLog
import com.example.redrive.core.logTextFormatter.LogSpannableTextCreator
import com.example.redrive.databinding.RefuelLogItemBinding

class RefuelLogsAdapter(
    private val listener: LogItemClickListener,
    private val spannableTextCreator: LogSpannableTextCreator
) :
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
            tvAvgConsumption.text = log.avgConsumption.value
            tvAvgConsumptionUnit.text = log.avgConsumption.unit
            tvItemRefuelMainInfo.text =
              spannableTextCreator.spannableText(log.odometerReading, log.date)
            tvDistance.text = log.travelledDistance
            tvFuelAmount.text = log.fuelAmount
            tvPayment.text = log.payment
            tvFuelPricePerUnit.text = log.pricePerUnit
        }
    }

    override fun onClick(v: View) {
        listener.onLogItemClick(v.tag as Long)
    }


    interface LogItemClickListener {
        fun onLogItemClick(itemId: Long)
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
