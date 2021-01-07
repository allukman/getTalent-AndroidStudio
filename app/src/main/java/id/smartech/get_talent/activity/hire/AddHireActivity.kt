package id.smartech.get_talent.activity.hire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.main.CompanyMainActivity
import id.smartech.get_talent.activity.main.EngineerMainActivity
import id.smartech.get_talent.activity.project.ProjectResponse
import id.smartech.get_talent.data.ProjectCompanyModel
import id.smartech.get_talent.databinding.ActivityAddHireBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.HireService
import id.smartech.get_talent.service.ProjectApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

class AddHireActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddHireBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ProjectApiService
    private lateinit var prefHelper: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_hire)

        prefHelper = PrefHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(ProjectApiService::class.java)
        listProjectSpinner()


        setSupportActionBar(binding.topToolbar)

        binding.btnAddHire.setOnClickListener {
            val projectId = prefHelper.getString(Constant.PJ_ID_CLICK)
            val engineerId = prefHelper.getString(Constant.EN_ID_CLICK)
            val hirePrice = binding.etHirePrice.text.toString()
            val hireMessage = binding.etHireMessage.text.toString()

            if (binding.etHirePrice.text.isEmpty() || binding.etHireMessage.text.isEmpty()) {
                Toast.makeText(this, "All field must be filled", Toast.LENGTH_SHORT).show()
            } else {
                createHire(engineerId!!, projectId, hirePrice, hireMessage)
            }
        }
    }

    private fun listProjectSpinner() {
        binding.spinnerListProject

        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getProjectByComId(prefHelper.getInteger(Constant.COM_ID ))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is ProjectResponse) {
                val list = result.data?.map {
                    ProjectCompanyModel(it.projectId, it.companyId, it.projectName, it.projectDesc, it.projectDeadline, it.projectImage, it.projectCreateAt, it.projectUpdateAt)
                }

                val projectName = arrayOfNulls<String>(list.size)
                val projectId = arrayOfNulls<String>(list.size)

                for (i in 0 until list.size) {
                    projectName[i] = list.get(i).projectName
                    projectId[i] = list.get(i).projectId
                }

                binding.spinnerListProject.adapter = ArrayAdapter<String>(this@AddHireActivity, R.layout.support_simple_spinner_dropdown_item, projectName)

                binding.spinnerListProject.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        prefHelper.put(Constant.PJ_ID_CLICK, projectId[position]!!)
                    }
                }
            }
        }
    }

    private fun createHire(enId: String, pjId: String?, hirePrice: String?, hireMessage: String?) {
            val service = ApiClient.getApiClient(context = this)?.create(HireService::class.java)
            coroutineScope.launch {
                val result = withContext(Dispatchers.IO) {
                    try {
                        service?.createHire(enId, pjId, hirePrice, hireMessage)
                    } catch (e:Throwable) {
                        e.printStackTrace()
                    }
                }

                if (result is CreateHireResponse) {
                    if (result.success) {
                        Toast.makeText(this@AddHireActivity, "Success create hire", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@AddHireActivity, CompanyMainActivity::class.java))
                    } else {
                        Toast.makeText(this@AddHireActivity, "Failed create hire", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }


}