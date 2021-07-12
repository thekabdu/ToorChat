package kz.diaspora.app.ui.my_adverts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.diaspora.app.R
import kz.diaspora.app.core.BaseViewModel
import kz.diaspora.app.data.cloud.ResultWrapper
import kz.diaspora.app.data.cloud.repository.BaseCloudRepository
import kz.diaspora.app.data.db.PrefsImpl
import kz.diaspora.app.domain.model.PostModel
import kz.diaspora.app.domain.model.response.ManipulatePostStatus
import kz.diaspora.app.utils.adverts
import javax.inject.Inject

@HiltViewModel
class MyAdvertsViewModel @Inject constructor(
    private val prefsImpl: PrefsImpl,
    private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val advertsData: MutableLiveData<List<PostModel>> by lazy { MutableLiveData<List<PostModel>>() }
    val activateStatusData: MutableLiveData<ManipulatePostStatus> by lazy { MutableLiveData<ManipulatePostStatus>() }
    val deactivateStatusData: MutableLiveData<ManipulatePostStatus> by lazy { MutableLiveData<ManipulatePostStatus>() }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }

    init {
    }

    fun getAdverts(advertType: MyAdvertType) {
        launchIO {
            isRefreshing.postValue(true)
            val adverts: ResultWrapper<List<PostModel>>
            when (advertType) {
                MyAdvertType.Favorite -> {
                    adverts = baseCloudRepository.getPostListFavorite()
                }
                MyAdvertType.Active -> {
                    adverts = baseCloudRepository.getPostListActive()
                }
                MyAdvertType.NotActive -> {
                    adverts = baseCloudRepository.getPostListNotActive()

                }
                MyAdvertType.Pending -> {
                    adverts = baseCloudRepository.getPostListInWait()
                }
            }
            when (adverts) {
                is ResultWrapper.Error -> error.postValue(adverts)
                is ResultWrapper.Success -> {
                    advertsData.postValue(adverts.value)
                    Log.d(TAG, adverts.value.toString())
                }
            }
            isRefreshing.postValue(false)
        }
    }

    fun activatePost(post: PostModel) {
        launchIO {
            isRefreshing.postValue(true)
            when (val adverts = baseCloudRepository.activatePost(post)) {
                is ResultWrapper.Error -> error.postValue(adverts)
                is ResultWrapper.Success -> {
                    activateStatusData.postValue(adverts.value)
                    getAdverts(MyAdvertType.NotActive)
                }
            }
            isRefreshing.postValue(false)
        }
    }

    fun deactivatePost(post: PostModel) {
        launchIO {
            isRefreshing.postValue(true)
            when (val adverts = baseCloudRepository.deactivatePost(post)) {
                is ResultWrapper.Error -> error.postValue(adverts)
                is ResultWrapper.Success -> {
                    deactivateStatusData.postValue(adverts.value)
                    getAdverts(MyAdvertType.Active)
                }
            }
            isRefreshing.postValue(false)
        }
    }
}