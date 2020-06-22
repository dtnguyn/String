package com.nguyen.string.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nguyen.string.R
import com.nguyen.string.di.Injection
import com.nguyen.string.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_register.view.*

class RegisterFragment : Fragment() {


    private lateinit var mainViewModel : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_register, container, false)

        mainViewModel = mainViewModel()

        mainViewModel.registerStatus.observe(this, Observer { status ->
            view.loading_icon.visibility = View.GONE
            if(status) {
                view.error_bar.visibility = View.GONE
                findNavController().navigate(R.id.action_register_to_verification)
            }
            else{
                view.error_bar.visibility = View.VISIBLE
            }
        })

        mainViewModel.registerMessage.observe(this, Observer { message ->
            if(message != "") view.error_bar.text = message
        })

        mainViewModel.registerData.observe(this, Observer { data ->
            Log.d("Register", "Get $data")
        })


        view.sign_up_button_register.setOnClickListener {
            val email = view.email_edit_text_login.text.toString()
            val name = view.name_edit_text.text.toString()
            val dob = view.dob_edit_text.text.toString()
            val username = view.username_edit_text.text.toString()
            val password = view.password_edit_text.text.toString()
            val confirmPassword = view.confirm_password_edit_text.text.toString()
            view.loading_icon.visibility = View.VISIBLE
            mainViewModel.registerUser(name, username, dob, email, password, confirmPassword)
        }

        view.terms_of_service.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_terms)
        }

        view.back_button_register.setOnClickListener {
            findNavController().popBackStack()
        }


        return view
    }


    private fun mainViewModel(): MainViewModel {
        val factory = Injection.provideMainViewModelFactory()
        return ViewModelProvider(this, factory)[MainViewModel::class.java]
    }
}