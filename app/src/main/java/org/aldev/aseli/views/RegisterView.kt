package org.aldev.aseli.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import org.aldev.aseli.R
import org.aldev.aseli.databinding.ActivityRegisterBinding
import org.aldev.aseli.session.SessionHandler
import org.aldev.aseli.viewmodels.RegisterViewModel

class RegisterView : AppCompatActivity() {
    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sessionHandler: SessionHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionHandler = SessionHandler(this)
        if (sessionHandler.checkSession()) moveToHome()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        setBindable()
        setOnRegisterResult()
    }
    private fun setBindable() {
        binding.registerUsername.doOnTextChanged { _, _, _, _ -> viewModel.username = binding.registerUsername.text.toString()}
        binding.registerPassword.doOnTextChanged { _, _, _, _ -> viewModel.password = binding.registerPassword.text.toString()}
        binding.registerConfirm.doOnTextChanged { _, _, _, _ -> viewModel.verification = binding.registerConfirm.text.toString()}
        binding.btnRegister.setOnClickListener {
            binding.btnRegister.isClickable = false
            viewModel.register()
        }
        binding.btnToLogin.setOnClickListener { moveToLogin() }
    }
    private fun moveToLogin(){
        startActivity(Intent(this, RegisterView::class.java))
        finish()
    }
    private fun moveToHome(){
        startActivity(Intent(this, HomeView::class.java))
        finish()
    }
    private fun handleSuccess(){
        sessionHandler.setUserSession(viewModel.authKey, viewModel.username)
        moveToHome()
    }
    private fun setOnRegisterResult(){
        viewModel.registerFailed.observe(this) {
            if(it == false) return@observe handleSuccess()

            binding.btnRegister.isClickable = true
            binding.registerAlert.visibility = View.VISIBLE

            val failedId = when(viewModel.failedType){
                "empty username" -> R.string.failed_empty_username
                "empty password" -> R.string.failed_empty_password
                "empty verification" -> R.string.failed_empty_verification
                "verification not match" -> R.string.failed_verification_not_match
                "username used" -> R.string.failed_username_used
                else -> 0
            }
            binding.registerAlert.text = getString(failedId)
        }
    }
}