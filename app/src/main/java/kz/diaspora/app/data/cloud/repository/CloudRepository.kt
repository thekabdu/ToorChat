package kz.diaspora.app.data.cloud.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kz.diaspora.app.data.cloud.ResultWrapper
import kz.diaspora.app.data.cloud.rest.ApiService
import kz.diaspora.app.data.cloud.safeApiCall
import kz.diaspora.app.domain.model.*
import kz.diaspora.app.domain.model.requests.LoginUserRequest
import kz.diaspora.app.domain.model.requests.RegisterUserRequest
import kz.diaspora.app.domain.model.requests.SendCommentRequest
import kz.diaspora.app.domain.model.response.EmptyRequest
import kz.diaspora.app.domain.model.response.ManipulatePostStatus
import kz.diaspora.app.domain.model.response.SendCommentResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class CloudRepository(
    private val api: ApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseCloudRepository {

    override suspend fun getProject(
        id: Int?,
        lang: String?
    ): ResultWrapper<PostModel> {
        return safeApiCall(dispatcher) {
            api.getProject(id, lang)
        }
    }
    override suspend fun getPostListInWait(): ResultWrapper<List<PostModel>> {
        return safeApiCall(dispatcher) {
            api.getPostListInWait()
        }
    }

    override suspend fun getPostListActive(): ResultWrapper<List<PostModel>> {
        return safeApiCall(dispatcher) {
            api.getPostListActive()
        }
    }

    override suspend fun getPostListNotActive(): ResultWrapper<List<PostModel>> {
        return safeApiCall(dispatcher) {
            api.getPostListNotActive()
        }
    }

    override suspend fun getPostListFavorite(): ResultWrapper<List<PostModel>> {
        return safeApiCall(dispatcher) {
            api.getPostListFavorite()
        }
    }

    override suspend fun getAdverts(
        page: Int?,
        limit: Int?,
        search: String?,
        lang: String?,
        by_date: String?
    ): ResultWrapper<List<PostModel>> {
        return safeApiCall(dispatcher) {
            api.getProjects(page, limit, search, lang, by_date)
        }
    }

    override suspend fun getDetailedPost(id: Int): ResultWrapper<DetailedPostModel> {
        return safeApiCall(dispatcher) {
            api.getDetailedPost(id)
        }
    }

    override suspend fun getPopular(
        page: Int?,
        limit: Int?,
        search: String?,
        lang: String?
    ): ResultWrapper<List<PostModel>> {
        return safeApiCall(dispatcher) {
            api.getPopular(page, limit, search, lang)
        }
    }

    override suspend fun getAbout(
        lang: String?
    ): ResultWrapper<AboutModel> {
        return safeApiCall(dispatcher) {
            api.getAbout(lang)
        }
    }

    override suspend fun getUserById(userId: Int): ResultWrapper<User> {
        return safeApiCall(dispatcher) {
            api.getUserById(userId)
        }
    }

    override suspend fun login(name: String, password: String): ResultWrapper<UserWithToken> {
        val requestBody = LoginUserRequest(name, password)
        return safeApiCall(dispatcher) {
            api.login(requestBody)
        }
    }

    override suspend fun register(
        username: String,
        password: String,
        name: String,
        surname: String,
        phone_or_email: String
    ): ResultWrapper<UserWithToken> {
        val requestBody = RegisterUserRequest(username, password, name, surname, phone_or_email)
        return safeApiCall(dispatcher) {
            api.register(requestBody)
        }
    }

    override suspend fun editUser(user: User): ResultWrapper<User> {
        return safeApiCall(dispatcher) {
            api.editUser(user)
        }
    }

    override suspend fun getStatuses(): ResultWrapper<List<MaritalStatusModel>> {
        return safeApiCall(dispatcher) {
            api.getStatuses()
        }
    }

    override suspend fun getCountryList(): ResultWrapper<List<CountryListItem>> {
        return safeApiCall(dispatcher) {
            api.getCountryList()
        }
    }

    override suspend fun getCityList(id: Int): ResultWrapper<List<CityListItem>> {
        return safeApiCall(dispatcher) {
            api.getCityList(id)
        }
    }

    override suspend fun getCategoryList(): ResultWrapper<CategoryList> {
        return safeApiCall(dispatcher) {
            api.getCategoryList()
        }
    }

    override suspend fun getPostList(category_id: Int?): ResultWrapper<List<PostModel>> {
        return safeApiCall(dispatcher) {
            api.getPostList(category_id)
        }
    }


//    override suspend fun getPostList(): ResultWrapper<List<PostModel>> {
//        return safeApiCall(dispatcher) {
//            api.getPostList()
//        }
//    }

    override suspend fun createPost(
        image_paths: String,
        title: String,
        description: String,
        category_id: String,
        city_id: String,
        email: String,
        phone: String
    ): ResultWrapper<CreatePostModel> {
        return safeApiCall(dispatcher) {
//            val bodys = mutableListOf<MultipartBody.Part>()

//            for (index:Int in image_paths.indices) {
                val imageFile = File(image_paths)
                val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                val body = requestFile.let {
                    MultipartBody.Part.createFormData("post_images", imageFile.name, it)
                }
//                bodys.add(body)
//                Log.i("imagesofbody", body.toString())
//            }

            api.createPost(
                body,
                title.toRequestBody("text/plain".toMediaTypeOrNull()),
                description.toRequestBody("text/plain".toMediaTypeOrNull()),
                category_id.toRequestBody("text/plain".toMediaTypeOrNull()),
                city_id.toRequestBody("text/plain".toMediaTypeOrNull()),
                email.toRequestBody("text/plain".toMediaTypeOrNull()),
                phone.toRequestBody("text/plain".toMediaTypeOrNull()),
            )
        }
    }

    override suspend fun changeUserAvatar(
        image_path: String,
    ): ResultWrapper<User> {
        return safeApiCall(dispatcher) {

            val imageFile = File(image_path)
            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val body = requestFile.let {
                MultipartBody.Part.createFormData("image", imageFile.name, it)
            }

            api.changeUserAvatar(
                body
            )
        }
    }

    override suspend fun editPost(
        post_id: String,
        title: String,
        description: String,
        category_id: String,
        city_id: String,
        email: String,
        phone: String
    ): ResultWrapper<CreatePostModel> {
        return safeApiCall(dispatcher) {

            api.editPost(
                post_id.toRequestBody("text/plain".toMediaTypeOrNull()),
                title.toRequestBody("text/plain".toMediaTypeOrNull()),
                description.toRequestBody("text/plain".toMediaTypeOrNull()),
                category_id.toRequestBody("text/plain".toMediaTypeOrNull()),
                city_id.toRequestBody("text/plain".toMediaTypeOrNull()),
                email.toRequestBody("text/plain".toMediaTypeOrNull()),
                phone.toRequestBody("text/plain".toMediaTypeOrNull()),
            )
        }
    }

    override suspend fun changeMainImageOfPost(
        post_id: String,
        image_path: String
    ): ResultWrapper<CreatePostModel> {
        val imageFile = File(image_path)
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = requestFile.let {
            MultipartBody.Part.createFormData("image", imageFile.name, it)
        }

        return safeApiCall(dispatcher) {
            api.changeMainImageOfPost(
                post_id.toRequestBody("text/plain".toMediaTypeOrNull()),
                body
            )
        }
    }

    override suspend fun getPostsCount(): ResultWrapper<PostsCountModel> {
        return safeApiCall(dispatcher) {
            api.getPostsCount()
        }
    }

    override suspend fun sendCommentToPost(
        post_id: Int,
        message: String
    ): ResultWrapper<SendCommentResponse> {
        return safeApiCall(dispatcher) {
            val comment = SendCommentRequest(post_id, message)
            api.sendCommentToPost(comment)
        }
    }

    override suspend fun getNewComments(): ResultWrapper<List<NotificationModel>> {
        return safeApiCall(dispatcher) {
            api.getNewComments()
        }
    }

    override suspend fun activatePost(post: PostModel): ResultWrapper<ManipulatePostStatus> {
        return safeApiCall(dispatcher) {
            api.activatePost(post)
        }
    }

    override suspend fun deactivatePost(post: PostModel): ResultWrapper<ManipulatePostStatus> {
        return safeApiCall(dispatcher) {
            api.deactivatePost(post)
        }
    }

    override suspend fun likePost(post_id: Int): ResultWrapper<StatusModel> {
        return safeApiCall(dispatcher) {
            api.likePost(post_id)
        }
    }

    override suspend fun unlikePost(post_id: Int): ResultWrapper<StatusModel> {
        return safeApiCall(dispatcher) {
            api.unlikePost(post_id)
        }
    }

    override suspend fun getChats(): ResultWrapper<List<ChatModel>> {
        return safeApiCall(dispatcher) {
            api.getChats()
        }
    }

    override suspend fun joinChat(chat_id: Int): ResultWrapper<StatusModel> {
        return safeApiCall(dispatcher) {
            api.joinChat(chat_id)
        }
    }

    override suspend fun getMessages(chat_id: Int): ResultWrapper<MessagesModel> {
        return safeApiCall(dispatcher) {
            api.getMessages(chat_id)
        }
    }

    override suspend fun likeChat(chat_id: Int): ResultWrapper<StatusModel> {
        return safeApiCall(dispatcher) {
            api.likeChat(chat_id)
        }
    }

    override suspend fun sendMessage(chat_id: Int, text: String, target_user_id: Int):ResultWrapper<EmptyRequest> {
        return safeApiCall(dispatcher) {
            api.sendMessage(chat_id, text, target_user_id)
        }
    }

    override suspend fun saveCityTo(city_id: Int): ResultWrapper<StatusModel> {
        return safeApiCall(dispatcher) {
            api.saveCityTo(city_id)
        }
    }

    override suspend fun saveCityFrom(city_id: Int): ResultWrapper<StatusModel> {
        return safeApiCall(dispatcher) {
            api.saveCityFrom(city_id)
        }
    }
}
