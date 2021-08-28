package it.units.musicplatform.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import it.units.musicplatform.R
import it.units.musicplatform.databinding.ActivityMainBinding
import it.units.musicplatform.fragments.HomeFragment
import it.units.musicplatform.fragments.ProfileFragment

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
                replace(
                    binding.fragmentContainer.id,
                    HomeFragment::class.java,
                    bundleOf(getString(R.string.user_id) to userId)
                )
//                replace<HomeFragment>(binding.fragmentContainer.id, args = bundleOf(getString(R.string.user_id) to userId))
            }
        }


    }

    fun homeNavigationBarListener(item: MenuItem) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(
                binding.fragmentContainer.id,
                HomeFragment::class.java,
                bundleOf(getString(R.string.user_id) to userId)
            )
//            add<HomeFragment>(binding.fragmentContainer.id, args = bundleOf(getString(R.string.user_id) to userId))
        }
        item.isCheckable = true
    }

    fun searchNavigationBarListener(item: MenuItem) {
        item.isCheckable = true
    }

    fun profileNavigationBarListener(item: MenuItem) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(
                binding.fragmentContainer.id,
                ProfileFragment::class.java,
                bundleOf(getString(R.string.user_id) to userId)
            )
//            add<ProfileFragment>(binding.fragmentContainer.id, args = bundleOf(getString(R.string.user_id) to userId))
        }
        item.isCheckable = true
    }
}