package kz.diaspora.app.data.cloud.rest

import kz.diaspora.app.data.cloud.firebase.Constants.Companion.CONTENT_TYPE
import kz.diaspora.app.data.cloud.firebase.Constants.Companion.SERVER_KEY
import kz.diaspora.app.domain.model.*
import kz.diaspora.app.domain.model.requests.LoginUserRequest
import kz.diaspora.app.domain.model.requests.RegisterUserRequest
import kz.diaspora.app.domain.model.requests.SendCommentRequest
import kz.diaspora.app.domain.model.response.EmptyRequest
import kz.diaspora.app.domain.model.response.ManipulatePostStatus
import kz.diaspora.app.domain.model.response.SendCommentResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.*

interface ApiService {

    @POST("api/login")
    suspend fun login(@Body userRequestData: LoginUserRequest): UserWithToken

    @POST("api/register")
    suspend fun register(@Body userRequestData: RegisterUserRequest): UserWithToken

    @POST("api/user/edit")
    suspend fun editUser(@Body userRequestData: User): User

    @GET("api/user/show")
    suspend fun getUserById(@Query("user_id") user_id: Int): User

    @POST("api/comment/create")
    suspend fun sendCommentToPost(@Body commentRequestData: SendCommentRequest): SendCommentResponse

    @GET("api/maritial_statues")
    suspend fun getStatuses(): List<MaritalStatusModel>

    @GET("api/country/list")
    suspend fun getCountryList(): List<CountryListItem>

    @GET("api/categories")
    suspend fun getCategoryList(): CategoryList

//    @GET("api/posts")
//    suspend fun getPostList(): List<PostModel>

    @GET("api/posts")
    suspend fun getPostList(@Query("category_id") category_id: Int?): List<PostModel>

    @GET("api/posts/count")
    suspend fun getPostsCount(): PostsCountModel

    @GET("api/post/show")
    suspend fun getDetailedPost(@Query("id") post_id: Int): DetailedPostModel

    @POST("api/posts/activate")
    suspend fun activatePost(@Body post: PostModel): ManipulatePostStatus

    @POST("api/posts/deactivate")
    suspend fun deactivatePost(@Body post: PostModel): ManipulatePostStatus

    @POST("api/post/liked/add")
    suspend fun likePost(@Query("post_id") post_id: Int): StatusModel

    @DELETE("api/post/liked/delete")
    suspend fun unlikePost(@Query("post_id") post_id: Int): StatusModel

    @GET("api/posts/wait")
    suspend fun getPostListInWait(): List<PostModel>

    @GET("api/posts/not_active")
    suspend fun getPostListNotActive(): List<PostModel>

    @GET("api/posts/active")
    suspend fun getPostListActive(): List<PostModel>

    @GET("api/posts/liked")
    suspend fun getPostListFavorite(): List<PostModel>

    @GET("api/city/list")
    suspend fun getCityList(@Query("country_id") country_id: Int): List<CityListItem>

    @GET("api/exhibits/{id}")
    suspend fun getProject(
        @Path("id") id: Int?,
        @Query("lang") lang: String?
    ): PostModel

    @GET("api/exhibits")
    suspend fun getProjects(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?,
        @Query("search") search: String?,
        @Query("lang") lang: String?,
        @Query("by_date") by_date: String?
    ): List<PostModel>

    @GET("api/exhibits/popular")
    suspend fun getPopular(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?,
        @Query("search") search: String?,
        @Query("lang") lang: String?
    ): List<PostModel>

    @GET("api/info")
    suspend fun getAbout(
        @Query("lang") lang: String?
    ): AboutModel

    @Multipart
    @POST("/api/post/create")
    suspend fun createPost(
        @Part post_images: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part("city_id") city_id: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody
    ): CreatePostModel

    @Multipart
    @POST("/api/user/avatar/change")
    suspend fun changeUserAvatar(
        @Part image: MultipartBody.Part
    ): User

    @Multipart
    @POST("/api/post/edit")
    suspend fun editPost(
        @Part("post_id") post_id: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part("city_id") city_id: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody
    ): CreatePostModel

    @Multipart
    @POST("/api/post/avatar/change")
    suspend fun changeMainImageOfPost(
        @Part("post_id") post_id: RequestBody,
        @Part image: MultipartBody.Part,
    ): CreatePostModel

    @GET("api/comments")
    suspend fun getNewComments(): List<NotificationModel>

    @GET("api/chats")
    suspend fun getChats(): List<ChatModel>

    @POST("api/chat/run")
    suspend fun joinChat(
        @Query("chat_id") chat_id: Int,
    ): StatusModel

    @GET("api/chat/messages")
    suspend fun getMessages(
        @Query("chat_id") chat_id: Int
    ): MessagesModel

    @POST("api/chat/send_message")
    suspend fun sendMessage(
        @Query("chat_id") chat_id: Int,
        @Query("text") text: String,
        @Query("target_user_id") target_user_id: Int,
    ): EmptyRequest

    @POST("api/chat/liked")
    suspend fun likeChat(
        @Query("chat_id") chat_id: Int
    ): StatusModel

    @POST("api/user/country")
    suspend fun saveCityTo(
        @Query("city_id") city_id: Int
    ): StatusModel

    @POST("api/user/edit")
    suspend fun saveCityFrom(
        @Query("city_id") city_id: Int
    ): StatusModel

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotificationModel
    ): Response<ResponseBody>

    @POST("api/device_token/create")
    suspend fun createDeviceToken(
            @Query("device_token") device_token: String?,
    ):UserWithToken

    @GET("api/chat/users")
    suspend fun getUsersInChat(
        @Query("chat_id") chat_id: Int
    ): List<User>

}