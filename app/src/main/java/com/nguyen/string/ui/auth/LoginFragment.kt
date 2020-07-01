package com.nguyen.string.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nguyen.string.R
import com.nguyen.string.di.Injection
import com.nguyen.string.ui.main.LoggedActivity
import com.nguyen.string.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_login.view.*


class LoginFragment : Fragment() {

    private lateinit var mainViewModel : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login, container, false)


        mainViewModel = mainViewModel()

        mainViewModel.loginStatus.observe(viewLifecycleOwner, Observer { status ->
            view.loading_icon.visibility = View.GONE
            Log.d("Login", "Login Status: $status")
            if(status) {
                val intent = Intent(context, LoggedActivity::class.java)
                startActivity(intent)
            } else {
                view.error_bar.visibility = View.VISIBLE
            }
        })

        mainViewModel.loginMessage.observe(viewLifecycleOwner, Observer { message ->
            if(message != "") {
                view.error_bar.text = message
            }
        })

        view.login_button.setOnClickListener {
            view.loading_icon.visibility = View.VISIBLE
            view.error_bar.visibility = View.GONE
            val email = view.email_edit_text_login.text.toString()
            val password  = view.password_edit_text_login.text.toString()
            mainViewModel.loginUser(email, password)
        }

        view.back_button_login.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    private fun mainViewModel(): MainViewModel {
        val factory = Injection.provideMainViewModelFactory()
        return ViewModelProvider(this, factory)[MainViewModel::class.java]
    }
}