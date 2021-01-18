package id.smartech.get_talent.activity.profile.edit_profile.company

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
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.main.CompanyMainActivity
import id.smartech.get_talent.activity.profile.detail_profile.company.DetailCompanyResponse
import id.smartech.get_talent.activity.profile.edit_profile.engineer.EditEngineerPresenter
import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.databinding.ActivityEditProfileCompanyBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.AccountService
import id.smartech.get_talent.service.CompanyApiService
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

class EditProfileCompanyActivity : AppCompatActivity(), EditCompanyContract.View {
    private lateinit var binding: ActivityEditProfileCompanyBinding
    private lateinit var prefHelper: PrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var serviceCompany: CompanyApiService
    private lateinit var serviceAccount: AccountService
    private var presenter: EditCompanyContract.Presenter? = null


    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile_company)
        prefHelper = PrefHelper(this)
        coroutineScope = CoroutineScope(Job() +Dispatchers.Main)

        serviceCompany = ApiClient.getApiClient(this)!!.create(CompanyApiService::class.java)
        serviceAccount = ApiClient.getApiClient(this)!!.create(AccountService::class.java)
        presenter = EditCompanyPresenter(coroutineScope, serviceCompany, serviceAccount)

        val comId = prefHelper.getInteger(Constant.COM_ID)
        presenter?.getCompanyByComId(comId)

        binding.btnEditSave.setOnClickListener {
            val accountId = prefHelper.getString(Constant.ACC_ID)
            val accountLevel = prefHelper.getString(Constant.ACC_LEVEL).toString()
            val accountName = binding.etAccName.text.toString()
            val accountEmail = binding.etAccEmail.text.toString()
            val accountPhone = binding.etAccPhone.text.toString()
            val accountPassword = binding.etAccPassword.text.toString()

            val comId = prefHelper.getInteger(Constant.COM_ID)
            val name = binding.etEditComName.text.toString()
            val position = binding.etEditComPosition.text.toString()
            val bidang = binding.etEditComField.text.toString()
            val location = binding.etEditComCity.text.toString()
            val desc = binding.etEditComDeskripsi.text.toString()
            val ig = binding.etEditComInstagram.text.toString()
            val linkedin = binding.etEditComLinkedin.text.toString()
            val github = binding.etEditComGithub.text.toString()

            if (accountName.isEmpty() || accountEmail.isEmpty() || accountPhone.isEmpty() || accountPassword.isEmpty() ||
                name.isEmpty() || position.isEmpty() || bidang.isEmpty() || location.isEmpty() || desc.isEmpty() ||
                ig.isEmpty() || linkedin.isEmpty() || github.isEmpty()    ) {
                Toast.makeText(this, "All field must be filled", Toast.LENGTH_SHORT).show()
            } else {
                presenter?.updateAccount(accountId, accountName, accountEmail, accountPhone, accountPassword, accountLevel)
                presenter?.updateCompanyWithoutImage(comId, name, position, bidang, location, desc, ig, linkedin, github)
                onBackPressed()
            }
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

//    private fun getCompanyById(){
//        coroutineScope.launch {
//            val response = withContext(Dispatchers.IO) {
//                try {
//                    service?.getCompanyById(prefHelper.getInteger(Constant.COM_ID))
//                } catch (e:Throwable) {
//                    e.printStackTrace()
//                }
//            }
//            if (response is DetailCompanyResponse) {
//                binding.model = response.data
//                setProfile(
//                    response.data.companyName,
//                    response.data.companyBidang,
//                    response.data.companyCity,
//                    response.data.companyPhoto
//                )
//            }
//        }
//    }

//    private fun updateCompany(comId: Int, name: String, position: String, bidang: String, city: String, desc: String, ig: String, linkedin: String, github: String, photo: MultipartBody.Part){
//        val companyName = name.toRequestBody("text/plain".toMediaTypeOrNull())
//        val comPosition = position.toRequestBody("text/plain".toMediaTypeOrNull())
//        val comBidang = bidang.toRequestBody("text/plain".toMediaTypeOrNull())
//        val comLocation = city.toRequestBody("text/plain".toMediaTypeOrNull())
//        val comDesc = desc.toRequestBody("text/plain".toMediaTypeOrNull())
//        val instagram = ig.toRequestBody("text/plain".toMediaTypeOrNull())
//        val comLinkedin = linkedin.toRequestBody("text/plain".toMediaTypeOrNull())
//        val comGithub = github.toRequestBody("text/plain".toMediaTypeOrNull())
//
//        coroutineScope.launch {
//            val response = withContext(Dispatchers.IO) {
//                try {
//                    service?.updateCompany(comId, companyName, comPosition, comBidang, comLocation, comDesc, instagram, comLinkedin, comGithub, photo)
//                } catch (e:Throwable) {
//                    e.printStackTrace()
//                }
//            }
//            if (response is HelperResponse) {
//                Log.d("success", "success update company")
//                startActivity(Intent(this@EditProfileCompanyActivity, CompanyMainActivity::class.java))
//            }
//        }
//    }
//
//    private fun updateCompanyWithoutImage(comId: Int, name: String, position: String, bidang: String, city: String, desc: String, ig: String, linkedin: String, github: String){
//        val companyName = name.toRequestBody("text/plain".toMediaTypeOrNull())
//        val comPosition = position.toRequestBody("text/plain".toMediaTypeOrNull())
//        val comBidang = bidang.toRequestBody("text/plain".toMediaTypeOrNull())
//        val comLocation = city.toRequestBody("text/plain".toMediaTypeOrNull())
//        val comDesc = desc.toRequestBody("text/plain".toMediaTypeOrNull())
//        val instagram = ig.toRequestBody("text/plain".toMediaTypeOrNull())
//        val comLinkedin = linkedin.toRequestBody("text/plain".toMediaTypeOrNull())
//        val comGithub = github.toRequestBody("text/plain".toMediaTypeOrNull())
//
//        coroutineScope.launch {
//            val response = withContext(Dispatchers.IO) {
//                try {
//                    service?.updateCompanyWithoutImage(comId, companyName, comPosition, comBidang, comLocation, comDesc, instagram, comLinkedin, comGithub)
//                } catch (e:Throwable) {
//                    e.printStackTrace()
//                }
//            }
//            if (response is HelperResponse) {
//                Log.d("success", "success update company")
//                startActivity(Intent(this@EditProfileCompanyActivity, CompanyMainActivity::class.java))
//            }
//        }
//    }

//    private fun updateAccount(accountId: String?, accountName: String, accountEmail: String, accountPhone: String, accountPassword: String, accountLevel: String){
//        val service = ApiClient.getApiClient(this)!!.create(AccountService::class.java)
//
//        coroutineScope.launch {
//            Log.d("android2", "Start: ${Thread.currentThread().name}")
//            val response = withContext(Dispatchers.IO){
//                Log.d("android2", "CallApi: ${Thread.currentThread().name}")
//                try {
//                    service?.updateAccount(accountId, accountName, accountEmail, accountPhone, accountPassword, accountLevel)
//                } catch (e:Throwable) {
//                    e.printStackTrace()
//                }
//            }
//
//            if (response is HelperResponse){
//                Log.d("android2", response.toString())
//                Toast.makeText(this@EditProfileCompanyActivity, "Success Update Account",Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(this@EditProfileCompanyActivity, "Failed Update Account",Toast.LENGTH_LONG).show()
//            }
//        }
//    }

    private fun setProfile(name: String, bidang: String, location: String, photo: String) {
        binding.companyName.text = name
        binding.companyField.text = bidang
        binding.comLocation.text = location

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
                val comId = prefHelper.getInteger(Constant.COM_ID)
                val name = binding.etEditComName.text.toString()
                val position = binding.etEditComPosition.text.toString()
                val bidang = binding.etEditComField.text.toString()
                val location = binding.etEditComCity.text.toString()
                val desc = binding.etEditComDeskripsi.text.toString()
                val ig = binding.etEditComInstagram.text.toString()
                val linkedin = binding.etEditComLinkedin.text.toString()
                val github = binding.etEditComGithub.text.toString()

                if (img != null) {
                    presenter?.updateCompany(comId, name, position, bidang, location, desc, ig, linkedin, github, img)
                }
            }

        }
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
    override fun onBackPressed() {
        startActivity(Intent(this, CompanyMainActivity::class.java))
    }

    private fun showMessage(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResultSuccessGetCompany(data: DetailCompanyResponse.Data) {
        binding.model = data
        setProfile(data.accountName, data.companyBidang, data.companyCity, data.companyPhoto)
    }

    override fun onResultSuccessUpdate(message: String) {
        showMessage(message)
        onBackPressed()
    }

    override fun onResultFail(message: String) {
        showMessage(message)
    }

    override fun showLoading() {
        binding.cvContainer1.visibility = View.GONE
        binding.cvContainer3.visibility = View.GONE
        binding.cvContainer4.visibility = View.GONE
        binding.cvContainer5.visibility = View.GONE
        binding.cvContainer6.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.cvContainer1.visibility = View.VISIBLE
        binding.cvContainer3.visibility = View.VISIBLE
        binding.cvContainer4.visibility = View.VISIBLE
        binding.cvContainer5.visibility = View.VISIBLE
        binding.cvContainer6.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
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