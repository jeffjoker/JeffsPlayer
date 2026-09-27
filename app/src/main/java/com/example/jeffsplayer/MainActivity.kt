package com.example.jeffsplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.FrameLayout
import tyrant.libmpv.MPVView

class MainActivity : AppCompatActivity() {
    private lateinit var mpvView: MPVView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Create a full-screen layout for the TV video surface
        val layout = FrameLayout(this)
        mpvView = MPVView(this, null)
        layout.addView(mpvView)
        setContentView(layout)

        // Test playing a sample stream link (Big Buck Bunny)
        mpvView.play("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
    }

    override fun onDestroy() {
        super.onDestroy()
        mpvView.destroy()
    }
}
