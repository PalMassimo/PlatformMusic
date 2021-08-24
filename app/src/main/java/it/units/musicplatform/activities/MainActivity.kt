package it.units.musicplatform.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import it.units.musicplatform.R
import it.units.musicplatform.databinding.ActivityLoginBinding
import it.units.musicplatform.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var userId = intent.getStringExtra(getString(R.string.user_id))


    }

    fun homeNavigationBarListener(item: MenuItem) {
        item.setCheckable(true)
    }

    fun searchNavigationBarListener(item: MenuItem) {
        item.setCheckable(true)
    }

    fun profileNavigationBarListener(item: MenuItem) {
        item.setCheckable(true)
    }
}