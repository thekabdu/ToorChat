package kz.diaspora.app.ui.edit_post

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
class EditPostViewModel @Inject constructor(
    private val prefsImpl: PrefsImpl,
    private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }
    val userData: MutableLiveData<User> by lazy { MutableLiveData<User>() }
    val cityList: MutableLiveData<List<CityListItem>> by lazy { MutableLiveData<List<CityListItem>>() }
    val categoryList: MutableLiveData<CategoryList> by lazy { MutableLiveData<CategoryList>() }

    init {
        getUserData()
        getCityList()
        getCategoryList()
    }

    fun getCityList() {
        launchIO {
            val id = prefsImpl.getCountryId()
            if (!id.isNullOrEmpty()) {
                isRefreshing.postValue(true)

                val data = baseCloudRepository.getCityList(Integer.parseInt(id))
                when (data) {
                    is ResultWrapper.Error -> error.postValue(data)
                    is ResultWrapper.Success -> {
                        cityList.postValue(data.value)
                    }
                }

                isRefreshing.postValue(false)
            }
        }
    }

    fun getCategoryList() {
        launchIO {

            isRefreshing.postValue(true)

            val data = baseCloudRepository.getCategoryList()
            when (data) {
                is ResultWrapper.Error -> error.postValue(data)
                is ResultWrapper.Success -> {
                    categoryList.postValue(data.value)
                }
            }

            isRefreshing.postValue(false)

        }
    }


    fun getUserData() {
        launchIO {
            isRefreshing.postValue(true)

            userData.postValue(prefsImpl.getUser())

            isRefreshing.postValue(false)
        }
    }


    fun editPost(post_id: String, path: String, title: String, description: String, category_id: String, city_id: String, email: String, phone: String) {
        launchIO {
            isRefreshing.postValue(true)

            val data =
                baseCloudRepository.editPost(post_id, title, description, category_id, city_id, email, phone)
            when (data) {
                is ResultWrapper.Error -> error.postValue(data)
                is ResultWrapper.Success -> {
                    Log.d(TAG, data.value.toString())
                }
            }

            val data2 =
                baseCloudRepository.changeMainImageOfPost(post_id, path)
            when (data2) {
                is ResultWrapper.Error -> error.postValue(data2)
                is ResultWrapper.Success -> {
                    Log.d(TAG, data2.value.toString())
                }
            }

            isRefreshing.postValue(false)
        }
    }

}