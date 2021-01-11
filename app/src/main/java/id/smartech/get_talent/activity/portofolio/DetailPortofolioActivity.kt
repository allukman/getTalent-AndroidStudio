package id.smartech.get_talent.activity.portofolio

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
import id.smartech.get_talent.databinding.ActivityDetailPortofolioBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.PortofolioApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

class DetailPortofolioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPortofolioBinding
    private lateinit var prefHelper: PrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: PortofolioApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_portofolio)
        prefHelper = PrefHelper(this)
        coroutineScope = CoroutineScope(Job()+ Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(PortofolioApiService::class.java)

        getPortofolioByPrId()

        binding.btnDetailRpDelete.setOnClickListener {
            showDialog()
        }

    }

    private fun getPortofolioByPrId(){
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getPortofolioByPrId(prefHelper.getString(Constant.PR_ID_CLICK))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is GetPortofolioByPrIdResponse) {
                if (response.success){
                    setPortofolio(
                        response.data.appName,
                        response.data.appDesc,
                        response.data.linkPub,
                        response.data.repository,
                        response.data.appType,
                        response.data.portofolioPhoto
                    )
                }
            }
        }
    }

    private fun deletePortofolio(){
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.deletePortofolio(prefHelper.getString(Constant.PR_ID_CLICK))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is HelperResponse) {
                if (response.success) {
                    Toast.makeText(this@DetailPortofolioActivity, "Success delete portofolio", Toast.LENGTH_SHORT).show()
                    moveIntent()
                } else {
                    Toast.makeText(this@DetailPortofolioActivity, "Failed delete portofolio", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setPortofolio(appName: String, appDesc: String, pub: String, repo: String, appType: String, image: String){
        binding.detailPrAppName.text = appName
        binding.detailPrDesc.text = appDesc
        binding.detailPrPub.text = pub
        binding.detailPrRepo.text = repo
        binding.detailPrTypeApp.text = appType

        val img = "http://174.129.47.146:4000/image/$image"

        Glide.with(this)
            .load(img)
            .placeholder(R.drawable.portofolio1)
            .error(R.drawable.portofolio1)
            .into(binding.detailPrPhoto)
    }

    private fun moveIntent(){
        startActivity(Intent(this, EngineerMainActivity::class.java))
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure!")
        builder.setMessage("Do you want to delete this experience?")
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            deletePortofolio()
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }

}