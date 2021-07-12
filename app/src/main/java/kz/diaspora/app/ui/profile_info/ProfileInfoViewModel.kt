package kz.diaspora.app.ui.profile_info

import android.util.Log
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.diaspora.app.core.BaseViewModel
import kz.diaspora.app.data.cloud.ResultWrapper
import kz.diaspora.app.data.cloud.repository.BaseCloudRepository
import kz.diaspora.app.data.db.PrefsImpl
import kz.diaspora.app.domain.model.*
import javax.inject.Inject

@HiltViewModel
class ProfileInfoViewModel @Inject constructor(
    private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }
    val statusData: MutableLiveData<List<MaritalStatusModel>> by lazy { MutableLiveData<List<MaritalStatusModel>>() }
    val userData: MutableLiveData<User> by lazy { MutableLiveData<User>() }

    init {
        getStatuses()
    }

    fun getStatuses() {
        launchIO {
            isRefreshing.postValue(true)

            val data = baseCloudRepository.getStatuses()
            when (data) {
                is ResultWrapper.Error -> error.postValue(data)
                is ResultWrapper.Success -> {
                    statusData.postValue(data.value)
                }
            }

            isRefreshing.postValue(false)
        }
    }

    fun getUserData(user_id: String) {
        launchIO {
            isRefreshing.postValue(true)
            val data = baseCloudRepository.getUserById(user_id.toInt())

            when (data) {
                is ResultWrapper.Error -> error.postValue(data)
                is ResultWrapper.Success -> {
                    Log.d(TAG, data.value.toString())
                    userData.postValue(data.value)
                }
            }


            isRefreshing.postValue(false)
        }
    }

}