package com.nguyen.string.ui.add

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.nguyen.string.R
import com.nguyen.string.databinding.FragmentCameraBinding
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class CameraFragment: Fragment() {

    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private var currentLens: Int = CameraSelector.LENS_FACING_BACK

    private var imageUris: ArrayList<String> = ArrayList()

    companion object {
        private const val TAG = "Camera Tag"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCameraBinding.inflate(inflater, container, false)

        binding.actionBarCamera.apply {
            closeVisibility = true
            nextVisibility = true
            nextButton.setBackgroundResource(R.drawable.back_button)
            nextButton.setTextColor(Color.parseColor("#8853E8"))
            nextButtonText = "Next"
            backVisibility = false
            actionBarTitle = "Camera"
        }

        binding.actionBarCamera.nextButton.setOnClickListener {
            if(imageUris.size != 0){
                val bundle = bundleOf("uriList" to imageUris)
                findNavController().navigate(R.id.preview_fragment, bundle)
            }
        }

        binding.actionBarVisibility = false

        // Request camera permissions
        if (cameraPermissionGranted()) {
            startCamera(currentLens)
        }

        // Setup the listener for take photo button
        binding.shutterButton.setOnClickListener { takePhoto(binding) }

        binding.switchCameraButton.setOnClickListener {
            when (currentLens){
                CameraSelector.LENS_FACING_BACK -> currentLens = CameraSelector.LENS_FACING_FRONT
                CameraSelector.LENS_FACING_FRONT -> currentLens = CameraSelector.LENS_FACING_BACK
            }
            startCamera(currentLens)
        }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()

        return binding.root
    }



    private fun startCamera(lens: Int) {
        Log.d("Camera Tag", "Starting the camera...")
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            preview = Preview.Builder()
                .build()

            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera
            val cameraSelector = CameraSelector.Builder().requireLensFacing(lens).build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageCapture)
                preview?.setSurfaceProvider(viewFinder.createSurfaceProvider())
            } catch(exc: Exception) {
                Log.d(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto(binding: FragmentCameraBinding) {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create timestamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Setup image capture listener which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    imageUris.add(savedUri.toString())
                    binding.actionBarVisibility = true
                    binding.imagePreviewVisibility = true
                    loadImage(savedUri, binding)
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            })
    }

    private fun cameraPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.string)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    private fun loadImage(savedUri: Uri, binding: FragmentCameraBinding){
        Glide.with(requireContext())
            .load(savedUri)
            .centerCrop()
            .into(binding.previewImage)
    }


}
