package `is`.xyz.mpv

import android.content.Context
import android.graphics.Bitmap
import android.view.Surface

// Wrapper for native library
@Suppress("unused")
object MPVLib {
    init {
        val libs = arrayOf("mpv", "player")
        for (lib in libs) {
            System.loadLibrary(lib)
        }
    }

    external fun create(appctx: Context)
    external fun init()
    external fun destroy()
    external fun attachSurface(surface: Surface)
    external fun detachSurface()
    external fun setSurfaceRefreshRate(fps: Float)
    external fun command(cmd: Array<String>)
    external fun setPropertyInt(property: String, value: Int)
    external fun setPropertyDouble(property: String, value: Double)
    external fun setPropertyBoolean(property: String, value: Boolean)
    external fun setPropertyString(property: String, value: String)
    external fun getPropertyInt(property: String): Int?
    external fun getPropertyDouble(property: String): Double?
    external fun getPropertyBoolean(property: String): Boolean?
    external fun getPropertyString(property: String): String?
    external fun getPropertyChapters(property: String): Int
    external fun getPropertyPlaybackPosition(): Int
    external fun getPropertyPlaybackDuration(): Int
    external fun getPropertyPercentPosition(): Int
    external fun observeProperty(property: String, format: Int)

    interface EventObserver {
        fun eventProperty(property: String)
        fun eventProperty(property: String, value: Long)
        fun eventProperty(property: String, value: Boolean)
        fun eventProperty(property: String, value: String)
        fun ePevent(event: String)
    }

    private val observers = mutableListOf<EventObserver>()

    @JvmStatic
    fun addObserver(o: EventObserver) {
        synchronized(observers) {
            observers.add(o)
        }
    }

    @JvmStatic
    fun removeObserver(o: EventObserver) {
        synchronized(observers) {
            observers.remove(o)
        }
    }
}
