package com.github.yoheimuta.amplayer.playback

import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import com.github.yoheimuta.amplayer.MainActivity
import com.github.yoheimuta.amplayer.PlayList
import com.github.yoheimuta.amplayer.extensions.id

class InMemorySource() : AbstractMusicSource() {

    private var catalog: List<MediaMetadataCompat> = emptyList()

    init {
        state = STATE_INITIALIZING
    }

    override fun iterator(): Iterator<MediaMetadataCompat> = catalog.iterator()

    override suspend fun load() {
        catalog = PlayList.getCatalog()
        state = STATE_INITIALIZED
    }

//      fun getCatalog(): List<MediaMetadataCompat> {
//        return listOf(
//            Pair(
//                "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
//                "TEST_1"
//            ),
//            Pair(
//                "https://tik.getvisitapp.com/output/session-44/hls/session-44-Mindful-use-of-Technology.m3u8",
//                "TEST_2"
//            ) ,Pair(
//                    "https://tik.getvisitapp.com/output/session-30/hls/anxiety1_mixdown.m3u8",
//            "TEST_3"
//        )
//        ).map { (url, title) ->
//            MediaMetadataCompat.Builder()
//                .apply {
//                    putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "1")
//                    putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, url)
//                    putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
//                    putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, "Ankit")
//                    putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, "Ankit")
//                }
//                .build()
//        }
 //  }

}
