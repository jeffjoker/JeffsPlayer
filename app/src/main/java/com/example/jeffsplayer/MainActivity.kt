package com.example.jeffsplayer

import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

        // Root container for video rendering and control overlay
        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.BLACK)
        }

        // SurfaceView where libmpv renders frames
        surfaceView = SurfaceView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
            holder.addCallback(this@MainActivity)
        }
        rootLayout.addView(surfaceView)

        // Control layout optimized for Android TV D-pad navigation
        val controlLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(32, 32, 32, 32)
        }

        urlEditText = EditText(this).apply {
            hint = "Enter HTTP Stream URL"
            setTextColor(Color.WHITE)
            setHintTextColor(Color.GRAY)
            isFocusable = true
            isFocusableInTouchMode = true
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
        }
        controlLayout.addView(urlEditText)

        playButton = Button(this).apply {
            text = "Play"
            isFocusable = true
            isFocusableInTouchMode = true
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener {
                val streamUrl = urlEditText.text.toString().trim()
                if (streamUrl.isNotEmpty()) {
                    MPVLib.command(arrayOf("loadfile", streamUrl))
                }
            }
        }
        controlLayout.addView(playButton)

        rootLayout.addView(controlLayout)
        setContentView(rootLayout)
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
        // Handle size/format changes if needed
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        try {
            MPVLib.detachSurface()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, KeyEvent.KEYCODE_SPACE -> {
                MPVLib.command(arrayOf("cycle", "pause"))
                return true
            }
            KeyEvent.KEYCODE_MEDIA_STOP -> {
                MPVLib.command(arrayOf("stop"))
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
