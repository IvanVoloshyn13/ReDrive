package voloshyn.android.redrive.presentation.tabs.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentSettingsBinding
import voloshyn.android.redrive.utils.findTopNavController
import voloshyn.android.redrive.utils.viewBinding

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val viewModel by viewModels<SettingsViewModel>()
    private val binding by viewBinding<FragmentSettingsBinding>()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.state.collectLatest {
                binding.layoutSettings.children.forEachIndexed { index, view ->
                    val itemName = view.findViewById<TextView>(R.id.tv_settings_name)
                    val itemUnit = view.findViewById<TextView>(R.id.tv_settings_unit_value)
                    val icon = view.findViewById<ImageView>(R.id.iv_unit_icon)
                    val item = it.getComponent(index)

                    icon.setImageDrawable(requireContext().getDrawable(item.icon))
                    itemName.text = item.settingsName
                    itemUnit.text = item.settingsUnitValue
                    view.tag = item

                    view.setOnClickListener {
                        val clickedItem = it.tag as SettingsStateItem
                        val units = viewModel.getUnits(clickedItem.field)
                        openItemUnitsDialog(clickedItem, units)
                    }
                }
            }
        }

        binding.bttSave.setOnClickListener {
            viewModel.saveUpdatedSettingsState()
            findTopNavController().popBackStack()
        }

    }

    private fun openItemUnitsDialog(item: SettingsStateItem, units: Array<String>) {
        val builder: MaterialAlertDialogBuilder =
            MaterialAlertDialogBuilder(requireContext(), R.style.App_SettingsDialog)

        builder.setTitle(item.settingsName)
            .setItems(units) { dialog, which ->
                // Do not modify this logic; it depends on the array data structure.
                // Refer to the @values.string comment in the Data module for details.
                viewModel.updateSettingsState(item.field, units[which], which + 1)
            }
        builder.create().show()
    }


}