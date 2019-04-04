package com.anwesh.uiprojects.htobifourview

/**
 * Created by anweshmishra on 04/04/19.
 */

import android.view.View
import android.view.MotionEvent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color
import android.content.Context
import android.app.Activity

val nodes : Int = 5
val lines : Int = 2
val scGap : Float = 0.05f
val scDiv : Double = 0.51
val strokeFactor : Int = 90
val sizeFactor : Int = 90
val foreColor : Int = Color.parseColor("#283593")
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.scaleFactor() : Float = Math.floor(this / scDiv).toFloat()
fun Float.mirrorValue(a : Int, b : Int) : Float = (1 - scaleFactor()) * a.inverse() + scaleFactor() * b.inverse()
fun Float.updateValue(dir : Float, a : Int, b : Int) : Float = mirrorValue(a, b) * scGap * dir
fun Float.updateDFromS(d : Float, scale : Float) : Float = this + (d - this) * scale

fun Canvas.drawHTBFNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = h / (nodes + 1)
    val size : Float = gap / sizeFactor
    val sc1 : Float = scale.divideScale(0, lines)
    val sc2 : Float = scale.divideScale(1, lines)
    paint.color = foreColor
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.strokeCap = Paint.Cap.ROUND
    save()
    translate(w /2 , gap * (i + 1))
    for (j in 0..(lines - 1)) {
        val scj : Float = sc2.divideScale(j, lines)
        val x : Float = (size / 2).updateDFromS(w / 2 - size / 2, scj)
        save()
        scale(1f - 2 * j, 1f)
        translate(size / 2 + (w / 2 - size / 2), 0f)
        rotate(90f * scj)
        drawLine(0f, -size, 0f, size, paint)
        drawLine(0f, 0f, -size / 2, 0f, paint)
        drawLine(-size / 2, 0f, -size / 2, (size / 2) * sc1, paint)
        restore()
    }
    restore()
}

class HToBiFourView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scale.updateValue(dir, 1, lines)
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    companion object {
        fun create(activity : Activity) : HToBiFourView {
            val view : HToBiFourView = HToBiFourView(activity)
            activity.setContentView(view)
            return view
        }
    }
}