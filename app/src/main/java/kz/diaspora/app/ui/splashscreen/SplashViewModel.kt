package kz.diaspora.app.ui.splashscreen

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.diaspora.app.core.BaseViewModel
import kz.diaspora.app.data.cloud.ResultWrapper
import kz.diaspora.app.data.cloud.repository.BaseCloudRepository
import kz.diaspora.app.data.db.PrefsImpl
import kz.diaspora.app.domain.model.UserWithToken
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
        val prefsImpl: PrefsImpl,
        private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val authData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }

    init {
        checkAuth()
    }


    private fun checkAuth() {
        launchIO {
            if (prefsImpl.getToken().isNullOrEmpty()) {
                authData.postValue(false)
            } else {
                authData.postValue(true)
            }
        }
    }


}