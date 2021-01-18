package id.smartech.get_talent.activity.experience.update_experience

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
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.loader.content.CursorLoader
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.experience.response_experience.GetExperienceByXpIdResponse
import id.smartech.get_talent.activity.main.EngineerMainActivity
import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.databinding.ActivityUpdateExperienceBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.ExperienceApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UpdateExperienceActivity : AppCompatActivity(), UpdateExperienceContract.View {
    private lateinit var binding: ActivityUpdateExperienceBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var prefHelper: PrefHelper
    private lateinit var service: ExperienceApiService
    private var presenter: UpdateExperienceContract.Presenter? = null

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_experience)
        coroutineScope = CoroutineScope(Job()+Dispatchers.Main)
        prefHelper = PrefHelper(this)
        service = ApiClient.getApiClient(this)!!.create(ExperienceApiService::class.java)
        presenter = UpdateExperiencePresenter(coroutineScope, service)

        val xpId = prefHelper.getString(Constant.XP_ID_CLICK)
        presenter?.getExperienceByXpId(xpId)

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

        binding.btnUpdateExperience.setOnClickListener {
            val xpId = prefHelper.getString(Constant.XP_ID_CLICK).toString()
            val enId = prefHelper.getString(Constant.EN_ID).toString()
            val position = binding.etXpPosition.text.toString()
            val comName = binding.etXpCompanyName.text.toString()
            val start = binding.etXpStart.text.toString()
            val end = binding.etXpEnd.text.toString()
            val desc = binding.etXpDeskripsi.text.toString()

            presenter?.updateExperienceWithoutImage(xpId, enId, position, comName, start, end, desc)
        }
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

            binding.btnUpdateExperience.setOnClickListener {
                val xpId = prefHelper.getString(Constant.XP_ID_CLICK).toString()
                val enId = prefHelper.getString(Constant.EN_ID).toString()
                val position = binding.etXpPosition.text.toString()
                val comName = binding.etXpCompanyName.text.toString()
                val start = binding.etXpStart.text.toString()
                val end = binding.etXpEnd.text.toString()
                val desc = binding.etXpDeskripsi.text.toString()

                if (img != null) {
                    presenter?.updateExperience(xpId,enId,position, comName, start, end, desc, img)
                }
            }


        }
    }

//    private fun getExperienceByXpId(xpId: String?){
//        coroutineScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                try {
//                    service?.getExperienceByXpId(xpId)
//                } catch (e:Throwable) {
//                    e.printStackTrace()
//                }
//            }
//
//            if (result is GetExperienceByXpIdResponse) {
//                if (result.success) {
//                    binding.model = result.data
//                    setProfile(
//                        result.data.exStart,
//                        result.data.exEnd
//                            )
//                }
//            }
//        }
//    }

    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    private fun setProfile(xpStart: String, xpEnd: String){
        val start = xpStart!!.split("T")[0]
        val end = xpEnd!!.split("T")[0]

        binding.etXpStart.text = start.toEditable()
        binding.etXpEnd.text = end.toEditable()
    }

//    private fun updateExperience(xpId: String, engineerId: String, jobPosition: String, companyName: String, exStart: String, exEnd: String, exDesc: String, photo: MultipartBody.Part){
//        val enId = engineerId.toRequestBody("text/plain".toMediaTypeOrNull())
//        val position = jobPosition.toRequestBody("text/plain".toMediaTypeOrNull())
//        val comName = companyName.toRequestBody("text/plain".toMediaTypeOrNull())
//        val start = exStart.toRequestBody("text/plain".toMediaTypeOrNull())
//        val end = exEnd.toRequestBody("text/plain".toMediaTypeOrNull())
//        val desc = exDesc.toRequestBody("text/plain".toMediaTypeOrNull())
//        coroutineScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                try {
//                    service?.updateExperience(xpId, enId, position, comName, start, end, desc, photo)
//                } catch (e:Throwable) {
//                    e.printStackTrace()
//                }
//            }
//
//            if (result is HelperResponse) {
//                if (result.success) {
//                    Toast.makeText(this@UpdateExperienceActivity, "Success update experience", Toast.LENGTH_SHORT).show()
//                    moveIntent()
//                } else {
//                    Toast.makeText(this@UpdateExperienceActivity, "Failed update experience", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    private fun updateExperienceWithoutImage(xpId: String, engineerId: String, jobPosition: String, companyName: String, exStart: String, exEnd: String, exDesc: String){
//        val enId = engineerId.toRequestBody("text/plain".toMediaTypeOrNull())
//        val position = jobPosition.toRequestBody("text/plain".toMediaTypeOrNull())
//        val comName = companyName.toRequestBody("text/plain".toMediaTypeOrNull())
//        val start = exStart.toRequestBody("text/plain".toMediaTypeOrNull())
//        val end = exEnd.toRequestBody("text/plain".toMediaTypeOrNull())
//        val desc = exDesc.toRequestBody("text/plain".toMediaTypeOrNull())
//        coroutineScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                try {
//                    service?.updateExperienceWithoutImage(xpId, enId, position, comName, start, end, desc)
//                } catch (e:Throwable) {
//                    e.printStackTrace()
//                }
//            }
//
//            if (result is HelperResponse) {
//                if (result.success) {
//                    Toast.makeText(this@UpdateExperienceActivity, "Success update experience", Toast.LENGTH_SHORT).show()
//                    moveIntent()
//                } else {
//                    Toast.makeText(this@UpdateExperienceActivity, "Failed update experience", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

    private fun showMessage(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        moveIntent()
    }

    private fun moveIntent(){
        startActivity(Intent(this, EngineerMainActivity::class.java))
    }

    override fun onResultSuccessGetExperience(data: GetExperienceByXpIdResponse.Data) {
        binding.model = data

        setProfile(data.exStart, data.exEnd)
    }

    override fun onResultSuccessUpdate(message: String) {
        showMessage(message)
        moveIntent()
    }

    override fun onResultFail(message: String) {
        showMessage(message)
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.container.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.container.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter?.unBind()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        presenter = null
        super.onDestroy()
    }
}