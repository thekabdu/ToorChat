package kz.diaspora.app.ui.feed

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
class DetailViewModel @Inject constructor(
    private val prefsImpl: PrefsImpl,
    private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val messageData: MutableLiveData<StatusModel> by lazy { MutableLiveData<StatusModel>() }
    val postData: MutableLiveData<DetailedPostModel> by lazy { MutableLiveData<DetailedPostModel>() }

    init {

    }
    fun getDetailedPost(post: PostModel) {
        launchIO {
            isRefreshing.postValue(true)
            when (val adverts = baseCloudRepository.getDetailedPost(post.id)) {
                is ResultWrapper.Error -> error.postValue(adverts)
                is ResultWrapper.Success -> {
                    postData.postValue(adverts.value)
                }
            }
            isRefreshing.postValue(false)
        }
    }

    fun sendCommentToPost(post: PostModel, message: String) {
        launchIO {

            isRefreshing.postValue(true)

            val data = baseCloudRepository.sendCommentToPost(post.id, message)
            when (data) {
                is ResultWrapper.Error -> error.postValue(data)
                is ResultWrapper.Success -> {
                    Log.d(TAG, "Comment send!")
                    getDetailedPost(post)
                }
            }

            isRefreshing.postValue(false)

        }
    }

    fun likePost(post_id: Int) {
        launchIO {
            isRefreshing.postValue(true)
            when (val adverts = baseCloudRepository.likePost(post_id)) {
                is ResultWrapper.Error -> error.postValue(adverts)
                is ResultWrapper.Success -> {
                    messageData.postValue(adverts.value)
                }
            }
            isRefreshing.postValue(false)
        }
    }

    fun dislikePost(post_id: Int) {
        launchIO {
            isRefreshing.postValue(true)
            when (val adverts = baseCloudRepository.unlikePost(post_id)) {
                is ResultWrapper.Error -> error.postValue(adverts)
                is ResultWrapper.Success -> {
                    messageData.postValue(adverts.value)
                }
            }
            isRefreshing.postValue(false)
        }
    }
}