package it.units.musicplatform.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import it.units.musicplatform.R
import it.units.musicplatform.databinding.ActivityLoginBinding
import it.units.musicplatform.databinding.ActivityMainBinding
import it.units.musicplatform.fragments.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var userId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        var userId = intent.getStringExtra(getString(R.string.user_id))

        if (savedInstanceState == null) {
            userId = intent.getStringExtra(getString(R.string.user_id))
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<HomeFragment>(binding.fragmentContainer.id, args = bundleOf(getString(R.string.user_id) to userId))
            }
        }


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