package org.aldev.aseli.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import org.aldev.aseli.R
import org.aldev.aseli.databinding.ActivityLoginBinding
import org.aldev.aseli.viewmodels.LoginViewModel

class LoginView : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setBindable()
        setOnLoginResult()
    }
    private fun setBindable(){
        binding.loginUsername.doOnTextChanged { _, _, _, _ -> viewModel.username = binding.loginUsername.text.toString() }
        binding.loginPassword.doOnTextChanged { _, _, _, _ -> viewModel.password = binding.loginPassword.text.toString() }
        binding.btnLogin.setOnClickListener {
            binding.btnLogin.isClickable = false
            viewModel.login()
        }
    }
    private fun setOnLoginResult(){
        viewModel.loginFailed.observe(this) {
            if(!it) return@observe

            binding.btnLogin.isClickable = true
            binding.loginAlert.visibility = View.VISIBLE

            val failedId = when(viewModel.failedType){
                "empty username" -> R.string.failed_empty_username
                "empty password" -> R.string.failed_empty_password
                else -> R.string.login_failed
            }
            binding.loginAlert.text = getString(failedId)
        }
    }
}