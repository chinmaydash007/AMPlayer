package com.github.yoheimuta.amplayer

import android.support.v4.media.MediaMetadataCompat

class PlayList {

    companion object {

        var songList = mutableListOf<Session>()


        fun getCatalog(): List<MediaMetadataCompat> {
            return songList.map { session ->
                MediaMetadataCompat.Builder()
                    .apply {
                        putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, session.videoUrl)
                        putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, session.videoUrl)
                        putString(MediaMetadataCompat.METADATA_KEY_TITLE, session.title)
                        putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, session.author)
                        putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, session.artist)
                    }
                    .build()
            }
        }


//        fun getCatalog(): List<MediaMetadataCompat> {
//            return listOf(
//                Pair(
//                    "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
//                    "TEST_1"
//                ),
//                Pair(
//                    "https://tik.getvisitapp.com/output/session-44/hls/session-44-Mindful-use-of-Technology.m3u8",
//                    "TEST_2"
//                ) ,Pair(
//                    "https://tik.getvisitapp.com/output/session-30/hls/anxiety1_mixdown.m3u8",
//                    "TEST_3"
//                )
//            ).map { (url, title) ->
//                MediaMetadataCompat.Builder()
//                    .apply {
//                        putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, url)
//                        putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, url)
//                        putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
//                        putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, "Ankit")
//                        putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, "Ankit")
//                    }
//                    .build()
//            }
//        }
    }
}