package kz.diaspora.app.ui.edit_advert

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.databinding.ActivityCreateAdvertBinding

@AndroidEntryPoint
class CreateAdvertActivity : AppCompatActivity() {

    private val TAG = this::class.java.simpleName
    private var _binding: ActivityCreateAdvertBinding? = null
    private val binding get() = _binding!!
//    val args: MyAdvertsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateAdvertBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        setListeners()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setListeners() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
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
                    binding.toolbar.visibility = View.VISIBLE
                    binding.toolbar.setNavigationIcon(R.drawable.ic_exit)
                }
                R.id.item_edit_post -> {
                    binding.toolbar.visibility = View.VISIBLE
                    binding.toolbar.setNavigationIcon(R.drawable.ic_back_arrow_black)
                }
                R.id.item_location_city -> {
                    binding.toolbar.visibility = View.GONE
                    binding.toolbar.navigationIcon = null
                }
                R.id.item_location_country -> {
                    binding.toolbar.visibility = View.GONE
                    binding.toolbar.navigationIcon = null
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

    override fun onBackPressed() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
