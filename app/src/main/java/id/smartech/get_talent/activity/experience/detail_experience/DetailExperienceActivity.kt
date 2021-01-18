package id.smartech.get_talent.activity.experience.detail_experience

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.experience.update_experience.UpdateExperienceActivity
import id.smartech.get_talent.activity.experience.response_experience.GetExperienceByXpIdResponse
import id.smartech.get_talent.activity.main.EngineerMainActivity
import id.smartech.get_talent.databinding.ActivityDetailExperienceBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.ExperienceApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

class DetailExperienceActivity : AppCompatActivity(), DetailExperienceContract.View {
    private lateinit var binding: ActivityDetailExperienceBinding
    private lateinit var prefHelper: PrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ExperienceApiService
    private var presenter: DetailExperienceContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_experience)
        prefHelper = PrefHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(ExperienceApiService::class.java)
        presenter = DetailExperiencePresenter(coroutineScope, service)

//        val xpId = prefHelper.getString(Constant.XP_ID_CLICK)
//        presenter?.getExperienceByXpId(xpId)

        binding.btnDetailXpDelete.setOnClickListener {
            showDialog()
        }

        binding.btnDetailXpUpdate.setOnClickListener {
            startActivity(Intent(this, UpdateExperienceActivity::class.java))
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


    override fun onResultSuccessGetExperience(data: GetExperienceByXpIdResponse.Data) {
        setExperience(data.exCompany, data.position, data.exStart, data.exEnd, data.exDesc, data.photo)
    }


    override fun onResultSuccessDeleteExperience() {
        Toast.makeText(this, "Success delete Experience", Toast.LENGTH_SHORT).show()
        moveIntent()
    }

    override fun onResultFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure!")
        builder.setMessage("Do you want to delete this experience?")
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            val xpId = prefHelper.getString(Constant.XP_ID_CLICK)
            presenter?.deleteExperienceByXpId(xpId)
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.cardviewContainer.visibility = View.GONE
        binding.btnDetailXpUpdate.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.cardviewContainer.visibility = View.VISIBLE
        binding.btnDetailXpUpdate.visibility = View.VISIBLE
    }

    override fun onStart() {
        presenter?.bindToView(this)
        val xpId = prefHelper.getString(Constant.XP_ID_CLICK)
        presenter?.getExperienceByXpId(xpId)
        super.onStart()
    }

    override fun onStop() {
        presenter?.unBind()
        super.onStop()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}