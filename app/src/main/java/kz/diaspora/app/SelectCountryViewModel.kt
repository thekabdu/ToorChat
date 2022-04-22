package kz.diaspora.app

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.diaspora.app.core.BaseViewModel
import kz.diaspora.app.data.cloud.ResultWrapper
import kz.diaspora.app.data.cloud.repository.BaseCloudRepository
import kz.diaspora.app.data.db.PrefsImpl
import kz.diaspora.app.domain.model.CityListItem
import kz.diaspora.app.domain.model.CountryListItem
import kz.diaspora.app.domain.model.PostsCountModel
import kz.diaspora.app.domain.model.User
import javax.inject.Inject


@HiltViewModel
class SelectCountryViewModel  @Inject constructor(
    private val prefsImpl: PrefsImpl,
    private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }
    val userData: MutableLiveData<User> by lazy { MutableLiveData<User>() }
    val countryList: MutableLiveData<List<CountryListItem>> by lazy { MutableLiveData<List<CountryListItem>>() }
    val cityList: MutableLiveData<List<CityListItem>> by lazy { MutableLiveData<List<CityListItem>>() }
    val countryCityNames: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val countryFromCityNames: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val countryFrom: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val countsData: MutableLiveData<PostsCountModel> by lazy { MutableLiveData<PostsCountModel>() }

    init {
        getCountryList()
        getLocation()
        getCountryFrom()
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