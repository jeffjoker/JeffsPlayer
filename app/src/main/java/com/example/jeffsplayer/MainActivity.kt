package com.example.jeffsplayer

import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import `is`.xyz.mpv.MPVLib

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback {
    private lateinit var surfaceView: SurfaceView
    private var isPlayerCreated = false

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
            if (!isPlayerCreated) {
                MPVLib.create(applicationContext)
                MPVLib.init()
                isPlayerCreated = true
            }
            
            MPVLib.attachSurface(holder.surface)
            
            // Load a test video stream
            MPVLib.command(arrayOf("loadfile", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        try {
            MPVLib.setPropertyInt("osd-wdt-size", width)
            MPVLib.setPropertyInt("osd-height", height)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        try {
            MPVLib.detachSurface()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        // Optional: pause or handle background state if needed
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPlayerCreated) {
            try {
                MPVLib.destroy()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isPlayerCreated = false
        }
    }
}
