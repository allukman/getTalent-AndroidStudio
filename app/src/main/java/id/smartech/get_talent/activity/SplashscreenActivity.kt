package id.smartech.get_talent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivitySplashscreenBinding
import id.smartech.get_talent.fragment.EngineerProfileFragment
import id.smartech.get_talent.helper.WebviewActivity

class SplashscreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashscreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splashscreen)

        val animation = AnimationUtils.loadAnimation(this, R.anim.fadein)

        binding.ivLogo.startAnimation(animation)



        Handler().postDelayed({
            startActivity(Intent(this, OnBoardActivity::class.java))
            finish()
        }, 5000)
    }
}