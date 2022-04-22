package kz.diaspora.app.ui.profile

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
class ProfileViewModel @Inject constructor(
        private val prefsImpl: PrefsImpl,
        private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val bookmarkClickData: MutableLiveData<PostModel> by lazy { MutableLiveData<PostModel>() }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }
    val authData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val userData: MutableLiveData<User> by lazy { MutableLiveData<User>() }
    val newUserData: MutableLiveData<User> by lazy { MutableLiveData<User>() }
    val countryList: MutableLiveData<List<CountryListItem>> by lazy { MutableLiveData<List<CountryListItem>>() }
    val cityList: MutableLiveData<List<CityListItem>> by lazy { MutableLiveData<List<CityListItem>>() }
    val countryCityNames: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val countryFromCityNames: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val countryFrom: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val countsData: MutableLiveData<PostsCountModel> by lazy { MutableLiveData<PostsCountModel>() }

    init {
        getUserData()
        getCountryList()
        getLocation()
        getCountryFrom()
        getPostsCount()
    }

    fun getCountryList() {
        launchIO {
            isRefreshing.postValue(true)

            val data = baseCloudRepository.getCountryList()
            when (data) {
                is ResultWrapper.Error -> error.postValue(data)
                is ResultWrapper.Success -> {
                    countryList.postValue(data.value)
                }
            }

            isRefreshing.postValue(false)
        }
    }

    fun getCityList(id: Int) {
        launchIO {
            isRefreshing.postValue(true)

            val data = baseCloudRepository.getCityList(id)
            when (data) {
                is ResultWrapper.Error -> error.postValue(data)
                is ResultWrapper.Success -> {
                    cityList.postValue(data.value)
                }
            }

            isRefreshing.postValue(false)
        }
    }

    fun getPostsCount() {
        launchIO {
            isRefreshing.postValue(true)

            val data = baseCloudRepository.getPostsCount()
            when (data) {
                is ResultWrapper.Error -> error.postValue(data)
                is ResultWrapper.Success -> {
                    countsData.postValue(data.value)
                }
            }

            isRefreshing.postValue(false)
        }
    }

    fun getUserData() {
        launchIO {
            isRefreshing.postValue(true)

            val oldUser = prefsImpl.getUser()

            if (oldUser != null) {
                val data =
                    oldUser.id?.let { baseCloudRepository.getUserById(it) }

                when (data) {
                    is ResultWrapper.Error -> error.postValue(data)
                    is ResultWrapper.Success -> {
                        Log.d(TAG, data.value.toString())
                        userData.postValue(data.value)
                        prefsImpl.setUser(data.value)
                    }
                }
            }

            isRefreshing.postValue(false)
        }
    }

    fun exit() {
        launchIO {
            isRefreshing.postValue(true)
            prefsImpl.logOut()

            authData.postValue(false)
            isRefreshing.postValue(false)
        }
    }

    fun changeUserAvatar(path: String) {
        launchIO {
            isRefreshing.postValue(true)

            val data =
                baseCloudRepository.changeUserAvatar(path)
            when (data) {
                is ResultWrapper.Error -> error.postValue(data)
                is ResultWrapper.Success -> {
                    Log.d(TAG, data.value.toString())
                    newUserData.postValue(data.value)
                    prefsImpl.setUser(data.value)
                }
            }

            isRefreshing.postValue(false)
        }
    }

    fun getLocation() {
        launchIO {
            if (prefsImpl.getCountry() != "") {
                var s = ""

                s += prefsImpl.getCountry()

                if (prefsImpl.getCity() != "") {
                    s += ", " + prefsImpl.getCity()
                }

                countryCityNames.postValue(s)
            } else {
                countryCityNames.postValue("Местоположение")
            }
        }
    }

    fun getCountryFrom() {
        launchIO {
            if (prefsImpl.getCountryFrom() != "") {
                var s = ""

                s += prefsImpl.getCountryFrom()

                if (prefsImpl.getCityFrom() != "") {
                    s += ", " + prefsImpl.getCityFrom()
                }

                countryFromCityNames.postValue(s)
            } else {
                countryFromCityNames.postValue("Откуда вы?")
            }
        }
    }
}