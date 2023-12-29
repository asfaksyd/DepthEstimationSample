package com.akeso.depthestimationapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.akeso.depthestimationapp.utils.Logger
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutionException


class MainActivity : AppCompatActivity() {
    private lateinit var previewView : PreviewView
    private var preview: Preview? = null

    private lateinit var cameraProviderListenableFuture : ListenableFuture<ProcessCameraProvider>
    private var isFrontCameraOn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_main)

        previewView = findViewById( R.id.camera_preview_view )

        val flipCameraFAB = findViewById<FloatingActionButton>( R.id.flip_camera_fab )
        flipCameraFAB.setOnClickListener {
            when( isFrontCameraOn ) {
                true -> setupCameraProvider( CameraSelector.LENS_FACING_BACK )
                false -> setupCameraProvider( CameraSelector.LENS_FACING_FRONT )
            }
            isFrontCameraOn = !isFrontCameraOn
        }

        // Request the CAMERA permission as we'll require it for displaying the camera preview.
        if (ActivityCompat.checkSelfPermission( this , Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED ) {
            requestCameraPermission()
        }
        else {
            setupCameraProvider( CameraSelector.LENS_FACING_FRONT )
        }
    }

    private fun requestCameraPermission() {
        Logger.logInfo( "Requesting camera permission" )
        requestCameraPermissionLauncher.launch( Manifest.permission.CAMERA )
    }

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission() ) {
            isGranted : Boolean ->
        if ( isGranted ) {
            Logger.logInfo( "Camera permission granted by user." )
            setupCameraProvider( CameraSelector.LENS_FACING_FRONT )
        }
        else {
            Logger.logInfo( "Camera permission denied by user." )
            val alertDialog = AlertDialog.Builder( this ).apply {
                setTitle( "Permissions" )
                setMessage( "The app requires the camera permission to function." )
                setPositiveButton( "GRANT") { dialog, _ ->
                    dialog.dismiss()
                    requestCameraPermission()
                }
                setNegativeButton( "CLOSE" ) { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }
                setCancelable( false )
                create()
            }
            alertDialog.show()
        }
    }


    // Setup the PreviewView for live camera feed.
    // See the docs -> https://developer.android.com/training/camerax/preview
    // and https://developer.android.com/training/camerax/analyze
    private fun setupCameraProvider( cameraFacing : Int ) {
        cameraProviderListenableFuture = ProcessCameraProvider.getInstance( this )
        cameraProviderListenableFuture.addListener({
            try {
                val cameraProvider: ProcessCameraProvider = cameraProviderListenableFuture.get()
                bindPreview( cameraProvider , cameraFacing )
            }
            catch (e: ExecutionException) {
                Logger.logError( e.message!! )
            }
            catch (e: InterruptedException) {
                Logger.logError( e.message!! )
            }
        }, ContextCompat.getMainExecutor( this ))
    }

    private fun bindPreview( cameraProvider: ProcessCameraProvider , lensFacing : Int ) {
        // Unbind any previous use-cases as we'll attach them once again.
        if ( preview != null ) {
            cameraProvider.unbind( preview )
        }

        Logger.logInfo( "Setting camera with ${
            if ( lensFacing == CameraSelector.LENS_FACING_FRONT ) { "front" }
            else { "rear" }
        } lens facing" )

        preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing( lensFacing )
            .build()
        preview!!.setSurfaceProvider(previewView.surfaceProvider)

        // Set the resolution which is the closest to the screen size.
        val displayMetrics = resources.displayMetrics
        val screenSize = Size( displayMetrics.widthPixels, displayMetrics.heightPixels )
        Logger.logInfo( "Screen size is $screenSize" )

        cameraProvider.bindToLifecycle(
            (this as LifecycleOwner),
            cameraSelector,
            preview
        )
    }

}