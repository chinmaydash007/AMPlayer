package com.github.yoheimuta.amplayer

data class Session(
    var videoUrl: String? = null,
    var title: String? = null,
    var artist: String? = null,
    var author: String? = null,
    var duration: String? = null,
    var description: String? = null,
    var likedStatus: Boolean = false,
    var thumbnailUrl: String? = null,
    var completionRatio: Float = 0f,
    var sessionId: Int = 0,
    var calorie: Int = 0,
    var timingId: Int = 0,
    var albumsSessionsId: Int = 0,
    var coachName: String? = null,
    var feedbackStatus: Int = 0,
    var category: String? = null,
    var metFactor: Long? = null,
    var weight: Long? = null
)
