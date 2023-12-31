package com.akeso.depthestimationapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.akeso.depthestimationapp.utils.BitmapUtils

// Overlay to display the depth map over the `PreviewView`.
class DrawingOverlay(context: Context, attributeSet: AttributeSet) :
    SurfaceView(context, attributeSet), SurfaceHolder.Callback {

    // This variable is assigned in FrameAnalyser.kt
    var depthMaskBitmap: Bitmap? = null

    // These variables are assigned in MainActivity.kt
    var isFrontCameraOn = true
    var isShowingDepthMap = false


    override fun surfaceCreated(holder: SurfaceHolder) {
    }


    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }


    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }


    // Will be called when `drawingOverlay.invalidate()` is called in FrameAnalyser.kt.
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (depthMaskBitmap != null && isShowingDepthMap) {
            // If the front camera is on, then flip the Bitmap vertically ( or produce a mirror image of it ).
            canvas.drawBitmap(
                if (isFrontCameraOn) {
                    BitmapUtils.flipBitmap(depthMaskBitmap!!)
                } else {
                    depthMaskBitmap!!
                },
                0f, 0f, null
            )
        }
    }
}