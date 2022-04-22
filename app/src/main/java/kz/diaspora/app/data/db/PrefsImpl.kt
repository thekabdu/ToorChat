package kz.diaspora.app.data.db

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import kz.diaspora.app.domain.model.SavePostModel
import kz.diaspora.app.domain.model.User
import javax.inject.Inject

class PrefsImpl @Inject constructor(appContext: Context) : Prefs {

    private val LANGUAGE = "LANGUAGE"
    private val RATINGS = "RATINGS"
    private val USER = "USER"
    private val TOKEN = "TOKEN"
    private val CITY_ID = "CITY_ID"
    private val COUNTRY_ID = "COUNTRY_ID"
    private val CITY_FROM_ID = "CITY_FROM_ID"
    private val COUNTRY_FROM_ID = "COUNTRY_FROM_ID"
    private val CITY_POST_ID = "CITY_POST_ID"
    private val COUNTRY_POST_ID = "COUNTRY_POST_ID"
    private val CITY = "CITY"
    private val CITY_FROM = "CITY_FROM"
    private val CITY_POST = "CITY_POST"
    private val COUNTRY = "COUNTRY"
    private val COUNTRY_FROM = "COUNTRY_FROM"
    private val COUNTRY_POST = "COUNTRY_POST"
    private val POST = "POST"

    private val gson = Gson()

    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext)

    override fun setLanguage(lang: String) {
        val editor = prefs.edit()
        editor.putString(LANGUAGE, lang)
        editor.apply()
    }

    override fun getLanguage(): String {
        return prefs.getString(LANGUAGE, "ru") ?: "ru"
    }

    override fun setCountryId(countryID: String) {
        val editor = prefs.edit()
        editor.putString(COUNTRY_ID, countryID)
        editor.apply()
    }

    override fun getCountryId(): String? {
        return prefs.getString(COUNTRY_ID, "")
    }

    override fun setCountryFromId(countryFromId: String) {
        val editor = prefs.edit()
        editor.putString(COUNTRY_FROM_ID, countryFromId)
        editor.apply()
    }

    override fun getCountryFromId(): String? {
        return prefs.getString(COUNTRY_FROM_ID, "")
    }

    override fun setCountryPostId(countryId: String) {
        val editor = prefs.edit()
        editor.putString(COUNTRY_POST_ID, countryId)
        editor.apply()
    }

    override fun getCountryPostId(): String? {
        return prefs.getString(COUNTRY_POST_ID, "")
    }

    override fun setCountry(country: String) {
        val editor = prefs.edit()
        editor.putString(COUNTRY, country)
        editor.apply()
    }

    override fun getCountry(): String? {
        return prefs.getString(COUNTRY, "")
    }

    override fun setCountryPost(country: String) {
        val editor = prefs.edit()
        editor.putString(COUNTRY_POST, country)
        editor.apply()
    }

    override fun getCountryPost(): String? {
        return prefs.getString(COUNTRY_POST, "")
    }

    override fun setCountryFrom(country: String) {
        val editor = prefs.edit()
        editor.putString(COUNTRY_FROM, country)
        editor.apply()
    }

    override fun getCountryFrom(): String? {
        return prefs.getString(COUNTRY_FROM, "")
    }

    override fun setCityId(city: String) {
        val editor = prefs.edit()
        editor.putString(CITY_ID, city)
        editor.apply()
    }

    override fun getCityId(): String? {
        return prefs.getString(CITY_ID, "")
    }

    override fun setCityFromId(cityFromId: String) {
        val editor = prefs.edit()
        editor.putString(CITY_FROM_ID, cityFromId)
        editor.apply()
    }

    override fun getCityFromId(): String? {
        return prefs.getString(CITY_FROM_ID, "")
    }

    override fun setCityPostId(cityId: String) {
        val editor = prefs.edit()
        editor.putString(CITY_POST_ID, cityId)
        editor.apply()
    }

    override fun getCityPostId(): String? {
        return prefs.getString(CITY_POST_ID, "")
    }

    override fun setCity(country: String) {
        val editor = prefs.edit()
        editor.putString(CITY, country)
        editor.apply()
    }

    override fun getCity(): String? {
        return prefs.getString(CITY, "")
    }

    override fun setCityFrom(city: String) {
        val editor = prefs.edit()
        editor.putString(CITY_FROM, city)
        editor.apply()
    }

    override fun getCityFrom(): String? {
        return prefs.getString(CITY_FROM, "")
    }

    override fun setCityPost(city: String) {
        val editor = prefs.edit()
        editor.putString(CITY_POST, city)
        editor.apply()    }

    override fun getCityPost(): String? {
        return prefs.getString(CITY_POST, "")
    }


    override fun setRatings(id: Int) {
        val editor = prefs.edit()
        var def = prefs.getString(RATINGS, "")
        def += " $id"
        editor.putString(RATINGS, def)
        editor.apply()
    }

    override fun getRatings(): String {
        return prefs.getString(RATINGS, "") ?: ""
    }

    override fun getToken(): String? {
        return prefs.getString(TOKEN, "")
    }

    override fun setToken(token: String) {
        val editor = prefs.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    override fun getUser(): User? {
        val json = prefs.getString(USER, null)
        return gson.fromJson(json, User::class.java)
    }

    override fun setUser(user: User) {
        val editor = prefs.edit()
        val json = gson.toJson(user)
        editor.putString(USER, json)
        editor.apply()
    }

    override fun getPostData(): SavePostModel? {
        val json = prefs.getString(POST, null)
        return gson.fromJson(json, SavePostModel::class.java)
    }

    override fun setPostData(post: SavePostModel?) {
        val editor = prefs.edit()
        val json = gson.toJson(post)
        editor.putString(POST, json)
        editor.apply()
    }

    override fun logOut() {
        val editor = prefs.edit()
        editor.putString(TOKEN, null)
        editor.putString(USER, null)
        editor.putString(CITY, null)
        editor.putString(COUNTRY, null)
        editor.apply()
    }
}