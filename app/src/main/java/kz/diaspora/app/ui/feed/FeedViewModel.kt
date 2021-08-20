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
class FeedViewModel @Inject constructor(
    private val prefsImpl: PrefsImpl,
    private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val advertsData: MutableLiveData<List<PostModel>> by lazy { MutableLiveData<List<PostModel>>() }
    val categoriesData: MutableLiveData<CategoryList> by lazy { MutableLiveData<CategoryList>() }
    val chatsData: MutableLiveData<List<ChatModel>> by lazy { MutableLiveData<List<ChatModel>>() }
    val roomsData: MutableLiveData<List<String>> by lazy { MutableLiveData<List<String>>() }
    val messageData: MutableLiveData<StatusModel> by lazy { MutableLiveData<StatusModel>() }
    val DeviceTokenData: MutableLiveData<UserWithToken> by lazy { MutableLiveData<UserWithToken>() }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }

    init {
        getRooms()
        getPosts(1)
        getCategories()
    }

//    fun getPosts() {
//        launchIO {
//            isRefreshing.postValue(true)
////            advertsData.postValue(adverts)
//            when (val adverts = baseCloudRepository.getPostList()) {
//                is ResultWrapper.Error -> error.postValue(adverts)
//                is ResultWrapper.Success -> {
//                    advertsData.postValue(adverts.value)
//                }
//            }
//            isRefreshing.postValue(false)
//        }
//    }

    fun getPosts(categoryId: Int?) {
        launchIO {
            isRefreshing.postValue(true)
            when (val adverts = baseCloudRepository.getPostList(categoryId)) {
                is ResultWrapper.Error -> error.postValue(adverts)
                is ResultWrapper.Success -> {
                    advertsData.postValue(adverts.value)
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
                }
            }
            isRefreshing.postValue(false)
        }
    }

    fun getCategories() {
        launchIO {

            isRefreshing.postValue(true)

            val data = baseCloudRepository.getCategoryList()
            when (data) {
                is ResultWrapper.Error -> error.postValue(data)
                is ResultWrapper.Success -> {
                    categoriesData.postValue(data.value)
                }
            }

            isRefreshing.postValue(false)

        }
    }

    fun getRooms() {
        launchIO {
            isRefreshing.postValue(true)
            val data = baseCloudRepository.getChats()
            when (data) {
                is ResultWrapper.Error -> error.postValue(data)
                is ResultWrapper.Success -> {
                    val rooms = mutableListOf<String>()
                    for(room in data.value) rooms.add(room.chat_room.toString())
                    roomsData.postValue(rooms)
                    chatsData.postValue(data.value)
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
    fun sendDeviceToken(device_token: String){
        launchIO {
            isRefreshing.postValue(true)
            val data = baseCloudRepository.createDeviceToken(
                device_token
            )
            when (data) {
                is ResultWrapper.Error -> error.postValue(data)
                is ResultWrapper.Success -> {
                    DeviceTokenData.postValue(data.value)
                    prefsImpl.setToken(data.value.access_token)
                }
            }
            isRefreshing.postValue(false)
        }
    }

}