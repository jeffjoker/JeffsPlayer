package com.example.jeffsplayer

import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import is.xyz.mpv.MPVLib

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback {
    private lateinit var surfaceView: SurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        surfaceView = SurfaceView(this)
        val layout = FrameLayout(this).apply {
            addView(surfaceView)
        }
        setContentView(layout)

        surfaceView.holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        try {
            // Initialize libmpv with cache/config paths required by the native wrapper
            MPVLib.create(this)
            MPVLib.init()
            
            MPVLib.attachSurface(holder.surface)
            MPVLib.command(arrayOf("loadfile", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        MPVLib.setPropertyInt("osd-wdt-size", width)
        MPVLib.setPropertyInt("osd-height", height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        try {
            MPVLib.detachSurface()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            MPVLib.destroy()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
