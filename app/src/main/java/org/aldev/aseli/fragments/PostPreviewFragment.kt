package org.aldev.aseli.fragments

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.aldev.aseli.R
import org.aldev.aseli.databinding.FragmentPostPreviewBinding
import org.aldev.aseli.session.SessionHandler
import org.aldev.aseli.viewmodels.PostPreviewFragmentViewModel
import org.aldev.aseli.views.HomeView
import java.io.File
import java.util.Locale


class PostPreviewFragment : Fragment() {
    private lateinit var avt: HomeView
    private lateinit var binding: FragmentPostPreviewBinding
    private lateinit var viewModel: PostPreviewFragmentViewModel
    private var image: File? = null
    var refreshPosts: (() -> Unit)? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        avt = requireActivity() as HomeView
        binding = FragmentPostPreviewBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[PostPreviewFragmentViewModel::class.java]

        viewModel.setClient(SessionHandler(avt).getSessionKey())

        binding.btnSend.setOnClickListener { onUploadPost() }
        binding.postPreviewImage.setOnClickListener { getImageResult.launch("image/*") }

        viewModel.result.observe(avt){ onUploaded(it) }
        return binding.root
    }
    private fun onUploaded(result: Boolean?){
        resetAllInputs()
        if(result != true) return
        refreshPosts!!()
    }
    private fun onUploadPost(){
        if(!validateInputs()) return
        disableAllInputs()
        viewModel.postImage(image!!, binding.postPreviewTitle.text.toString(), binding.postPreviewDescription.text.toString(), getMimeType(image!!.toUri())!!)
    }
    private fun resetAllInputs(){
        binding.postPreviewImage.setImageResource(R.drawable.icon_image)
        binding.postPreviewTitle.text.clear()
        binding.postPreviewDescription.text.clear()

        binding.btnSend.isEnabled = true
        binding.postPreviewTitle.isEnabled = true
        binding.postPreviewDescription.isEnabled = true
    }
    private fun disableAllInputs(){
        binding.btnSend.isEnabled = false
        binding.postPreviewTitle.isEnabled = false
        binding.postPreviewDescription.isEnabled = false
    }
    private val getImageResult = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if(it == null) return@registerForActivityResult

        val inputStream = avt.contentResolver.openInputStream(it)
        image = File.createTempFile("temp_post_image", ".jpg", avt.cacheDir)
        inputStream.use { input ->
            image!!.outputStream().use { output -> input!!.copyTo(output) }
        }

        setImage()
    }
    private fun getMimeType(uri: Uri): String? {
        val mimeType: String? = if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            val cr: ContentResolver = avt.contentResolver
            cr.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.lowercase(Locale.getDefault())
            )
        }
        return mimeType
    }
    private fun setImage(){
        val bitmap = BitmapFactory.decodeFile(image!!.path)
        binding.postPreviewImage.setImageBitmap(bitmap)
    }
    private fun validateInputs(): Boolean {
        val failedMsg = when(true){
            (image == null) -> R.string.post_preview_image_required
            binding.postPreviewTitle.text.isEmpty() -> R.string.post_preview_title_required
            binding.postPreviewDescription.text.isEmpty() -> R.string.post_preview_description_required
            else -> 0
        }

        binding.postPreviewAlert.visibility = if(failedMsg != 0) View.VISIBLE else View.GONE
        if(failedMsg != 0) binding.postPreviewAlert.text = getString(failedMsg)
        return failedMsg == 0
    }
}