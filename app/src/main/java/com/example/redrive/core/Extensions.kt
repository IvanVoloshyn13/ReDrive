package com.example.redrive.core

import android.content.Context
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.domain.AppException
import com.example.redrive.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Fragment.findTopNavController(): NavController {
    val topLevelHost =
        requireActivity().supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}

fun Fragment.navigate(direction: NavDirections) {
    findNavController().navigate(direction)
}

 fun TextInputEditText.setTextFromState(value: String) {
    if (this.text.toString() == value) return
    this.setTextKeepState(value)
}

 fun EditText.addDebouncedListener(
    coroutineScope: CoroutineScope,
    delayMillis: Long = 500,
    onTextChanged: (String) -> Unit
) {
    var job: Job? = null
    this.doAfterTextChanged { text ->
        job?.cancel()
        job = coroutineScope.launch {
            delay(delayMillis)
            onTextChanged(text.toString())
        }
    }
}

fun Fragment.hideSoftInputAndClearViewsFocus(root: ViewGroup) {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    root.setOnClickListener { view ->
        imm.hideSoftInputFromWindow(
            view.applicationWindowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    root.children.forEach {
        it.clearFocus()
    }
}

fun Fragment.showErrorAndResetState(
    errorMessage: String,
    setAction: String? = null,
    action: () -> Unit = { },
    resetStateAction: () -> Unit
) {
    Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG)
        .setTextMaxLines(3)
        .setAction(setAction) {
            action()
        }
        .show()
    resetStateAction()
}


fun ViewModel.wrapLocaleDataSourceRequests(
    appStringResProvider: AppStringResProvider,
    action: suspend () -> Unit,
    onError: suspend (message: String) -> Unit
) {
    viewModelScope.launch {
        try {
            action()
        } catch (e: AppException) {
            val message = appStringResProvider.provideStringResByException(e)
            onError(message)
        }
    }
}

