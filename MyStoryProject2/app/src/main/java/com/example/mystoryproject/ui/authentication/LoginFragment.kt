package com.example.mystoryproject.ui.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryproject.R
import com.example.mystoryproject.databinding.FragmentLoginBinding
import com.example.mystoryproject.ui.home.MainActivity
import com.example.mystoryproject.ui.viewModel.AuthenticationViewModel
import com.example.mystoryproject.ui.viewModel.SettingsViewModel
import com.example.mystoryproject.ui.viewModel.SettingsViewModelFactory
import com.example.mystoryproject.utils.SettingsPreferences

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val authentication: AuthenticationViewModel by activityViewModels()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    companion object {
        private const val TAG = "AuthenticationViewModel"
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.appLogo, View.TRANSLATION_Y, -40f, 20f).apply {
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            duration = 3000
        }.start()

        val welcome_text = ObjectAnimator.ofFloat(binding.welcome, View.ALPHA, 1f).setDuration(400)
        val email_field = ObjectAnimator.ofFloat(binding.loginEmail, View.ALPHA, 1f).setDuration(400)
        val password_field = ObjectAnimator.ofFloat(binding.loginPassword, View.ALPHA, 1f).setDuration(400)
        val btn_login = ObjectAnimator.ofFloat(binding.btnSignin, View.ALPHA, 1f).setDuration(400)
        val btn_register = ObjectAnimator.ofFloat(binding.register, View.ALPHA, 1f).setDuration(400)
        AnimatorSet().apply {
            playSequentially(
                welcome_text,
                email_field,
                password_field,
                btn_login,
                btn_register
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
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val previous = SettingsPreferences.getInstance((activity as AuthenticationActivity).dataStore)
        val settingsVm = ViewModelProvider(this, SettingsViewModelFactory(previous)).get(SettingsViewModel::class.java)
        authentication.isLoading.observe(viewLifecycleOwner) { showLoading(it) }

        authentication.login.observe(viewLifecycleOwner) { login -> settingsVm.setUserPreferences(
                login.loginResult.token,
                login.loginResult.userId,
                login.loginResult.name,
                authentication.eMail.value ?: "None"
            )
        }

        settingsVm.getToken().observe(viewLifecycleOwner) { token ->
            if (token != "Not Set") {
                val mIntent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(mIntent)
            }
        }

        binding.btnSignin.setOnClickListener { //login
            val email_text = binding.loginEmail.text.toString().trim()
            val password_text = binding.loginPassword.text.toString().trim()

            //Get error message when email field and password field are null
            when {
                TextUtils.isEmpty(email_text) -> { binding.loginEmail.error = "Field must be filled" }
                TextUtils.isEmpty(password_text) -> { binding.loginPassword.error = "Field must be filled" }
                binding.loginPassword.error?.length ?: 0 > 0 -> binding.loginPassword.requestFocus()
                else -> {
                    authentication.login(email_text, password_text)
                    authentication.login.observe(viewLifecycleOwner) { login ->
                        if (login.error) {
                            Toast.makeText(this.context, "Failed to login", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(this.context, "Login succesful", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.registerText.setOnClickListener {
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.authentication_container, RegisterFragment(), RegisterFragment::class.java.simpleName)
            }
        }

        playAnimation()
    }
}