package voloshyn.android.redrive.presentation.tabs.vehicles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import voloshyn.android.app.databinding.VehicleItemBinding
import voloshyn.android.domain.models.Vehicle

class VehiclesAdapter(private val listener: OnVehicleItemClickListener) :
    ListAdapter<Vehicle, VehicleViewHolder>(VehicleDiffCallback()), View.OnClickListener {

    override fun onClick(v: View?) {
        listener.onClick(v?.tag as Vehicle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = VehicleItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return VehicleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = getItem(position)
        with(vehicle) {
            holder.binding.tvVehicleName.text = this.name
            holder.binding.root.tag = vehicle
        }
    }

    interface OnVehicleItemClickListener {
        fun onClick(vehicle: Vehicle)
    }


}

class VehicleViewHolder(val binding: VehicleItemBinding) : RecyclerView.ViewHolder(binding.root)

class VehicleDiffCallback : DiffUtil.ItemCallback<Vehicle>() {
    override fun areItemsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
        return oldItem == newItem
    }


}



