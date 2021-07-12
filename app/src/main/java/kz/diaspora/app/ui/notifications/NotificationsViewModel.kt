package kz.diaspora.app.ui.notifications

import android.util.Log
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.diaspora.app.core.BaseViewModel
import kz.diaspora.app.data.cloud.ResultWrapper
import kz.diaspora.app.data.cloud.repository.BaseCloudRepository
import kz.diaspora.app.data.db.PrefsImpl
import kz.diaspora.app.domain.model.NotificationModel
import kz.diaspora.app.domain.model.PostModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val prefsImpl: PrefsImpl,
    private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val notificationsData: MutableLiveData<List<NotificationModel>> by lazy { MutableLiveData<List<NotificationModel>>() }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }
    val postData: MutableLiveData<PostModel> by lazy { MutableLiveData<PostModel>() }

    init {
        getNotifications()
    }

    fun getNotifications() {
        launchIO {
            isRefreshing.postValue(true)
            val projects = baseCloudRepository.getNewComments()
            when (projects) {
                is ResultWrapper.Error -> error.postValue(projects)
                is ResultWrapper.Success -> notificationsData.postValue(projects.value)
            }

            isRefreshing.postValue(false)
        }
    }

    fun getPost(post: PostModel) {
        launchIO {
            isRefreshing.postValue(true)
            Log.d(TAG, post.id.toString())
            val adverts = baseCloudRepository.getDetailedPost(post.id)
            when (adverts) {
                is ResultWrapper.Error -> error.postValue(adverts)
                is ResultWrapper.Success -> {
                    postData.postValue(adverts.value.post)
                }
            }
            Log.d(TAG, adverts.toString())
            isRefreshing.postValue(false)
        }
    }

    fun clearPost() {
        launchIO {
            postData.postValue(null)
        }
    }
}