package com.example.redrive.presentation.refuelFeature.refuel

import androidx.fragment.app.viewModels
import com.example.redrive.R
import com.example.redrive.presentation.refuelFeature.BaseRefuelFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RefuelFragment : BaseRefuelFragment<RefuelViewModel>(R.layout.fragment_refuel) {
    override val baseVM: RefuelViewModel by viewModels()

}