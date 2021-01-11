package id.smartech.get_talent.activity.profile.editProfile

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
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.main.EngineerMainActivity
import id.smartech.get_talent.activity.profile.detailProfile.DetailEngineerResponse
import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.databinding.ActivityEditProfileEngineerBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.EngineerApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditProfileEngineerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileEngineerBinding
    private lateinit var prefHelper: PrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: EngineerApiService

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile_engineer)
        prefHelper = PrefHelper(this)
        coroutineScope = CoroutineScope(Job()+ Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(EngineerApiService::class.java)

        getEngineerByEngId()

        binding.btnEditCancel.setOnClickListener {
            startActivity(Intent(this@EditProfileEngineerActivity, EngineerMainActivity::class.java))
        }

        binding.tvEdit.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions,
                        PERMISSION_CODE
                    );
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, EngineerMainActivity::class.java))
    }

    private fun getEngineerByEngId() {
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getEngineerByEngId(prefHelper.getString(Constant.EN_ID))
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is DetailEngineerResponse) {
                Log.d("result", response.toString())
                binding.model = response.data
                setProfile(
                    response.data.accountName,
                    response.data.engineerJobTitle,
                    response.data.engineerDomisili,
                    response.data.engineerJobType,
                    response.data.engineerPhoto
                )
            }
        }
    }

    private fun updateEngineer(engineerId: String, jobTitle: String, jobType: String, domisili: String, deskripsi: String, instagram: String, github: String, gitlab: String, photo: MultipartBody.Part){
        val title = jobTitle.toRequestBody("text/plain".toMediaTypeOrNull())
        val jobtype = jobType.toRequestBody("text/plain".toMediaTypeOrNull())
        val loc = domisili.toRequestBody("text/plain".toMediaTypeOrNull())
        val desc = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull())
        val ig = instagram.toRequestBody("text/plain".toMediaTypeOrNull())
        val enGithub = github.toRequestBody("text/plain".toMediaTypeOrNull())
        val enGitlab = gitlab.toRequestBody("text/plain".toMediaTypeOrNull())

        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.updateEngineer(engineerId, title, jobtype,loc, desc, ig, enGithub, enGitlab, photo )
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is HelperResponse) {
                Log.d("success", "success update engineer")
                startActivity(Intent(this@EditProfileEngineerActivity, EngineerMainActivity::class.java))
            }
        }
    }

    private fun setProfile(name: String, jobTitle: String, location: String, jobType: String, photo: String) {
        binding.engineerName.text = name
        binding.engineerJobTitle.text = jobTitle
        binding.engLocation.text = location
        binding.jobType.text = jobType

        val img = "http://174.129.47.146:4000/image/$photo"

        Glide.with(this)
            .load(img)
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .into(binding.profilePhoto)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            binding.profilePhoto.setImageURI(data?.data)

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

            binding.btnEditSave.setOnClickListener {
                val enId = prefHelper.getString(Constant.EN_ID).toString()
                val title = binding.etEditEngJobTitle.text.toString()
                val type = binding.etEditEngJobType.text.toString()
                val location = binding.etEditEngDomisili.text.toString()
                val desc = binding.etEditEngDeskripsi.text.toString()
                val instagram = binding.etEditEngInstagram.text.toString()
                val github = binding.etEditEngGithub.text.toString()
                val gitlab = binding.etEditEngGitlab.text.toString()

                if (img != null) {
                    updateEngineer(enId, title, type, location, desc, instagram, github, gitlab, img)
                }
            }

        }
    }

    fun getPath(context: Context, contentUri: Uri) : String? {
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


}