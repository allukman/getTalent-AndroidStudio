package id.smartech.get_talent.activity.experience.create_experience

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
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.main.EngineerMainActivity
import id.smartech.get_talent.databinding.ActivityCreateExperienceBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.ExperienceApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CreateExperienceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateExperienceBinding
    private lateinit var prefHelper: PrefHelper
    private lateinit var viewModel: AddExperienceViewModel

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_experience)
        viewModel = ViewModelProvider(this).get(AddExperienceViewModel::class.java)
        prefHelper = PrefHelper(this)

        val service = ApiClient.getApiClient(this)?.create(ExperienceApiService::class.java)

        if (service != null) {
            viewModel.setService(service)
        }

        binding.imageView.setOnClickListener {
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            PERMISSION_CODE-> {
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

            binding.btnAddExperience.setOnClickListener {
                val enId = prefHelper.getString(Constant.EN_ID).toString()
                val comName = binding.etXpCompanyName.text.toString()
                val position = binding.etXpPosition.text.toString()
                val start = binding.etXpStart.text.toString()
                val end = binding.etXpEnd.text.toString()
                val desc = binding.etXpDeskripsi.text.toString()


                if (img != null) {
                    viewModel.createExperience(enId, comName, position, start, end, desc, img)
                    startActivity(Intent(this, EngineerMainActivity::class.java))
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