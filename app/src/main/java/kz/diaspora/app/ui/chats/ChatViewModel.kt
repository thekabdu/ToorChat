package kz.diaspora.app.ui.chats

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.diaspora.app.core.BaseViewModel
import kz.diaspora.app.data.cloud.ResultWrapper
import kz.diaspora.app.data.cloud.repository.BaseCloudRepository
import kz.diaspora.app.data.db.PrefsImpl
import kz.diaspora.app.domain.model.ChatModel
import kz.diaspora.app.domain.model.StatusModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val prefsImpl: PrefsImpl,
    private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val chatData: MutableLiveData<List<ChatModel>> by lazy { MutableLiveData<List<ChatModel>>() }
    val status: MutableLiveData<StatusModel> by lazy { MutableLiveData<StatusModel>() }
    val messageData: MutableLiveData<StatusModel> by lazy { MutableLiveData<StatusModel>() }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }

    init {
        getChatsList()
    }

    fun getChatsList() {
        launchIO {
            isRefreshing.postValue(true)
            when (val data = baseCloudRepository.getChats()) {
                is ResultWrapper.Error -> {
                    error.postValue(data)
                }
                is ResultWrapper.Success -> {
                    val chats = arrayListOf<ChatModel>()
                    chatData.postValue(data.value)
                }
            }
            isRefreshing.postValue(false)
        }
    }

    fun runChat(chat_id: Int) {
        launchIO {
            isRefreshing.postValue(true)
            when (val data = baseCloudRepository.joinChat(chat_id)) {
                is ResultWrapper.Error -> {
                    error.postValue(data)
                }
                is ResultWrapper.Success -> {
                    status.postValue(data.value)
                }
            }
            isRefreshing.postValue(false)
        }
    }

    fun likePost(chat_id: Int) {
        launchIO {
            isRefreshing.postValue(true)
            when (val adverts = baseCloudRepository.likeChat(chat_id)) {
                is ResultWrapper.Error -> error.postValue(adverts)
                is ResultWrapper.Success -> {
                    messageData.postValue(adverts.value)
                }
            }
            isRefreshing.postValue(false)
        }
    }

    fun clear() {
        status.postValue(null)
    }
}