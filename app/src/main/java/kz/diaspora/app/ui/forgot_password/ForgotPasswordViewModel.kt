package kz.diaspora.app.ui.forgot_password

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.diaspora.app.core.BaseViewModel
import kz.diaspora.app.data.cloud.ResultWrapper
import kz.diaspora.app.data.cloud.repository.BaseCloudRepository
import kz.diaspora.app.data.db.PrefsImpl
import kz.diaspora.app.domain.model.ForgotEmail
import kz.diaspora.app.domain.model.UserWithToken
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
        private val prefsImpl: PrefsImpl,
        private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val emailData: MutableLiveData<ForgotEmail> by lazy { MutableLiveData<ForgotEmail>() }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }

    fun forgotEmail(email: String){
        launchIO {
            isRefreshing.postValue(true)
            val data = baseCloudRepository.forgotEmail(
                    email
            )
            when (data){
            is ResultWrapper.Error -> error.postValue(data)
            is ResultWrapper.Success -> {
                emailData.value.toString()
            }
            }
            isRefreshing.postValue(false)
        }

    }
}