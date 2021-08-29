package it.units.musicplatform.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.R
import it.units.musicplatform.databinding.ActivityMainBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.fragments.HomeFragment
import it.units.musicplatform.fragments.ProfileFragment
import it.units.musicplatform.viewmodels.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private val addPostLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK && it.data?.extras?.get("post") != null) {
                val post = it.data!!.extras!!.get("post") as Post
//                val localUriSong = it.data!!.extras!!.get("localUriSong") as Uri
//                val localCoverSong = it.data!!.extras!!.get("localUriCover") as Uri
                userViewModel.addPost(post)
        }
    }

    private val userId = FirebaseAuth.getInstance().currentUser!!.uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(binding.fragmentContainer.id, HomeFragment::class.java, bundleOf("user_id" to userId))
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    fun addButtonListener(item: MenuItem) = addPostLauncher.launch(Intent(this, AddPostActivity::class.java))


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