package com.anwesh.uiprojects.linkedhtobifourview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.htobifourview.HToBiFourView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HToBiFourView.create(this)
    }
}
