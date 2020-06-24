package com.nguyen.string.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.nguyen.string.R
import com.nguyen.string.di.Injection
import com.nguyen.string.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_start.view.*


class StartFragment : Fragment() {

    private lateinit var mainViewModel : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_start, container, false)

        mainViewModel = mainViewModel()


        view.sign_up_with_email_button.setOnClickListener {
            findNavController().navigate(R.id.action_start_to_register)
        }

        view.move_to_log_in_button.setOnClickListener {
            findNavController().navigate(R.id.action_start_to_login)
        }


        view.sign_up_with_facebook_button.setOnClickListener {
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut()
            }
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"))
        }
        val callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                Log.d("Register fb", "Success")
                mainViewModel.registerWithFb(result?.accessToken?.token!!)
            }

            override fun onCancel() {
                Log.d("Register fb", "Cancel")
            }

            override fun onError(error: FacebookException?) {
                Log.d("Register fb", "Error")
            }

        })

        return view
    }


    private fun mainViewModel(): MainViewModel {
        val factory = Injection.provideMainViewModelFactory()
        return ViewModelProvider(this, factory)[MainViewModel::class.java]
    }
}