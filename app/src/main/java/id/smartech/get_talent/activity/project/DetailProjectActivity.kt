package id.smartech.get_talent.activity.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.hire.HireStatusResponse
import id.smartech.get_talent.activity.main.EngineerMainActivity
import id.smartech.get_talent.activity.main.GetEngineerIdResponse
import id.smartech.get_talent.databinding.ActivityDetailProjectBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.HireService
import id.smartech.get_talent.service.ProjectApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

class DetailProjectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProjectBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var prefHelper: PrefHelper
    private lateinit var service: ProjectApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_project)
        coroutineScope = CoroutineScope(Job()+ Dispatchers.Main)
        prefHelper = PrefHelper(this)
        service = ApiClient.getApiClient(context = this)!!.create(ProjectApiService::class.java)

        getDetailProject()

        val level = prefHelper.getString(Constant.ACC_LEVEL)

        if(level == "2") {
            binding.cv2.visibility = View.GONE
        } else {
            binding.cv2.visibility = View.VISIBLE

            binding.btnApprove.setOnClickListener {
                updateStatusHire("approve")
                moveIntent()
            }

            binding.btnReject.setOnClickListener {
                updateStatusHire("reject")
                moveIntent()
            }
        }

    }

    private fun getDetailProject() {
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getProjectById(prefHelper.getString(Constant.PJ_ID_CLICK))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is ProjectIdResponse) {
                if (response.success) {
                    Toast.makeText(this@DetailProjectActivity, "Success", Toast.LENGTH_SHORT).show()
                    setProfile(response.data.projectName, response.data.projectDesc, response.data.projectDeadline, response.data.projectImage)
                } else {
                    Toast.makeText(this@DetailProjectActivity, "failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setProfile(pjName: String?, pjDeskripsi: String?, pjDeadline: String?, image: String?){
        binding.pjName.text = pjName
        binding.pjDeskripsi.text = pjDeskripsi
        binding.pjDeadline.text = pjDeadline!!.split("T")[0]


        val img = "http://174.129.47.146:4000/image/$image"

        Glide.with(this)
            .load(img)
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .into(binding.pjPhoto)
    }

    private fun updateStatusHire(status: String) {
        val service = ApiClient.getApiClient(context = this)?.create(HireService::class.java)
        coroutineScope.launch {
            val result = withContext(Dispatchers.Main) {
                try {
                    service?.updateStatus(prefHelper.getString(Constant.HIRE_ID), status)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if(result is HireStatusResponse) {
                if (result.success) {
                    Toast.makeText(this@DetailProjectActivity, "success update", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@DetailProjectActivity, "failed update", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun moveIntent(){
        startActivity(Intent(this, EngineerMainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

}