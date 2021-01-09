package id.smartech.get_talent.activity.register

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.smartech.get_talent.service.AccountService
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RegisterViewModel: ViewModel(), CoroutineScope {

    val isRegisterLiveData = MutableLiveData<Boolean>()

    private lateinit var service: AccountService
    private lateinit var prefHelper: PrefHelper

    fun setSharedPreferences(prefHelper: PrefHelper) {
        this.prefHelper = prefHelper
    }

    fun setRegisterService(service: AccountService) {
        this.service = service
    }

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun registerEngineer(name: String, email: String, phone: String, password: String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.registerEngineer(name, email, phone, password, 1)
                } catch (e:Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        isRegisterLiveData.value = false
                    }
                }
            }

            if(result is RegisterResponse) {
                if(result.success) {
                    isRegisterLiveData.value = true
                }
            }
        }
    }

    fun registerCompany(name: String, email: String, phone: String, password: String, companyName: String, companyPosition: String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.registerCompany(name, email, phone, password, 2, companyName , companyPosition)
                } catch (e:Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        isRegisterLiveData.value = false
                    }
                }
            }

            if(result is RegisterResponse) {
                if(result.success) {
                    isRegisterLiveData.value = true
                }
            }
        }
    }

}