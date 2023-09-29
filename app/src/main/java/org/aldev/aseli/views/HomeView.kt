package org.aldev.aseli.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.aldev.aseli.adapter.PostsAdapter
import org.aldev.aseli.databinding.ActivityHomeBinding
import org.aldev.aseli.viewmodels.HomeViewModel

class HomeView : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    private lateinit var viewModel : HomeViewModel
    private lateinit var postsAdapter: PostsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewModel.getPosts()
        setBindable()
    }
    private fun setBindable(){
        viewModel.posts.observe(this){
            postsAdapter = PostsAdapter(this@HomeView.layoutInflater, it!!)
            binding.root.apply {
                adapter = postsAdapter
                layoutManager = LinearLayoutManager(this@HomeView)
            }
        }
    }
}