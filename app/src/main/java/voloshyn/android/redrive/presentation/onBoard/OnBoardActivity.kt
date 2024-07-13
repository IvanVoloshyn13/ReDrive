package voloshyn.android.redrive.presentation.onBoard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import voloshyn.android.app.R
import voloshyn.android.app.databinding.ActivityOnBoardBinding
@AndroidEntryPoint
class OnBoardActivity : AppCompatActivity() {


    private lateinit var binding: ActivityOnBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, OnBoardFragmentContainer()).commit()
    }
}