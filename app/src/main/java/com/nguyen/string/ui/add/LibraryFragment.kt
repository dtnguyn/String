package com.nguyen.string.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nguyen.string.databinding.FragmentCameraBinding
import com.nguyen.string.databinding.FragmentLibraryBinding

class LibraryFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLibraryBinding.inflate(inflater, container, false)

        return binding.root
    }
}