package voloshyn.android.redrive.presentation.tabs.logs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import voloshyn.android.app.databinding.RefuelLogItemBinding
import voloshyn.android.domain.models.logs.RefuelLog

class RefuelLogsAdapter() :
    ListAdapter<RefuelLog, RefuelLogsViewHolder>(RefuelLogsDiffCallback()), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefuelLogsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RefuelLogItemBinding.inflate(inflater,parent,false)
        binding.root.setOnClickListener(this)
        return RefuelLogsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RefuelLogsViewHolder, position: Int) {
       val log= getItem(position)
          holder.binding.apply {
              tvLastAvgConsumptionValue.text=log.avgFuelConsumption
              distance.text=log.travelledDistance
              fuelAmount.text=log.fuelVolume
              refuelPayment.text=log.payments
              fuelPricePerUnit.text=log.unitPrice

        }
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
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