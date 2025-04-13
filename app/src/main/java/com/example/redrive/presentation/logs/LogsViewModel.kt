package com.example.redrive.presentation.logs

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.domain.useCase.ObserveRefuelLogsUseCase
import com.example.redrive.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor(
    private val observeRefuelLogsUseCase: ObserveRefuelLogsUseCase
) : BaseViewModel() {

    init {
        viewModelScope.launch {
            observeRefuelLogsUseCase().collectLatest {
                Log.d("LOG", it.toString())
            }
        }

    }
}