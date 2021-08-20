package kz.diaspora.app.domain.model

data class PushNotificationModel (
    val data: NotificationData,
    val to: String
        )