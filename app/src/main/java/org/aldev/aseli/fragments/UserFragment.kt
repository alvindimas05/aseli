package org.aldev.aseli.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import org.aldev.aseli.adapter.PostsAdapter
import org.aldev.aseli.databinding.FragmentUserBinding
import org.aldev.aseli.session.SessionHandler
import org.aldev.aseli.viewmodels.UserFragmentViewModel
import org.aldev.aseli.views.HomeView

class UserFragment : Fragment() {
    private lateinit var avt: HomeView
    private lateinit var binding: FragmentUserBinding
    private lateinit var viewModel: UserFragmentViewModel
    private lateinit var sessionHandler: SessionHandler
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        avt = requireActivity() as HomeView
        binding = FragmentUserBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(avt)[UserFragmentViewModel::class.java]
        sessionHandler = SessionHandler(avt)

        viewModel.setClient(sessionHandler.getSessionKey())
        setBindable()

        viewModel.getPosts(sessionHandler.getUsername())
        viewModel.getUserData()

        return binding.root
    }
    private fun setBindable(){
        binding.userUsername.text = sessionHandler.getUsername()

        viewModel.posts.observe(avt){
            if(it == null) return@observe
            val postsAdapter = PostsAdapter(this@UserFragment.layoutInflater, it, viewModel)
            binding.userPostsAdapter.apply {
                adapter = postsAdapter
                layoutManager = LinearLayoutManager(avt)
            }

            binding.userPostsTotal.text = it.size.toString()
        }

        viewModel.followersTotal.observe(avt){ binding.userFollowersTotal.text = it.toString() }
        viewModel.followingTotal.observe(avt){ binding.userFollowingTotal.text = it.toString() }
        viewModel.profileImage.observe(avt){ Glide.with(avt).load(it).into(binding.userProfilImage) }
    }
}