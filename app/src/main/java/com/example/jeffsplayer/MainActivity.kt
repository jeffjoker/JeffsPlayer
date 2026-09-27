package com.example.jeffsplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.FrameLayout
import is.xyz.mpv.MPVView

class MainActivity : AppCompatActivity() {
    private lateinit var mpvView: MPVView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = FrameLayout(this)
        mpvView = MPVView(this, null)
        layout.addView(mpvView)
        setContentView(layout)

        // Play a sample video link
        mpvView.play("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
    }

    override fun onDestroy() {
        super.onDestroy()
        mpvView.destroy()
    }
}
