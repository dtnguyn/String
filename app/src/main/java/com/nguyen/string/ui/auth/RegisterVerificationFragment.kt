package com.nguyen.string.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nguyen.string.R
import com.nguyen.string.di.Injection
import com.nguyen.string.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_register_verification.view.*

class RegisterVerificationFragment : Fragment() {


    private lateinit var mainViewModel : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register_verification, container, false)

        mainViewModel = mainViewModel()

        view.email_verification.text = mainViewModel.getVerifyEmail()
        view.open_mail_app_button.setOnClickListener {
            val mailIntent = Intent(Intent.ACTION_MAIN)
            mailIntent.addCategory(Intent.CATEGORY_APP_EMAIL)
            startActivity(mailIntent)
        }

        return view
    }


    private fun mainViewModel(): MainViewModel {
        val factory = Injection.provideMainViewModelFactory()
        return ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

}