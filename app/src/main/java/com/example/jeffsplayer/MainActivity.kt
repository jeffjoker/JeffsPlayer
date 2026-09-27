package com.example.jeffsplayer

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import `is`.xyz.mpv.MPVLib

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback {
    private lateinit var surfaceView: SurfaceView
    private lateinit var urlEditText: EditText
    private lateinit var playButton: Button
    private var isPlayerCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Root container holding both video surface and UI controls
        val rootLayout = FrameLayout(this)

        // 1. SurfaceView for video playback (occupies the background)
        surfaceView = SurfaceView(this)
        rootLayout.addView(
            surfaceView, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        // 2. Temporary URL Input Overlay at the top
        val controlsLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(32, 32, 32, 32)
            setBackgroundColor(Color.parseColor("#80000000")) // Semi-transparent dark background
        }

        urlEditText = EditText(this).apply {
            hint = "Enter http:// stream URL"
            setText("http://")
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                marginEnd = 16
            }
        }

        playButton = Button(this).apply {
            text = "Play"
            setOnClickListener {
                val url = urlEditText.text.toString().trim()
                if (url.isNotEmpty()) {
                    try {
                        MPVLib.command(arrayOf("loadfile", url))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        controlsLayout.addView(urlEditText)
        controlsLayout.addView(playButton)

        rootLayout.addView(
            controlsLayout, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.TOP
            }
        )

        setContentView(rootLayout)
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
