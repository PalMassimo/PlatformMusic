package it.units.musicplatform.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
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
import it.units.musicplatform.fragments.SearchFragment
import it.units.musicplatform.viewmodels.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var userId: String
    private val addPostLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK && it.data?.extras?.get("post") != null) {
            val post = it.data!!.extras!!.get("post") as Post
            userViewModel.addPost(post)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = FirebaseAuth.getInstance().currentUser!!.uid

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(binding.fragmentContainer.id, HomeFragment::class.java, bundleOf("user_id" to userId))
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.upper_bar_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu!!.findItem(R.id.searchMenuItem).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isIconifiedByDefault = false
        }

        return true
    }

    fun addButtonListener(item: MenuItem) = addPostLauncher.launch(Intent(this, AddPostActivity::class.java))


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent?.action.equals(Intent.ACTION_SEARCH)) {
            val subName = intent!!.getStringExtra("query")
            subName?.let { startSearch(it.trim()) }
        }
    }


    private fun startSearch(subName: String) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(binding.fragmentContainer.id, SearchFragment::class.java, bundleOf("query" to subName))
            binding.bottomNavigationView.menu.findItem(R.id.search).isChecked = true
        }
    }

    fun logoutButtonListener(item: MenuItem) {
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    fun aboutButtonListener(item: MenuItem) = Toast.makeText(this, "About window not implemented yet", Toast.LENGTH_SHORT).show()


    fun homeNavigationBarListener(item: MenuItem) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(binding.fragmentContainer.id, HomeFragment::class.java, bundleOf(getString(R.string.user_id) to userId))
        }
        item.isChecked = true
    }

    fun searchNavigationBarListener(item: MenuItem) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(binding.fragmentContainer.id, SearchFragment::class.java, bundleOf(getString(R.string.user_id) to userId))
        }
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