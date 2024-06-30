package voloshyn.android.redrive.onBoard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import voloshyn.android.app.R
import voloshyn.android.app.databinding.ActivityOnBoardBinding


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