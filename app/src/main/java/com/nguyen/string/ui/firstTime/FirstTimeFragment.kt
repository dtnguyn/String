package com.nguyen.string.ui.firstTime

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.string.R
import com.nguyen.string.data.Interest
import com.nguyen.string.databinding.FragmentFirstTimeBinding
import com.nguyen.string.di.Injection
import com.nguyen.string.ui.firstTime.adapter.FirstTimeAdapter
import com.nguyen.string.viewmodel.FirstTimeViewModel

class FirstTimeFragment : Fragment() {

    companion object {

        private val selectedInterestList: ArrayList<Interest> = ArrayList()

        private var PAGE: String = "INTEREST"

        fun getPage() : String{
            return PAGE
        }

        fun addInterest(interest: Interest){
            selectedInterestList.add(interest)
        }

        fun removeInterest(id: Int){
            selectedInterestList.filter {
                it.id != id
            }
        }
    }


    private lateinit var firstTimeViewModel: FirstTimeViewModel
    private lateinit var recyclerView: RecyclerView

    private var interestAdapter: FirstTimeAdapter? = null
    private var followAdapter: FirstTimeAdapter? = null

    private var remain = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentFirstTimeBinding.inflate(inflater, container, false)

        firstTimeViewModel = firstTimeViewModel()

        recyclerView = binding.interestRecyclerview
        initInterestView(binding)

        firstTimeViewModel.getInterestList()
        firstTimeViewModel.getUserList()


        firstTimeViewModel.interestList.observe(viewLifecycleOwner, Observer {
            interestAdapter = FirstTimeAdapter(it, fun(counter: Int){
                remain = 3 - counter
                if(remain <= 0) {
                    binding.actionBar.nextButtonText = "Next"

                    binding.actionBar.nextButton.setBackgroundResource(R.drawable.sign_up_button)
                    binding.actionBar.nextButton.isEnabled = true
                } else {
                    binding.actionBar.nextButtonText = "$remain more"
                    binding.actionBar.nextButton.setBackgroundResource(R.drawable.button_disabled)
                    binding.actionBar.nextButton.isEnabled = false
                }
            })

            recyclerView.adapter = interestAdapter
        })

        firstTimeViewModel.userList.observe(viewLifecycleOwner, Observer {
            followAdapter = FirstTimeAdapter(it, {}, fun(userId: String){
                firstTimeViewModel.followUser(userId)
            })
            recyclerView.addOnScrollListener(ScrollListener(fun(recyclerView: RecyclerView) {
                if (!recyclerView.canScrollVertically(1)) {
                    firstTimeViewModel.getMoreUserList()
                }
            }))

        })

        binding.actionBar.nextButton.setOnClickListener {
            when(getPage()){
                "INTEREST" -> initFollowView(binding)
                "FOLLOW" -> {
                    Log.d("permission", "here")
                    firstTimeViewModel.submitSelectedInterestList(selectedInterestList)
                    findNavController().navigate(R.id.action_first_time_fragment_to_permission_fragment)
                }
            }
        }

        return binding.root
    }






    private fun initInterestView(binding: FragmentFirstTimeBinding){
        PAGE = "INTEREST"
        binding.actionBar.apply {
            actionBarTitle = "Select interest"
            backVisibility = false
            nextVisibility = true
            if(remain > 0) {
                nextButtonText = "$remain more"
                nextButton.isEnabled = false
            } else {
                nextButtonText = "Next"
                nextButton.isEnabled = true
            }
        }
        binding.fragmentTitle = getString(R.string.interest_guide)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        if(interestAdapter != null) recyclerView.adapter = interestAdapter

    }

    private fun initFollowView(binding: FragmentFirstTimeBinding){
        PAGE = "FOLLOW"
        binding.actionBar.apply {
            actionBarTitle = "Follow people"
            backVisibility = true
            nextVisibility = true
            nextButtonText = "Done"
            nextButton.isEnabled = true

            backButton.setOnClickListener {
                initInterestView(binding)
            }
        }
        binding.fragmentTitle = getString(R.string.follow_guide)
        recyclerView.layoutManager = LinearLayoutManager(context)
        if(followAdapter != null) recyclerView.adapter = followAdapter

    }



    private fun firstTimeViewModel(): FirstTimeViewModel {
        val factory = Injection.provideInterestViewModel()
        return ViewModelProvider(this, factory)[FirstTimeViewModel::class.java]
    }

    class ScrollListener(val loadMore : (recyclerView: RecyclerView) -> Unit) : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            loadMore(recyclerView)
        }
    }

}