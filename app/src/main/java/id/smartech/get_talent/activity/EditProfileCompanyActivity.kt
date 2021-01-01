package id.smartech.get_talent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.main.CompanyMainActivity

class EditProfileCompanyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_company)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, CompanyMainActivity::class.java))
    }
}