package kz.diaspora.app.data.db

import kz.diaspora.app.domain.model.SavePostModel
import kz.diaspora.app.domain.model.User

interface Prefs {

    fun setLanguage(lang: String)
    fun getLanguage(): String

    fun setCountryId(countryId: String)
    fun getCountryId(): String?

    fun setCountryFromId(countryId: String)
    fun getCountryFromId(): String?

    fun setCountryPostId(countryId: String)
    fun getCountryPostId(): String?

    fun setCountry(country: String)
    fun getCountry(): String?

    fun setCountryFrom(country: String)
    fun getCountryFrom(): String?

    fun setCountryPost(country: String)
    fun getCountryPost(): String?

    fun setCityId(cityId: String)
    fun getCityId(): String?

    fun setCityFromId(cityId: String)
    fun getCityFromId(): String?

    fun setCityPostId(cityId: String)
    fun getCityPostId(): String?

    fun setCity(city: String)
    fun getCity(): String?

    fun setCityFrom(city: String)
    fun getCityFrom(): String?

    fun setCityPost(city: String)
    fun getCityPost(): String?

    fun setRatings(id: Int)
    fun getRatings(): String

    fun getToken(): String?
    fun setToken(token: String)

    fun getUser(): User?
    fun setUser(user:User)

    fun getPostData(): SavePostModel?
    fun setPostData(post:SavePostModel?)

    fun logOut()
}
