package id.smartech.get_talent.activity.portofolio.update_portofolio

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
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.main.EngineerMainActivity
import id.smartech.get_talent.activity.portofolio.response_portofolio.GetPortofolioByPrIdResponse
import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.databinding.ActivityUpdatePortofolioBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.PortofolioApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UpdatePortofolioActivity : AppCompatActivity(), UpdatePortofolioContract.View {
    private lateinit var binding: ActivityUpdatePortofolioBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: PortofolioApiService
    private lateinit var prefHelper: PrefHelper
    private var presenter: UpdatePortofolioContract.Presenter? = null

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_portofolio)
        coroutineScope = CoroutineScope(Job()+Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(PortofolioApiService::class.java)
        prefHelper = PrefHelper(this)
        presenter = UpdatePortofolioPresenter(coroutineScope, service)

        val prIdClick = prefHelper.getString(Constant.PR_ID_CLICK)
        presenter?.getPortofolioByPrId(prIdClick)

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

        binding.btnUpdatePortofolio.setOnClickListener {
            val prId = prefHelper.getString(Constant.PR_ID_CLICK).toString()
            val enId = prefHelper.getString(Constant.EN_ID).toString()
            val appName = binding.etPrAppName.text.toString()
            val appDesc = binding.etPrDeskripsi.text.toString()
            val appPub = binding.etPrLinkPub.text.toString()
            val repo = binding.etPrLinkRepo.text.toString()
            val tpKerja = binding.etPrTpKerja.text.toString()
            val type = binding.etPrAppType.text.toString()

            if (prId.isEmpty() || enId.isEmpty() || appName.isEmpty() || appDesc.isEmpty() || appPub.isEmpty() ||
                repo.isEmpty() || tpKerja.isEmpty() || type.isEmpty()) {
                Toast.makeText(this, "Harap isi semua data", Toast.LENGTH_SHORT).show()
            } else {
                presenter?.updatePortofolioWithoutImage(prId, enId, appName, appDesc, appPub, repo, tpKerja, type)
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, EngineerMainActivity::class.java))
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

            binding.btnUpdatePortofolio.setOnClickListener {
                val prId = prefHelper.getString(Constant.PR_ID_CLICK).toString()
                val enId = prefHelper.getString(Constant.EN_ID).toString()
                val appName = binding.etPrAppName.text.toString()
                val appDesc = binding.etPrDeskripsi.text.toString()
                val appPub = binding.etPrLinkPub.text.toString()
                val repo = binding.etPrLinkRepo.text.toString()
                val tpKerja = binding.etPrTpKerja.text.toString()
                val type = binding.etPrAppType.text.toString()

                if (img != null) {
                        presenter?.updatePortofolio(prId, enId, appName, appDesc, appPub, repo, tpKerja, type, img)
                }
            }

        }
    }

    private fun showMessage(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }


    override fun onResultSuccessGetPortofolio(data: GetPortofolioByPrIdResponse.Data) {
        binding.model = data
    }

    override fun onResultSuccessUpdate(message: String) {
        showMessage(message)
        onBackPressed()
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