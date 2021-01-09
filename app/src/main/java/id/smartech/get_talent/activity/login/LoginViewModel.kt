package id.smartech.get_talent.activity.login

import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.smartech.get_talent.service.AccountService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoginViewModel: ViewModel(), CoroutineScope {

    val isLoginLiveData = MutableLiveData<Boolean>()

    private lateinit var service: AccountService
    private lateinit var prefHelper: PrefHelper

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setSharedPreferences(prefHelper: PrefHelper) {
        this.prefHelper = prefHelper
    }

    fun setLoginService(service: AccountService) {
        this.service = service
    }

    private fun saveSession(accountId: String, token: String, level: String, name: String){
        prefHelper.put( Constant.IS_LOGIN, true )
        prefHelper.put(Constant.ACC_ID, accountId)
        prefHelper.put(Constant.TOKEN, token)
        prefHelper.put(Constant.ACC_LEVEL, level)
        prefHelper.put(Constant.ACC_NAMA, name)
    }

    fun loginRequest(email: String, password: String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.loginRequest(email, password)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        isLoginLiveData.value = false
                    }
                }
            }
            if (result is LoginResponse) {
                if(result.success) {
                    saveSession(result.data.accountId, result.data.token, result.data.accountLevel, result.data.accountName)
                    isLoginLiveData.value = true
                }
            }
        }
    }
}