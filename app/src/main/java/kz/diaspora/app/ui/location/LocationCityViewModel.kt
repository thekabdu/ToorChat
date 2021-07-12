package kz.diaspora.app.ui.location


import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.diaspora.app.core.BaseViewModel
import kz.diaspora.app.data.cloud.ResultWrapper
import kz.diaspora.app.data.cloud.repository.BaseCloudRepository
import kz.diaspora.app.data.db.PrefsImpl
import kz.diaspora.app.domain.model.CityList
import kz.diaspora.app.domain.model.CityListItem
import kz.diaspora.app.domain.model.CountryListItem
import kz.diaspora.app.domain.model.StatusModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class LocationCityViewModel @Inject constructor(
        private val prefsImpl: PrefsImpl,
        private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }
    val cityList: MutableLiveData<List<CityListItem>> by lazy { MutableLiveData<List<CityListItem>>() }
    val message: MutableLiveData<StatusModel> by lazy { MutableLiveData<StatusModel>() }
    val searchCityList: MutableLiveData<List<CityListItem>> by lazy { MutableLiveData<List<CityListItem>>() }
    val searchList: MutableLiveData<ArrayList<String>> by lazy { MutableLiveData<ArrayList<String>>() }

    init {
        if (prefsImpl.getCountryId() != null)
            getCityList(prefsImpl.getCountryId()!!)
    }

    fun getCityList(id: String) {
        launchIO {
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

    fun getSearchList(searchText: String) {
        launchIO {
            isRefreshing.postValue(true)
            val fromList = ArrayList<String>()
            val newList = ArrayList<CityListItem>()
            for (i in cityList.value?.indices!!) {
                val city = cityList.value!!.get(i)
                if (city.city_name.toLowerCase(Locale.ROOT).contains(searchText.toLowerCase(
                        Locale.ROOT))) {
                    fromList.add(city.city_name)
                    newList.add(city)
                }
            }
            searchList.postValue(fromList)
            searchCityList.postValue(newList)
            isRefreshing.postValue(false)
        }
    }

    fun saveCity(cityId: String, city: String) {
        prefsImpl.setCityId(cityId)
        prefsImpl.setCity(city)
        launchIO {
            isRefreshing.postValue(true)
            val data = baseCloudRepository.saveCityTo(Integer.parseInt(cityId))
            when (data) {
                is ResultWrapper.Error -> error.postValue(data)
                is ResultWrapper.Success -> {
                    message.postValue(data.value)
                }
            }
            isRefreshing.postValue(false)
        }
    }

    fun clearSearch() {
        searchList.postValue(null)
        searchCityList.postValue(null)
    }

}