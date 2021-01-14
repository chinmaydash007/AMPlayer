package com.github.yoheimuta.amplayer.playback

import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import com.github.yoheimuta.amplayer.extensions.id

class InMemorySource() : AbstractMusicSource() {

    private var catalog: List<MediaMetadataCompat> = emptyList()

    init {
        state = STATE_INITIALIZING
    }

    override fun iterator(): Iterator<MediaMetadataCompat> = catalog.iterator()

    override suspend fun load() {
        catalog = getCatalog()
        state = STATE_INITIALIZED
    }

    private suspend fun getCatalog(): List<MediaMetadataCompat> {
        return listOf(
            Pair(
                "https://tik.getvisitapp.com/output/session-77/hls/session-77-Achieve-more-with-less.m3u8",
                "TEST_1"
            ),
            Pair(
                "https://tik.getvisitapp.com/output/session-44/hls/session-44-Mindful-use-of-Technology.m3u8",
                "TEST_2"
            )
        ).map { (url, title) ->
            MediaMetadataCompat.Builder()
                .apply {
                    putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, url)
                    putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, url)
                    putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                }
                .build()
        }
    }
}
