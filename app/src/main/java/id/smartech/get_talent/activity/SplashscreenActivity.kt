package id.smartech.get_talent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import id.smartech.get_talent.R
import id.smartech.get_talent.fragment.EngineerProfileFragment
import id.smartech.get_talent.helper.WebviewActivity

class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        Handler().postDelayed({
            startActivity(Intent(this, EditProfileCompanyActivity::class.java))
            finish()
        }, 3000)
    }
}