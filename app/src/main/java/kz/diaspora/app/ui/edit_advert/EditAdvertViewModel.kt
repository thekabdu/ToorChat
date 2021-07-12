package kz.diaspora.app.ui.edit_advert

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.diaspora.app.core.BaseViewModel
import kz.diaspora.app.data.cloud.ResultWrapper
import kz.diaspora.app.data.cloud.repository.BaseCloudRepository
import kz.diaspora.app.data.db.PrefsImpl
import kz.diaspora.app.domain.model.*
import kz.diaspora.app.utils.adverts
import javax.inject.Inject

@HiltViewModel
class EditAdvertViewModel @Inject constructor(
    private val prefsImpl: PrefsImpl,
    private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }
    val userData: MutableLiveData<User> by lazy { MutableLiveData<User>() }
    val cityList: MutableLiveData<List<CityListItem>> by lazy { MutableLiveData<List<CityListItem>>() }
    val categoryList: MutableLiveData<CategoryList> by lazy { MutableLiveData<CategoryList>() }
    val countryCityNames: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val postData: MutableLiveData<SavePostModel> by lazy { MutableLiveData<SavePostModel>() }

    init {
        getUserData()
        getCityList()
        getLocation()
        getCategoryList()
        getPostData()
    }

    fun getPostData() {
        launchIO {
            isRefreshing.postValue(true)
            if (prefsImpl.getPostData() != null) postData.postValue(prefsImpl.getPostData())
            isRefreshing.postValue(false)
        }
    }

    fun getCityList() {
        launchIO {
            val id = prefsImpl.getCountryFromId()
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

    fun getLocation() {
        launchIO {
            if (prefsImpl.getCountryPost() != "") {
                var s = ""

                s += prefsImpl.getCountryPost()

                if (prefsImpl.getCityPost() != "") {
                    s += ", " + prefsImpl.getCityPost()
                }

                countryCityNames.postValue(s)
            } else {
                countryCityNames.postValue("Местоположение")
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


    fun createPost(path: String, title: String, description: String, category_id: String?, city_id: String, email: String, phone: String) {
        launchIO {
            isRefreshing.postValue(true)
            val city = prefsImpl.getCityPostId().toString()

            if (category_id != null) {
                val data = baseCloudRepository.createPost(
                    path,
                    title,
                    description,
                    category_id,
                    city,
                    email,
                    phone
                )
                when (data) {
                    is ResultWrapper.Error -> error.postValue(data)
                    is ResultWrapper.Success -> {
                        Log.d(TAG, data.value.toString())
                    }
                }
            }
            isRefreshing.postValue(false)
        }
    }

    fun savePostData(path: String?, title: String?, description: String?, category_id: String?, phone: String?) {
        launchIO {
            isRefreshing.postValue(true)
            prefsImpl.setPostData(SavePostModel(path, title, description, category_id, phone))
            isRefreshing.postValue(false)
        }
    }

    fun clear() {
        prefsImpl.setCountryPost("")
        prefsImpl.setCityPost("")
        prefsImpl.setPostData(null)
    }

}