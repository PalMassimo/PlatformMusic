package it.units.musicplatform.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.R
import it.units.musicplatform.databinding.ActivityMainBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.viewmodels.UserViewModel
import it.units.musicplatform.viewmodels.factories.UserViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var userId: String
    private lateinit var navigationController: NavController
    private val addPostLauncher = registerAddPostLauncher()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra(getString(R.string.user_id))!!

        userViewModel = ViewModelProvider(this, UserViewModelFactory(userId)).get(UserViewModel::class.java)

        navigationController = findNavController(R.id.fragment).also { binding.bottomNavigationView.setupWithNavController(it) }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.up_navigation_menu, menu)

        menu?.let {
            it.findItem(R.id.addPostMenuItem).setOnMenuItemClickListener {
                val intent = Intent(this, AddPostActivity::class.java).putExtra("user_id", userId)
                addPostLauncher.launch(intent)
                return@setOnMenuItemClickListener true
            }
            it.findItem(R.id.logoutMenuItem).setOnMenuItemClickListener {
                FirebaseAuth.getInstance().signOut()
                finish()
                return@setOnMenuItemClickListener true
            }
            it.findItem(R.id.aboutMenuItem).setOnMenuItemClickListener {
                Toast.makeText(this, "About window not implemented yet", Toast.LENGTH_SHORT).show()
                return@setOnMenuItemClickListener true
            }
        }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu!!.findItem(R.id.searchMenuItem).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isIconifiedByDefault = false
        }

        return true
    }

    override fun onNewIntent(intent: Intent?) {

        super.onNewIntent(intent)

        if (intent?.action.equals(Intent.ACTION_SEARCH))
            intent!!.getStringExtra("query")?.let { navigationController.navigate(R.id.searchFragment, bundleOf("query" to it.trim())) }

    }

    private fun registerAddPostLauncher() = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            activityResult.data?.extras?.let {
                val post = it.get("post") as Post
                val localUriCover = it.getString("localUriCover")
                val localUriSong = it.getString("localUriSong")
                userViewModel.addPost(post, Uri.parse(localUriSong), if(localUriCover == null) null else Uri.parse(localUriCover))
            }
        } else if (activityResult.resultCode == RESULT_CANCELED) {
            activityResult.data?.extras?.getString("message")?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}