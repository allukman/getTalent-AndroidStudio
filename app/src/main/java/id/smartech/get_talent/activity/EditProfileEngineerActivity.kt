package id.smartech.get_talent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.smartech.get_talent.R

class EditProfileEngineerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_engineer)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, EngineerMainActivity::class.java))
    }
}