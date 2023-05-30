package com.example.mystoryproject.ui.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.mystoryproject.R
import com.example.mystoryproject.databinding.FragmentRegisterBinding
import com.example.mystoryproject.ui.viewModel.AuthenticationViewModel

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val authentication: AuthenticationViewModel by activityViewModels()

    companion object {
        private const val TAG = "AuthenticationViewModel"
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.appLogo, View.TRANSLATION_Y, -40f, 20f).apply {
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            duration = 4000
        }.start()

        val welcome_text = ObjectAnimator.ofFloat(binding.welcome, View.ALPHA, 1f).setDuration(500)
        val name_field = ObjectAnimator.ofFloat(binding.registerName, View.ALPHA, 1f).setDuration(500)
        val email_field = ObjectAnimator.ofFloat(binding.registerEmail, View.ALPHA, 1f).setDuration(500)
        val password_field = ObjectAnimator.ofFloat(binding.registerPassword, View.ALPHA, 1f).setDuration(500)
        val btn_signup = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                welcome_text,
                name_field,
                email_field,
                password_field,
                btn_signup
            )
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (!isLoading) {
            binding.progressBar.visibility = View.GONE
        }
        else {
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View?
    {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener{
            val name_text = binding.registerName.text.toString().trim()
            val email_text = binding.registerEmail.text.toString().trim()
            val password_text = binding.registerPassword.text.toString().trim()

            //Get error message when email field and password field are null
            when {
                TextUtils.isEmpty(name_text) -> { binding.registerName.error = "This field cannot be empty" }
                TextUtils.isEmpty(email_text) -> { binding.registerEmail.error = "This field cannot be empty" }
                TextUtils.isEmpty(password_text) -> { binding.registerPassword.error = "This field cannot be empty" }
                else -> {
                    authentication.register(name_text, email_text, password_text)
                    authentication.register.observe(viewLifecycleOwner) { register ->
                        if (register.error) {
                            Toast.makeText(this.context, "Failed to register your account", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(this.context, "Your account has been registered", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        authentication.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        authentication.register.observe(viewLifecycleOwner) { register ->
            if (!register.error) {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.authentication_container, LoginFragment(), LoginFragment::class.java.simpleName)
                    commit()
                }
            }
        }

        playAnimation()
    }
}