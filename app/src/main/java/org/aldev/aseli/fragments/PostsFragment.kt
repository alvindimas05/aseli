package org.aldev.aseli.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.aldev.aseli.adapter.PostsAdapter
import org.aldev.aseli.databinding.FragmentPostsBinding
import org.aldev.aseli.session.SessionHandler
import org.aldev.aseli.viewmodels.PostsFragmentViewModel
import org.aldev.aseli.views.HomeView

class PostsFragment : Fragment() {
    private lateinit var viewModel: PostsFragmentViewModel
    private lateinit var binding: FragmentPostsBinding
    private lateinit var avt: HomeView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        avt = requireActivity() as HomeView
        binding = FragmentPostsBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this)[PostsFragmentViewModel::class.java]
        viewModel.setClient(SessionHandler(avt).getSessionKey())

        setBindable()
        viewModel.getPosts()

        return binding.root
    }
    fun refreshPost(){
        viewModel.getPosts()
    }
    private fun setBindable(){
        viewModel.posts.observe(avt){
            val postsAdapter = PostsAdapter(avt, it!!, viewModel)
            binding.postsList.apply {
                adapter = postsAdapter
                layoutManager = LinearLayoutManager(avt)
            }
        }
    }
}