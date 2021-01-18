package id.smartech.get_talent.activity.project.detailProject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.hire.response_hire.GetHireByIdResponse
import id.smartech.get_talent.activity.main.EngineerMainActivity
import id.smartech.get_talent.activity.project.update_project.UpdateProjectActivity
import id.smartech.get_talent.activity.project.response_project.ProjectIdResponse
import id.smartech.get_talent.databinding.ActivityDetailProjectBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.HireService
import id.smartech.get_talent.service.ProjectApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

class DetailProjectActivity : AppCompatActivity(), DetailProjectContract.View {
    private lateinit var binding: ActivityDetailProjectBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var prefHelper: PrefHelper
    private lateinit var serviceProject: ProjectApiService
    private lateinit var serviceHire: HireService
    private var presenter: DetailProjectContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_project)
        coroutineScope = CoroutineScope(Job()+ Dispatchers.Main)
        prefHelper = PrefHelper(this)
        serviceProject = ApiClient.getApiClient(this)!!.create(ProjectApiService::class.java)
        serviceHire = ApiClient.getApiClient(this)!!.create(HireService::class.java)
        presenter = DetailProjectPresenter(coroutineScope,serviceProject, serviceHire)

        val pjId = prefHelper.getString(Constant.PJ_ID_CLICK)
        val hrId = prefHelper.getString(Constant.HIRE_ID)
        val level = prefHelper.getString(Constant.ACC_LEVEL)

        presenter?.getHireById(hrId)
        presenter?.getProjectByPjId(pjId)

        if(level == "2") {
            binding.cv2.visibility = View.GONE
            binding.cv3.visibility = View.VISIBLE
            binding.btnUpdate.setOnClickListener {
                moveEditProject()
            }
        } else if (binding.hireStatus.text != "wait") {
            binding.cv2.visibility = View.GONE
            binding.cv3.visibility = View.GONE
        } else {
            binding.cv2.visibility = View.VISIBLE
            binding.cv3.visibility = View.GONE
            binding.btnApprove.setOnClickListener {
                presenter?.updateHireByHrId(hrId, "approve")
                moveIntent()
            }

            binding.btnReject.setOnClickListener {
                presenter?.updateHireByHrId(hrId, "reject")
                moveIntent()
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


    private fun moveIntent(){
        startActivity(Intent(this, EngineerMainActivity::class.java))
        finish()
    }

    private fun moveEditProject(){
        startActivity(Intent(this, UpdateProjectActivity::class.java))
        finish()
    }

    override fun onResultSuccessGetProject(data: ProjectIdResponse.Data) {
        setProfile(data.projectName, data.projectDesc, data.projectDeadline, data.projectImage)
    }

    override fun onResultSuccessGetHire(data: GetHireByIdResponse.Data) {
        binding.hirePrice.text = data.hirePrice
        binding.hireStatus.text = data.hireStatus
    }

    override fun onResultSuccessUpdateHire() {
        Toast.makeText(this, "Success Update Hire", Toast.LENGTH_SHORT).show()
        moveIntent()
    }

    override fun onResultFailed() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }


    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.cv1.visibility = View.GONE
        binding.cv2.visibility = View.GONE
        binding.cv3.visibility = View.GONE
    }

    override fun hideLoading() {
        val level = prefHelper.getString(Constant.ACC_LEVEL)
        val hrId = prefHelper.getString(Constant.HIRE_ID)

        binding.progressBar.visibility = View.GONE
        binding.cv1.visibility = View.VISIBLE

        if(level == "2") {
            binding.cv2.visibility = View.GONE
            binding.cv3.visibility = View.VISIBLE
            binding.btnUpdate.setOnClickListener {
                moveEditProject()
            }
        } else if (binding.hireStatus.text != "wait") {
            binding.cv2.visibility = View.GONE
            binding.cv3.visibility = View.GONE
        } else {
            binding.cv2.visibility = View.VISIBLE
            binding.cv3.visibility = View.GONE
            binding.btnApprove.setOnClickListener {
                presenter?.updateHireByHrId(hrId, "approve")
                moveIntent()
            }

            binding.btnReject.setOnClickListener {
                presenter?.updateHireByHrId(hrId, "reject")
                moveIntent()
            }
        }
    }

    override fun onStart() {
        presenter?.bindToView(this)
        super.onStart()
    }

    override fun onStop() {
        coroutineScope.cancel()
        presenter?.unBind()
        super.onStop()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

}