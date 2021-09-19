package kz.diaspora.app.data.cloud.repository

import kz.diaspora.app.data.cloud.ResultWrapper
import kz.diaspora.app.domain.model.*
import kz.diaspora.app.domain.model.response.EmptyRequest
import kz.diaspora.app.domain.model.response.ManipulatePostStatus
import kz.diaspora.app.domain.model.response.SendCommentResponse

interface BaseCloudRepository {

    suspend fun getProject(id: Int?, lang: String?): ResultWrapper<PostModel>

    suspend fun getAdverts(
        page: Int?,
        limit: Int?,
        search: String?,
        lang: String?,
        by_date: String?
    ): ResultWrapper<List<PostModel>>

    suspend fun getPopular(
        page: Int?,
        limit: Int?,
        search: String?,
        lang: String?
    ): ResultWrapper<List<PostModel>>

    suspend fun getAbout(lang: String?): ResultWrapper<AboutModel>

    suspend fun getUserById(userId: Int): ResultWrapper<User>

    suspend fun login(name: String, password: String): ResultWrapper<UserWithToken>

    suspend fun register(
        username: String,
        password: String,
        name: String,
        surname: String,
        phone_or_email: String
    ): ResultWrapper<UserWithToken>

    suspend fun editUser(user: User): ResultWrapper<User>

    suspend fun getStatuses(): ResultWrapper<List<MaritalStatusModel>>

    suspend fun getCountryList(): ResultWrapper<List<CountryListItem>>

    suspend fun getCityList(id: Int): ResultWrapper<List<CityListItem>>

    suspend fun getCategoryList(): ResultWrapper<CategoryList>

//    suspend fun getPostList(): ResultWrapper<List<PostModel>>

    suspend fun getPostList(category_id: Int?): ResultWrapper<List<PostModel>>

    suspend fun getDetailedPost(id: Int): ResultWrapper<DetailedPostModel>
    suspend fun getPostsCount(): ResultWrapper<PostsCountModel>

    suspend fun getPostListInWait(): ResultWrapper<List<PostModel>>
    suspend fun getPostListActive(): ResultWrapper<List<PostModel>>
    suspend fun getPostListNotActive(): ResultWrapper<List<PostModel>>
    suspend fun getPostListFavorite(): ResultWrapper<List<PostModel>>

    suspend fun createPost(
        image_paths: String,
        title: String,
        description: String,
        category_id: String,
        city_id: String,
        email: String,
        phone: String
    ): ResultWrapper<CreatePostModel>

    suspend fun editPost(
        post_id: String,
        title: String,
        description: String,
        category_id: String,
        city_id: String,
        email: String,
        phone: String
    ): ResultWrapper<CreatePostModel>

    suspend fun changeMainImageOfPost(
        post_id: String,
        image_path: String
    ): ResultWrapper<CreatePostModel>

    suspend fun changeUserAvatar(
        image_path: String,
    ): ResultWrapper<User>

    suspend fun sendCommentToPost(
        post_id: Int,
        message: String
    ): ResultWrapper<SendCommentResponse>

    suspend fun getNewComments(): ResultWrapper<List<NotificationModel>>

    suspend fun activatePost(post: PostModel): ResultWrapper<ManipulatePostStatus>

    suspend fun deactivatePost(post: PostModel): ResultWrapper<ManipulatePostStatus>

    suspend fun likePost(post_id: Int): ResultWrapper<StatusModel>

    suspend fun unlikePost(post_id: Int): ResultWrapper<StatusModel>

    suspend fun getChats(): ResultWrapper<List<ChatModel>>

    suspend fun joinChat(chat_id: Int): ResultWrapper<StatusModel>

    suspend fun getMessages(chat_id: Int): ResultWrapper<MessagesModel>

    suspend fun getUsersInChat(chat_id: Int): ResultWrapper<List<User>>

    suspend fun likeChat(chat_id: Int): ResultWrapper<StatusModel>

    suspend fun sendMessage(chat_id: Int,
                            text: String,
                            target_user_id: Int,
    ): ResultWrapper<EmptyRequest>

    suspend fun saveCityTo(city_id: Int): ResultWrapper<StatusModel>

    suspend fun saveCityFrom(city_id: Int): ResultWrapper<StatusModel>

    suspend fun createDeviceToken(device_token:String):ResultWrapper<UserWithToken>

}
