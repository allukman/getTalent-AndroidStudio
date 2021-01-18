package id.smartech.get_talent.activity.project.update_project

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.loader.content.CursorLoader
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.main.CompanyMainActivity
import id.smartech.get_talent.activity.project.response_project.ProjectIdResponse
import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.databinding.ActivityUpdateProjectBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.ProjectApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UpdateProjectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProjectBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ProjectApiService
    private lateinit var prefHelper: PrefHelper

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_project)
        coroutineScope = CoroutineScope(Job()+Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(ProjectApiService::class.java)
        prefHelper = PrefHelper(this)
        val pjIdClick = prefHelper.getString(Constant.PJ_ID_CLICK)

        getDetailProject(pjIdClick)

        binding.imageView.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions,
                        PERMISSION_CODE
                    );
                }
                else{
                    pickImageFromGallery()
                }
            } else {
                pickImageFromGallery()
            }
        }

        binding.btnUpdateProject.setOnClickListener {
            val pjId = prefHelper.getString(Constant.PJ_ID_CLICK).toString()
            val companyId = prefHelper.getInteger(Constant.COM_ID).toString()
            val pjName = binding.etProjectName.text.toString()
            val pjDesc = binding.etProjectDeskripsi.text.toString()
            val pjDeadline = binding.etProjectDeadline.text.toString()

            updateProjectWithoutImage(pjId, companyId, pjName, pjDesc, pjDeadline)
        }
    }

    private fun updateProject(projectId: String, companyId: String, projectName: String, projectDesc: String, projectDeadline: String, photo: MultipartBody.Part){
        val comId = companyId.toRequestBody("text/plain".toMediaTypeOrNull())
        val pjName = projectName.toRequestBody("text/plain".toMediaTypeOrNull())
        val pjDesc = projectDesc.toRequestBody("text/plain".toMediaTypeOrNull())
        val pjDeadline = projectDeadline.toRequestBody("text/plain".toMediaTypeOrNull())
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.UpdateProject(projectId, comId, pjName, pjDesc, pjDeadline, photo)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is HelperResponse) {
                if (response.success) {
                    Toast.makeText(this@UpdateProjectActivity, "Success update project", Toast.LENGTH_SHORT).show()
                    moveIntent()
                } else {
                    Toast.makeText(this@UpdateProjectActivity, "Failed update project", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateProjectWithoutImage(projectId: String, companyId: String, projectName: String, projectDesc: String, projectDeadline: String){
        val comId = companyId.toRequestBody("text/plain".toMediaTypeOrNull())
        val pjName = projectName.toRequestBody("text/plain".toMediaTypeOrNull())
        val pjDesc = projectDesc.toRequestBody("text/plain".toMediaTypeOrNull())
        val pjDeadline = projectDeadline.toRequestBody("text/plain".toMediaTypeOrNull())
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.UpdateProjectWithoutImage(projectId, comId, pjName, pjDesc, pjDeadline)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is HelperResponse) {
                if (response.success) {
                    Toast.makeText(this@UpdateProjectActivity, "Success update project", Toast.LENGTH_SHORT).show()
                    moveIntent()
                } else {
                    Toast.makeText(this@UpdateProjectActivity, "Failed update project", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getDetailProject(pjIdClick: String?) {
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getProjectById(pjIdClick)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is ProjectIdResponse) {
                if (response.success) {
                    binding.model = response.data
                    setText(response.data.projectDeadline)
                }
            }
        }
    }

    private fun moveIntent(){
        startActivity(Intent(this, CompanyMainActivity::class.java))
    }

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    private fun setText(pjDeadline: String?) {
        val deadline = pjDeadline!!.split("T")[0]
        binding.etProjectDeadline.text = deadline.toEditable()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }
                else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,
            IMAGE_PICK_CODE
        )
    }

    private fun getPath(context: Context, contentUri: Uri) : String? {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)

        val cursorLoader = CursorLoader(context, contentUri, proj, null, null, null)
        val cursor = cursorLoader.loadInBackground()

        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
            cursor.close()
        }
        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            binding.imageView.setImageURI(data?.data)

            val filePath = data?.data?.let { getPath(this, it) }
            val file = File(filePath)
            Log.d("FileName", file.name)

            var img: MultipartBody.Part? = null
            val mediaTypeImg = "image/jpeg".toMediaType()
            val inputStream = data?.data?.let { contentResolver.openInputStream(it) }
            val reqFile: RequestBody? = inputStream?.readBytes()?.toRequestBody(mediaTypeImg)

            img = reqFile?.let { it1 ->
                MultipartBody.Part.createFormData("photo", file.name, it1)
            }

            binding.btnUpdateProject.setOnClickListener {
                val pjId = prefHelper.getString(Constant.PJ_ID_CLICK).toString()
                val companyId = prefHelper.getInteger(Constant.COM_ID).toString()
                val pjName = binding.etProjectName.text.toString()
                val pjDesc = binding.etProjectDeskripsi.text.toString()
                val pjDeadline = binding.etProjectDeadline.text.toString()

                if (img != null)  {
                    updateProject(pjId, companyId, pjName, pjDesc, pjDeadline, img)
                }
            }



        }
    }
}