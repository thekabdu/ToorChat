package kz.diaspora.app.ui.messages.users_in_chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.diaspora.app.core.BaseViewModel
import kz.diaspora.app.data.cloud.ResultWrapper
import kz.diaspora.app.data.cloud.repository.BaseCloudRepository
import kz.diaspora.app.data.db.PrefsImpl
import kz.diaspora.app.domain.model.ChatModel
import kz.diaspora.app.domain.model.MessagesModel
import kz.diaspora.app.domain.model.StatusModel
import kz.diaspora.app.domain.model.User
import javax.inject.Inject

@HiltViewModel
class UsersInChatViewModel @Inject constructor(
    private val prefsImpl: PrefsImpl,
    private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val usersListData: MutableLiveData<List<User>> by lazy { MutableLiveData<List<User>>() }
    val status: MutableLiveData<StatusModel> by lazy { MutableLiveData<StatusModel>() }
    val messagesData: MutableLiveData<MessagesModel> by lazy { MutableLiveData<MessagesModel>() }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }

    init {
       //getUsersList()
    }

    fun getUsersList(chat_id: String) {
        launchIO {
            isRefreshing.postValue(true)
            when (val data = baseCloudRepository.getUsersInChat(chat_id.toInt())) {
                is ResultWrapper.Error -> {
                    error.postValue(data)
                }
                is ResultWrapper.Success -> {
                    usersListData.postValue(data.value)
                    Log.d("userList", usersListData.toString())
                }

            }
            isRefreshing.postValue(false)
        }
    }

    fun clear() {
        status.postValue(null)
    }
}