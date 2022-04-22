package kz.diaspora.app.ui.newlocation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.diaspora.app.core.BaseViewModel
import kz.diaspora.app.data.cloud.ResultWrapper
import kz.diaspora.app.data.cloud.repository.BaseCloudRepository
import kz.diaspora.app.data.db.PrefsImpl
import kz.diaspora.app.domain.model.CountryListItem
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class NewSelectCountryFromViewModel @Inject constructor(
    private val prefsImpl: PrefsImpl,
    private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }
    val countryList: MutableLiveData<List<CountryListItem>> by lazy { MutableLiveData<List<CountryListItem>>() }
    val searchCountryList: MutableLiveData<List<CountryListItem>> by lazy { MutableLiveData<List<CountryListItem>>() }
    val searchList: MutableLiveData<ArrayList<String>> by lazy { MutableLiveData<ArrayList<String>>() }

    init {
        getCountryList()
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

    fun getSearchList(searchText: String) {
        launchIO {
            isRefreshing.postValue(true)
            val fromList = ArrayList<String>()
            val newList = ArrayList<CountryListItem>()
            for (i in countryList.value?.indices!!) {
                val country = countryList.value!!.get(i)
                if (country.country_name.toLowerCase(Locale.ROOT).contains(searchText.toLowerCase(
                        Locale.ROOT))) {
                    fromList.add(country.country_name)
                    newList.add(country)
                }
            }
            searchList.postValue(fromList)
            searchCountryList.postValue(newList)
            isRefreshing.postValue(false)
        }
    }

    fun saveCountry(countryId: String, country: String) {
        prefsImpl.setCountryFromId(countryId)
        prefsImpl.setCountryFrom(country)
    }

    fun clearSearch() {
        searchList.postValue(null)
        searchCountryList.postValue(null)
    }

}