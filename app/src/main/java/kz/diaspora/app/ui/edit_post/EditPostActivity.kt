package kz.diaspora.app.ui.edit_post

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.ActivityCreateAdvertBinding
import kz.diaspora.app.databinding.ActivityEditProfileBinding

@AndroidEntryPoint
class EditPostActivity : AppCompatActivity() {

    private val TAG = this::class.java.simpleName
    private var _binding: ActivityEditProfileBinding? = null
    private val binding get() = _binding!!
//    val args: MyAdvertsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener(listener)
        val abc = AppBarConfiguration.Builder().build()
        NavigationUI.setupActionBarWithNavController(this, navController, abc)
    }

    private val listener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            binding.toolbar.setNavigationIcon(R.drawable.ic_back_arrow_black)
            when (destination.id) {
                R.id.item_my_adverts -> {
                    binding.toolbar.setNavigationIcon(R.drawable.ic_exit)
                }
                else -> {

                }
            }
        }

    override fun onSupportNavigateUp(): Boolean {
        if (!(findNavController(R.id.nav_host_fragment).navigateUp() || super.onSupportNavigateUp())) {
            onBackPressed()
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
