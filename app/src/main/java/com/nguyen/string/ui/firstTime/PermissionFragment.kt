package com.nguyen.string.ui.firstTime

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.nguyen.string.R
import com.nguyen.string.databinding.FragmentPermissionBinding
import com.nguyen.string.ui.main.LoggedActivity

class PermissionFragment : Fragment() {

    private var currentPermission = "NOTIFICATION"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentPermissionBinding.inflate(inflater, container, false)

        initNotificationPermission(binding)

        binding.acceptPermissionButton.setOnClickListener {
            when(currentPermission){
                "NOTIFICATION" -> requestPermission(Manifest.permission.ACCESS_NOTIFICATION_POLICY, fun(result: Boolean){
                    Log.d("Permission", "Request Permission result: $result")
                    initLocationPermission(binding)
                })
                "LOCATION" -> requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, fun(result: Boolean){
                    val intent = Intent(requireContext(), LoggedActivity::class.java)
                    startActivity(intent)
                })
            }
        }

        return binding.root
    }


    private fun initNotificationPermission(binding: FragmentPermissionBinding){
        currentPermission = "NOTIFICATION"
        binding.permissionsImage.setImageResource(R.drawable.notification_permission_image)
        binding.permissionTitle.setText(R.string.enable_notifications)
        binding.permissionDescription.setText(R.string.notification_permission_description)
    }

    private fun initLocationPermission(binding: FragmentPermissionBinding){
        currentPermission = "LOCATION"
        binding.permissionsImage.setImageResource(R.drawable.location_permission_image)
        binding.permissionTitle.setText(R.string.enable_location)
        binding.permissionDescription.setText(R.string.location_permission_description)
    }

    private fun requestPermission(permission: String, callback: (result: Boolean) -> Unit) {
        val request = ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

        if (!request) {
            val requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                    callback(isGranted)
                }
            requestPermissionLauncher.launch(permission)
        } else {
            callback(true)
        }
    }
}