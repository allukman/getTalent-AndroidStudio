package id.smartech.get_talent.activity.experience

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.main.EngineerMainActivity
import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.databinding.ActivityDetailExperienceBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.ExperienceApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

class DetailExperienceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailExperienceBinding
    private lateinit var prefHelper: PrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ExperienceApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_experience)
        prefHelper = PrefHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(ExperienceApiService::class.java)

        getExperienceByXpId()

        binding.btnDetailXpDelete.setOnClickListener {
            showDialog()
        }
    }

    private fun getExperienceByXpId(){
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getExperienceByXpId(prefHelper.getString(Constant.XP_ID_CLICK))
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is GetExperienceByXpIdResponse) {
                if (response.success) {
                    setExperience(
                        response.data.exCompany,
                        response.data.position,
                        response.data.exStart,
                        response.data.exEnd,
                        response.data.exDesc,
                        response.data.photo)
                }
            }
        }
    }

    private fun deleteExperience(){
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.deleteExperience(prefHelper.getString(Constant.XP_ID_CLICK))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is HelperResponse) {
                if (response.success) {
                    Toast.makeText(this@DetailExperienceActivity, "Success Delete Experience", Toast.LENGTH_SHORT).show()
                    moveIntent()
                } else {
                    Toast.makeText(this@DetailExperienceActivity, "Failed Delete Experience", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setExperience(companyName: String, position: String, start: String, end: String, desc: String, image: String){
        val startDate = start.split("-")[0]
        val endDate = end.split("-")[0]

        binding.detailXpCompanyName.text = companyName
        binding.detailXpPosition.text = position
        binding.detailXpStartDate.text = startDate
        binding.detailXpEndDate.text = "- $endDate"
        binding.detailXpDeskripsi.text = desc

        val img = "http://174.129.47.146:4000/image/$image"

        Glide.with(this)
            .load(img)
            .placeholder(R.drawable.instagram)
            .error(R.drawable.instagram)
            .into(binding.detailXpPhoto)
    }

    private fun moveIntent(){
        startActivity(Intent(this, EngineerMainActivity::class.java))
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure!")
        builder.setMessage("Do you want to delete this experience?")
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            deleteExperience()
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }
}