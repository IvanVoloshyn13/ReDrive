package voloshyn.android.redrive.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.navigateToSignInFragment() {
    showToast("TODO")
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}
