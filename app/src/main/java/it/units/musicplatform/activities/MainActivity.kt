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
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
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
    private lateinit var navigationController: NavController
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

        userId = intent.getStringExtra(getString(R.string.user_id))!!

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        navigationController = findNavController(R.id.fragment).also { binding.bottomNavigationView.setupWithNavController(it) }

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


    private fun startSearch(subName: String) = navigationController.navigate(R.id.searchFragment, bundleOf("query" to subName))


    fun logoutButtonListener(item: MenuItem) {
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    fun aboutButtonListener(item: MenuItem) = Toast.makeText(this, "About window not implemented yet", Toast.LENGTH_SHORT).show()


}