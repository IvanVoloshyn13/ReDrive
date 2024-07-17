package voloshyn.android.redrive.presentation.profile

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentSignInBinding
import voloshyn.android.redrive.utils.viewBinding

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    private val binding by viewBinding<FragmentSignInBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spannableString = SpannableString(binding.bttSignIn.text)
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
        binding.bttSignIn.text = spannableString
    }

}
