package org.aldev.aseli.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import org.aldev.aseli.R
import org.aldev.aseli.databinding.ActivityLoginBinding
import org.aldev.aseli.session.SessionHandler
import org.aldev.aseli.viewmodels.LoginViewModel

class LoginView : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionHandler: SessionHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionHandler = SessionHandler(this)
        if (sessionHandler.checkSession()) moveToHome()

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
            if(it == false) return@observe handleSuccess()

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
    private fun handleSuccess(){
        sessionHandler.setUserSession(viewModel.authKey)
        moveToHome()
    }
    private fun moveToRegister(){
        startActivity(Intent(this, RegisterView::class.java))
        finish()
    }
    private fun moveToHome(){
        startActivity(Intent(this, HomeView::class.java))
        finish()
    }
}