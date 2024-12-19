package voloshyn.android.redrive.presentation.tabs.vehicles

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import voloshyn.android.app.R
import voloshyn.android.app.databinding.VehicleItemBinding
import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.models.VehicleType

class VehiclesAdapter(private val listener: VehicleActionsListener) :
    ListAdapter<Vehicle, VehicleViewHolder>(VehicleDiffCallback()), View.OnClickListener {
    private lateinit var popupMenu: PopupMenu

    override fun onClick(v: View) {
        val vehicle = v.tag as Vehicle

        when (v.id) {
            R.id.iv_more -> showPopUpMenu(view = v)
            else -> listener.onItemClick(vehicle)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = VehicleItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        binding.ivMore.setOnClickListener(this)
        return VehicleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = getItem(position)
        holder.bind(vehicle)
    }

    private fun showPopUpMenu(view: View) {
        popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.vehicles_more_pop_up_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            val vehicle = view.tag as Vehicle
            when (it.itemId) {
                R.id.edit -> {
                    listener.editVehicle(vehicle)
                }

                R.id.delete -> {
                    listener.deleteVehicle(vehicle.id)
                }

            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }


    interface VehicleActionsListener {
        fun onItemClick(vehicle: Vehicle)
        fun editVehicle(vehicle: Vehicle)
        fun deleteVehicle(vehicleId: Long)
    }


}

class VehicleViewHolder(
    val binding: VehicleItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(vehicle: Vehicle) {
        binding.root.tag = vehicle
        binding.ivMore.tag = vehicle
        binding.tvVehicleName.text = vehicle.name
        binding.ivVehicleType.setImageDrawable(
            setVehicleIconByType(
                vehicle.type,
                binding.root.context
            )
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setVehicleIconByType(type: VehicleType, context: Context): Drawable {
        return when (type) {
            VehicleType.Car -> context.getDrawable(R.drawable.ic_car_black)!!
            VehicleType.Bike -> context.getDrawable(R.drawable.ic_bike_black)!!
        }
    }
}

class VehicleDiffCallback : DiffUtil.ItemCallback<Vehicle>() {
    override fun areItemsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
        return oldItem == newItem
    }


}



