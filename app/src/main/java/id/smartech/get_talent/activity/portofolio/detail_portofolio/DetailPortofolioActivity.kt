package id.smartech.get_talent.activity.portofolio.detail_portofolio

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
import id.smartech.get_talent.activity.main.EngineerMainActivity
import id.smartech.get_talent.activity.portofolio.response_portofolio.GetPortofolioByPrIdResponse
import id.smartech.get_talent.activity.portofolio.update_portofolio.UpdatePortofolioActivity
import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.databinding.ActivityDetailPortofolioBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.PortofolioApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

class DetailPortofolioActivity : AppCompatActivity(), DetailPortofolioContract.View {
    private lateinit var binding: ActivityDetailPortofolioBinding
    private lateinit var prefHelper: PrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: PortofolioApiService
    private var presenter: DetailPortofolioContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_portofolio)
        prefHelper = PrefHelper(this)
        coroutineScope = CoroutineScope(Job()+ Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(PortofolioApiService::class.java)
        presenter = DetailPortofolioPresenter(coroutineScope, service)


        binding.btnDetailRpDelete.setOnClickListener {
            showDialog()
        }

        binding.btnDetailRpUpdate.setOnClickListener {
            startActivity(Intent(this, UpdatePortofolioActivity::class.java))
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

    override fun onResultSuccessGetPortofolio(data: GetPortofolioByPrIdResponse.Data) {
        setPortofolio(data.appName, data.appDesc, data.linkPub, data.repository, data.appType, data.portofolioPhoto)
    }

    override fun onResultSuccessDeletePortofolio() {
        Toast.makeText(this, "Success delete Portofolio", Toast.LENGTH_SHORT).show()
        moveIntent()
    }

    override fun onResultFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure!")
        builder.setMessage("Do you want to delete this portofolio?")
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            val prId = prefHelper.getString(Constant.PR_ID_CLICK)
            presenter?.deletePortofolioByPrId(prId)
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }


    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.cardviewContainer.visibility = View.GONE
        binding.btnDetailRpUpdate.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.cardviewContainer.visibility = View.VISIBLE
        binding.btnDetailRpUpdate.visibility = View.VISIBLE
    }

    override fun onStart() {
        presenter?.bindToView(this)
        val prId = prefHelper.getString(Constant.PR_ID_CLICK)
        presenter?.getPortofolioByPrId(prId)
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