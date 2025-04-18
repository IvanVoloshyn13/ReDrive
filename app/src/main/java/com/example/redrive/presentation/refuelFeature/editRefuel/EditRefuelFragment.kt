package com.example.redrive.presentation.refuelFeature.editRefuel

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.redrive.R
import com.example.redrive.presentation.refuelFeature.BaseRefuelFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditRefuelFragment : BaseRefuelFragment<EditRefuelViewModel>(R.layout.fragment_refuel) {

    private val args: EditRefuelFragmentArgs by navArgs()
    private val refuelId by lazy {
        args.refuelId

    }
    private val viewModel: EditRefuelViewModel by viewModels()
    override val baseVM: EditRefuelViewModel
        get() = viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        baseVM.doOnRefuelId(refuelId)
        super.onViewCreated(view, savedInstanceState)
    }

}