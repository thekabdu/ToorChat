package kz.diaspora.app.ui.messages

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.diaspora.app.core.BaseViewModel
import kz.diaspora.app.data.cloud.ResultWrapper
import kz.diaspora.app.data.cloud.repository.BaseCloudRepository
import kz.diaspora.app.data.db.PrefsImpl
import kz.diaspora.app.di.NetworkModule
import kz.diaspora.app.domain.model.MessageModel
import kz.diaspora.app.domain.model.MessagesModel
import kz.diaspora.app.domain.model.PushNotificationModel
import okhttp3.internal.notify
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val prefsImpl: PrefsImpl,
    private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val messagesData: MutableLiveData<MessagesModel> by lazy { MutableLiveData<MessagesModel>() }
    val messageData: MutableLiveData<List<MessageModel>> by lazy { MutableLiveData<List<MessageModel>>() }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }

//    init {
//        getMessages()
//    }
   // s

    fun getMessages(chat_id: String) {
        launchIO {
            isRefreshing.postValue(true)
            val data = baseCloudRepository.getMessages(chat_id.toInt())
            when (data) {
                is ResultWrapper.Error -> {
                    error.postValue(data)
                }
                is ResultWrapper.Success -> {
                    for (message in data.value.messages) {
                        if (message.user_id == prefsImpl.getUser()?.id) {
                            message.is_my_message = true
                        }
                    }
                    messageData.postValue(data.value.messages)
                }
            }
            isRefreshing.postValue(false)
        }
    }

    fun sendMessage(chat_id: String, text:String) {
        launchIO {
            isRefreshing.postValue(true)
            val data = baseCloudRepository.sendMessage(chat_id.toInt(), text, 0)
            when (data) {
                is ResultWrapper.Error -> {
                    error.postValue(data)

                }
                is ResultWrapper.Success -> {

                }
            }
            isRefreshing.postValue(false)
        }
    }

    fun sendNotification(notification: PushNotificationModel) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = NetworkModule.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}



