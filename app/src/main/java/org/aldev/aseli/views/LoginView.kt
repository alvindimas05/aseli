package org.aldev.aseli.views

import android.content.Intent
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
        checkForSession()

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
        binding.btnToRegister.setOnClickListener { moveToRegister() }
    }
    private fun setOnLoginResult(){
        viewModel.loginFailed.observe(this) {
            if(!it) return@observe setUserSession()

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
    private fun moveToRegister(){
        startActivity(Intent(this, RegisterView::class.java))
        finish()
    }
    private fun setUserSession(){
        val pref = getSharedPreferences("user_data", MODE_PRIVATE).edit()
        pref.putString("auth_key", viewModel.authKey)
        pref.apply()

        moveToHome()
    }
    private fun checkForSession(){
        val pref = getSharedPreferences("user_data", MODE_PRIVATE)
        if (pref.contains("auth_key")) moveToHome()
    }
    private fun moveToHome(){
        startActivity(Intent(this, HomeView::class.java))
        finish()
    }
}