package com.nguyen.string.ui.firstTime.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.string.R
import com.nguyen.string.data.Interest
import com.nguyen.string.data.User
import com.nguyen.string.databinding.FollowUserItemBinding
import com.nguyen.string.databinding.InterestItemBinding
import com.nguyen.string.ui.firstTime.FirstTimeFragment.Companion.addInterest
import com.nguyen.string.ui.firstTime.FirstTimeFragment.Companion.getPage
import com.nguyen.string.ui.firstTime.FirstTimeFragment.Companion.removeInterest

class FirstTimeAdapter(
    private val mInterestsOrUsers: List<Any>,
    private val updateNextButton : (counter: Int) -> Unit = {},
    private val followUser: (userId: String) -> Unit = {}) : RecyclerView.Adapter<FirstTimeAdapter.BaseViewHolder>() {

    companion object {
        private var counter = 0
    }

    private val hashMap : HashMap<String, Boolean> = HashMap()

    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        abstract fun bind(interestOrUser: Any, map : HashMap<String, Boolean>, update: (counter: Int) -> Unit, follow: (userId: String) -> Unit)
    }

    class InterestViewHolder(private val binding: InterestItemBinding) : BaseViewHolder(binding.root){

        private val checkbox = binding.interestCheckbox

        override fun bind(interestOrUser: Any, map : HashMap<String, Boolean>, update: (counter: Int) -> Unit, follow: (userId: String) -> Unit) {

            var clicking = true
            interestOrUser as Interest

            binding.apply {
                interest = interestOrUser
                executePendingBindings()
            }

            checkbox.setOnClickListener {
                clicking = true
            }

            checkbox.setOnCheckedChangeListener { _, checked ->
                if (!clicking) {
                    clicking = true
                    return@setOnCheckedChangeListener
                }
                binding.interest?.photo?.id?.let { map[it] = checked }
                if(checked){
                    counter++
                    addInterest(interestOrUser)
                } else {
                    counter--
                    removeInterest(interestOrUser.id!!)
                }

                update.invoke(counter)
            }

            binding.interest?.photo?.id?.let{
                if(map[it] != null){
                    if(checkbox.isChecked != map[it]){
                        clicking = false
                        checkbox.toggle()
                    }
                } else {
                    if(checkbox.isChecked) {
                        clicking = false
                        checkbox.toggle()
                    }
                }
            }


        }
    }

    class FollowViewHolder(private val binding: FollowUserItemBinding) : BaseViewHolder(binding.root){
        @SuppressLint("ResourceAsColor")
        override fun bind(
            interestOrUser: Any,
            map: HashMap<String, Boolean>,
            update: (counter: Int) -> Unit,
            follow: (userId: String) -> Unit
        ) {

            interestOrUser as User

            var userId: String
            interestOrUser.let {
                userId = it.toString()
                binding.user = it
            }

            if(map[userId] != null){
                updateFollowButton(map[userId]!!)
            } else {
                if(interestOrUser.checkFollow != null){
                    updateFollowButton(interestOrUser.checkFollow!!)
                }
            }


            if(interestOrUser.photos == null || interestOrUser.photos?.size == 0){
                binding.imageContainer.visibility = View.GONE
            }

            binding.followButton.setOnClickListener {
                if(map[userId] != null){
                    map[userId] = !map[userId]!!
                    updateFollowButton(map[userId]!!, userId, follow)
                } else {
                    map[userId] = !interestOrUser.checkFollow!!
                    updateFollowButton(map[userId]!!, userId, follow)
                }
            }

        }

        private fun updateFollowButton(value: Boolean, userId: String = "", follow: (userId: String) -> Unit = {}){
            if(value){
                binding.followButton.setBackgroundResource(R.drawable.outlined_pur_button)
                binding.followButton.setTextColor(Color.parseColor("#8853E8"))
                binding.followButton.setText(R.string.unfollow)
                follow.invoke(userId)
            } else {
                binding.followButton.setBackgroundResource(R.drawable.sign_up_button)
                binding.followButton.setTextColor(Color.parseColor("#d9d9d9"))
                binding.followButton.setText(R.string.follow)
                follow.invoke(userId)
            }
        }


    }

    override fun getItemViewType(position: Int): Int {
        if(getPage() == "FOLLOW") return R.layout.follow_user_item
        return R.layout.interest_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        var holder : BaseViewHolder? = null

        when(viewType){
            R.layout.interest_item -> holder =
                InterestViewHolder(
                    InterestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            R.layout.follow_user_item -> holder =
                FollowViewHolder(
                    FollowUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
        }
        return holder!!
    }

    override fun getItemCount(): Int {
        return mInterestsOrUsers.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when(getPage()){
            "INTEREST" -> holder.bind(mInterestsOrUsers[position] as Interest, hashMap, updateNextButton, {})
            "FOLLOW" -> holder.bind(mInterestsOrUsers[position] as User, hashMap, {}, followUser)
        }

    }

    fun addUsersFollow(users: List<User>){
        if(mInterestsOrUsers[0] !is User) return
        mInterestsOrUsers as ArrayList<User>
        users.forEach {
            mInterestsOrUsers.add(it)
            notifyItemInserted(mInterestsOrUsers.size - 1)
        }
    }



}