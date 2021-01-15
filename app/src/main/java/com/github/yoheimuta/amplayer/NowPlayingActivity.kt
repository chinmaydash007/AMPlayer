package com.github.yoheimuta.amplayer

import android.content.ComponentName
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.yoheimuta.amplayer.databinding.NowPlayingBinding
import com.github.yoheimuta.amplayer.extensions.id
import com.github.yoheimuta.amplayer.extensions.mediaUri
import com.github.yoheimuta.amplayer.extensions.title
import com.github.yoheimuta.amplayer.playback.GET_PLAYER_COMMAND
import com.github.yoheimuta.amplayer.playback.MUSIC_SERVICE_BINDER_KEY
import com.github.yoheimuta.amplayer.playback.MusicService
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

const val NOW_PLAYING_INTENT_MEDIA_ID = "mediaId"
private const val TAG = "NowPlayingActivity"

class NowPlayingActivity: AppCompatActivity() {

    private var mediaId: String? = null
    private lateinit var binding: NowPlayingBinding
    private lateinit var mediaBrowser: MediaBrowserCompat
    private lateinit var audioManager: AudioManager
    private lateinit var uiHandler: Handler

    private val playerEventListener = PlayerEventListener()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.now_playing)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        binding = DataBindingUtil.setContentView(this, R.layout.now_playing)
        binding.playerView.setControllerShowTimeoutMs(0)

        mediaId = "https://tik.getvisitapp.com/output/session-44/hls/session-44-Mindful-use-of-Technology.m3u8"
        mediaBrowser = MediaBrowserCompat(
            this,
            ComponentName(this, MusicService::class.java),
            ConnectionCallback(),
            null // optional Bundle
        )



        uiHandler = Handler(Looper.getMainLooper())
    }

    public override fun onStart() {
        super.onStart()
        mediaBrowser.connect()
    }

    override fun onPause() {
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    public override fun onStop() {
        super.onStop()
        mediaBrowser.disconnect()
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (binding.playerView.player != null) {
            binding.playerView.player!!.removeListener(playerEventListener)
        }
    }



    private fun buildUI() {
        val mediaController = MediaControllerCompat.getMediaController(this)
        mediaController.sendCommand(GET_PLAYER_COMMAND, Bundle(), ResultReceiver(Handler()))
    }

    private inner class ConnectionCallback : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            Log.i(TAG, "onConnected")

            mediaBrowser.sessionToken.also { token ->
                val mediaController = MediaControllerCompat(
                    this@NowPlayingActivity, // Context
                    token
                ).apply {
                    registerCallback(MediaControllerCallback())
                }

                MediaControllerCompat.setMediaController(this@NowPlayingActivity, mediaController)
            }

            buildUI()
        }

        override fun onConnectionSuspended() {
            Log.i(TAG, "The Service has crashed")
        }

        override fun onConnectionFailed() {
            Log.i(TAG, "The Service has refused our connection")
        }
    }


    private inner class ResultReceiver(handler: Handler): android.os.ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            val service = resultData.getBinder(MUSIC_SERVICE_BINDER_KEY)
            if (service is MusicService.MusicServiceBinder) {
                if (binding.playerView.player != null) {
                    return
                }
                val player = service.getExoPlayer()
                player.addListener(playerEventListener)
                binding.playerView.player = player
                changeBackground()

                player.addListener(object : Player.EventListener {
                    override fun onTracksChanged(
                        trackGroups: TrackGroupArray,
                        trackSelections: TrackSelectionArray
                    ) {
                    }

                    override fun onLoadingChanged(isLoading: Boolean) {}
                    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {}
                    override fun onPlayerError(error: ExoPlaybackException) {
                        //Catch here, but app still crash on some errors!
                        error.printStackTrace()
                    }
                })

                val mediaController =
                    MediaControllerCompat.getMediaController(this@NowPlayingActivity)
                mediaController.getTransportControls().prepareFromMediaId(mediaId, null)
            }
        }
    }

    fun changeBackground(){


        val urlImage =
            "https://visit-public.s3.ap-south-1.amazonaws.com/tik/assets/Album_Art-1.png"

        object : AsyncTask<String?, Int?, Drawable?>() {

            override fun onPostExecute(result: Drawable?) {

                //Add image to ImageView
                binding.playerView.defaultArtwork = result
            }

            override fun doInBackground(vararg params: String?): Drawable? {
                var bmp: Bitmap? = null
                try {
                    val connection = URL(urlImage).openConnection() as HttpURLConnection
                    connection.connect()
                    val input = connection.inputStream
                    bmp = BitmapFactory.decodeStream(input)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return BitmapDrawable(bmp)
            }
        }.execute()
    }

    private inner class MediaControllerCallback: MediaControllerCompat.Callback() {
        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            if (metadata == null) {
                return
            }
            else
                Log.i(TAG, metadata.id.toString())



        }
    }

    private inner class PlayerEventListener: Player.EventListener {
        override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
            Log.i(TAG, "onChanged Track")
        }
    }
}


