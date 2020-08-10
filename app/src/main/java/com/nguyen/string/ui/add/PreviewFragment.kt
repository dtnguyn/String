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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.nguyen.string.R
import com.nguyen.string.databinding.FragmentCameraBinding
import com.nguyen.string.databinding.FragmentPreviewBinding
import com.nguyen.string.ui.add.adapter.PreviewAdapter
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PreviewFragment: Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPreviewBinding.inflate(inflater, container, false)

        val recyclerView = binding.previewRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val uriList = arguments?.getStringArrayList("uriList")
        uriList?.let {
            recyclerView.adapter = PreviewAdapter(it, requireContext())
        }

        return binding.root
    }





}
