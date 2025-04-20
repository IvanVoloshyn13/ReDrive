package com.example.redrive.presentation.vehicles

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
import com.example.domain.model.Vehicle
import com.example.domain.model.VehicleType
import com.example.redrive.R
import com.example.redrive.databinding.VehicleItemBinding


class VehiclesAdapter(private val listener: VehicleActionsListener) :
    ListAdapter<Vehicle, VehicleViewHolder>(VehicleDiffCallback()), View.OnClickListener {
    private lateinit var popupMenu: PopupMenu

    override fun onClick(v: View) {
        val vehicle = v.tag as Vehicle
        when (v.id) {
            R.id.iv_more -> showPopUpMenu(view = v, vehicle)
            else -> listener.onVehicleItemClick(vehicle)
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

    private fun showPopUpMenu(view: View, vehicle: Vehicle) {
        popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.vehicles_more_pop_up_menu, popupMenu.menu)
        val setAsCurrentItem = popupMenu.menu.findItem(R.id.set_as_current)
        setAsCurrentItem.isVisible = !vehicle.isCurrentVehicle
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit -> {
                    listener.onEditItemClick(vehicle)
                }

                R.id.delete -> {
                    listener.onDeleteItemClick(vehicle.id)
                }

                R.id.set_as_current -> {
                    listener.onVehicleItemClick(vehicle)
                }

            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }


    interface VehicleActionsListener {
        fun onVehicleItemClick(vehicle: Vehicle)
        fun onEditItemClick(vehicle: Vehicle)
        fun onDeleteItemClick(vehicleId: Long)
    }


}

class VehicleViewHolder(
    val binding: VehicleItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(vehicle: Vehicle) {
        binding.root.tag = vehicle
        binding.ivMore.tag = vehicle
        binding.ivIsCurrent.visibility = if (vehicle.isCurrentVehicle) View.VISIBLE else View.GONE
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
            VehicleType.Car -> context.getDrawable(R.drawable.ic_car)!!
            VehicleType.Bike -> context.getDrawable(R.drawable.ic_bike)!!

        }
    }
}

class VehicleDiffCallback : DiffUtil.ItemCallback<Vehicle>() {
    override fun areItemsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.name == newItem.name &&
                oldItem.initialOdometerValue == newItem.initialOdometerValue &&
                oldItem.type == newItem.type &&
                oldItem.isCurrentVehicle == newItem.isCurrentVehicle
    }
}



