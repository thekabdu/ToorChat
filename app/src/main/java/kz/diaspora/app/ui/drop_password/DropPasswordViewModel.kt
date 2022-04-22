package kz.my_portfel.app.ui.pincode

import android.util.Log
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.diaspora.app.core.BaseViewModel
import kz.diaspora.app.data.cloud.ResultWrapper
import kz.diaspora.app.data.cloud.repository.BaseCloudRepository
import kz.diaspora.app.data.db.PrefsImpl
import kz.diaspora.app.domain.model.ForgotCode
import kz.diaspora.app.domain.model.ForgotEmail
import kz.diaspora.app.domain.model.UserWithToken
import javax.inject.Inject

@HiltViewModel
class DropPasswordViewModel @Inject constructor(
    private val prefsImpl: PrefsImpl,
    private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val loginData: MutableLiveData<UserWithToken> by lazy { MutableLiveData<UserWithToken>() }
    val emailData:MutableLiveData<ForgotEmail> by lazy { MutableLiveData<ForgotEmail>() }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }

    init {
    }

    fun forgotCode(email: String, two_factor_code: Int){
        launchIO {
            isRefreshing.postValue(true)
            val data = baseCloudRepository.forgotCode(
                   email,
                    two_factor_code
            )

            Log.d("taag", "${loginData.toString()}")
            when (data) {
                is ResultWrapper.Error -> error.postValue(data)
                is ResultWrapper.Success -> {
                    loginData.postValue(data.value)
                    emailData.value!!.email
                    prefsImpl.setToken(data.value.access_token)
                    prefsImpl.setUser(data.value.user)
                }
            }
            isRefreshing.postValue(false)
        }
    }
}