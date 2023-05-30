package com.example.mystoryproject.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mystoryproject.R
import com.example.mystoryproject.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.authentication_container, LoginFragment()).commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}