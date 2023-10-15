package org.aldev.aseli.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import org.aldev.aseli.adapter.PostsAdapter
import org.aldev.aseli.databinding.FragmentUserBinding
import org.aldev.aseli.misc.Client
import org.aldev.aseli.misc.ImageUpload
import org.aldev.aseli.session.SessionHandler
import org.aldev.aseli.viewmodels.UserFragmentViewModel
import org.aldev.aseli.views.HomeView
import java.io.File

class UserFragment : Fragment() {
    private lateinit var avt: HomeView
    private lateinit var binding: FragmentUserBinding
    private lateinit var viewModel: UserFragmentViewModel
    private lateinit var sessionHandler: SessionHandler
    private lateinit var postsAdapter: PostsAdapter
    private var image: File? = null
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
    @SuppressLint("NotifyDataSetChanged")
    private val getImageResult = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if(it == null) return@registerForActivityResult

        val inputStream = avt.contentResolver.openInputStream(it)
        image = File.createTempFile("temp_profile_image", ".jpg", avt.cacheDir)
        inputStream.use { input ->
            image!!.outputStream().use { output -> input!!.copyTo(output) }
        }

        viewModel.changeProfileImage(image!!, ImageUpload.getMimeType(avt, image!!.toUri())!!)
        postsAdapter.notifyDataSetChanged()
    }
    private fun setBindable(){
        binding.userUsername.text = sessionHandler.getUsername()

        viewModel.posts.observe(avt){
            if(it == null) return@observe
            postsAdapter = PostsAdapter(this@UserFragment.layoutInflater, it, viewModel)
            binding.userPostsAdapter.apply {
                adapter = postsAdapter
                layoutManager = LinearLayoutManager(avt)
            }

            binding.userPostsTotal.text = it.size.toString()
        }

        viewModel.followersTotal.observe(avt){ binding.userFollowersTotal.text = it.toString() }
        viewModel.followingTotal.observe(avt){ binding.userFollowingTotal.text = it.toString() }
        viewModel.profileImage.observe(avt){
            Glide.with(avt).load(if(it != null) "${Client.imagesUrl}/${it}" else Client.randomImageUrl).into(binding.userProfilImage)
        }

        binding.userProfilImage.setOnClickListener { getImageResult.launch("image/*") }
    }
}