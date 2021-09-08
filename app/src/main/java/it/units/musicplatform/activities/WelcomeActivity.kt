package it.units.musicplatform.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.units.musicplatform.R
import it.units.musicplatform.databinding.ActivityWelcomeBinding
import it.units.musicplatform.fragments.LoginFragment

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.welcomefragmentContainer, LoginFragment()).commit()

    }

}