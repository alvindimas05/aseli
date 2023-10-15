package org.aldev.aseli.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import org.aldev.aseli.databinding.ActivityHomeBinding
import org.aldev.aseli.fragments.PostPreviewFragment
import org.aldev.aseli.fragments.PostsFragment
import org.aldev.aseli.fragments.UserFragment

class HomeView : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
//    private lateinit var bottomBarBinding: BottomBarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPagerAdapter()
        setBottomBarButtons()
    }
    private fun setPagerAdapter(){
        val pagerAdapter = HomePagerAdapter(supportFragmentManager, lifecycle, binding.homePager)
        binding.homePager.adapter = pagerAdapter
    }
    private fun setBottomBarButtons(){
//        bottomBarBinding = BottomBarBinding.inflate(layoutInflater)
        binding.bottomBar.bottomBarHome.setOnClickListener { binding.homePager.currentItem = 0 }
        binding.bottomBar.bottomBarPlus.setOnClickListener { binding.homePager.currentItem = 1 }
        binding.bottomBar.bottomBarUser.setOnClickListener { binding.homePager.currentItem = 2 }
    }
    class HomePagerAdapter(manager: FragmentManager, lifecycle: Lifecycle, pager: ViewPager2) : FragmentStateAdapter(manager, lifecycle){
        private val fragments = ArrayList<Fragment>()
        init {
            val postsFragment = PostsFragment()
            val postPreviewFragment = PostPreviewFragment()
            val userFragment = UserFragment()

            fragments.add(postsFragment)
            fragments.add(postPreviewFragment)
            fragments.add(userFragment)

            postPreviewFragment.refreshPosts = {
                postsFragment.refreshPost()
                pager.currentItem = 0
            }
        }
        override fun getItemCount() = fragments.size
        override fun createFragment(i: Int) = fragments[i]
    }
}