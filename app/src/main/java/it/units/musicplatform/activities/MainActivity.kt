package it.units.musicplatform.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import it.units.musicplatform.R
import it.units.musicplatform.databinding.ActivityMainBinding
import it.units.musicplatform.fragments.HomeFragment
import it.units.musicplatform.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(binding.fragmentContainer.id, HomeFragment::class.java, bundleOf("user_id" to userId))
        }
//        intent.getStringExtra("user_id")
//        userId = intent.extras?.get("user_id") as String

//        if (savedInstanceState == null) {
//            userId = intent.getStringExtra(getString(R.string.user_id))
//            supportFragmentManager.commit {
//                setReorderingAllowed(true)
//                replace(binding.fragmentContainer.id, HomeFragment::class.java, bundleOf(getString(R.string.user_id) to userId))
//            }
//        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    fun addButtonListener(item: MenuItem) {
        startActivity(Intent(this, AddPostActivity::class.java))
    }

    fun homeNavigationBarListener(item: MenuItem) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(binding.fragmentContainer.id, HomeFragment::class.java, bundleOf(getString(R.string.user_id) to userId))
        }
        item.isChecked = true
    }

    fun searchNavigationBarListener(item: MenuItem) {
        item.isChecked = true
    }

    fun profileNavigationBarListener(item: MenuItem) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(binding.fragmentContainer.id, ProfileFragment::class.java, bundleOf(getString(R.string.user_id) to userId))
        }
        item.isChecked = true
    }
}