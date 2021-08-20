package kz.diaspora.app.ui

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.diaspora.app.R
import kz.diaspora.app.core.App
import kz.diaspora.app.databinding.ActivityMainBinding
import kz.diaspora.app.utils.setupWithNavController

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = this::class.java.simpleName
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentNavController: LiveData<NavController>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
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

    public fun setToolbarTitle(title: String) {
        binding.tvToolbar.text = title
    }

    public fun setToolbarLike(vis: Boolean) {
        if (vis) binding.ivLike.visibility = View.VISIBLE
        else binding.ivLike.visibility = View.GONE
    }

    public fun setToolbarImage(vis: Boolean) {
        if (vis) {
            binding.ivLike.setImageDrawable(ContextCompat.getDrawable(App.instance, R.drawable.ic_like_active))
        } else {
            binding.ivLike.setImageDrawable(ContextCompat.getDrawable(App.instance, R.drawable.ic_like))
        }
    }

    public fun setToolbarEndText(title: String) {
        binding.tvDrop.text = title
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setListeners() {
//        binding.ivLike.setOnClickListener {
//            if (binding.ivLike.getDrawable().getConstantState() == baseContext?.getResources()?.getDrawable(R.drawable.ic_like_active)?.getConstantState()) {
//                binding.ivLike.setImageResource(R.drawable.ic_like)
//            }
//            else binding.ivLike.setImageResource(R.drawable.ic_like_active)
//        }
    }

    private fun setupBottomNavigationBar() {

        val navGraphIds = listOf(R.navigation.tab1, R.navigation.tab2, R.navigation.tab3, R.navigation.tab4)

        val controller = binding.bnvNavigation.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        controller.observe(this, { navController ->
            setupActionBarWithNavController(navController)
            currentNavController.value?.addOnDestinationChangedListener(listener)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController.value?.navigateUp() ?: false
    }

    private val listener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            binding.toolbar.setNavigationIcon(R.drawable.ic_back_arrow_black)
            when (destination.id) {
                R.id.item_home -> {
                    setToolbarTitle("")
                    setToolbarLike(false)
                    setToolbarEndText("")
                    binding.toolbar.visibility = View.GONE
                    binding.toolbar.navigationIcon = null
                }
                R.id.item_messages -> {
                    setToolbarTitle("")
                    setToolbarLike(false)
                    setToolbarEndText("")
                    binding.toolbar.visibility = View.VISIBLE
//                    binding.toolbar.navigationIcon = null
                }
                R.id.item_notifications -> {
                    setToolbarTitle("")
                    setToolbarLike(false)
                    setToolbarEndText("")
                    binding.toolbar.visibility = View.VISIBLE
                    binding.toolbar.navigationIcon = null
                }
                R.id.item_profile -> {
                    setToolbarTitle("")
                    setToolbarLike(false)
                    setToolbarEndText("")
                    binding.toolbar.visibility = View.GONE
                    binding.toolbar.navigationIcon = null
                }
                R.id.item_profile_info -> {
                    setToolbarTitle("")
                    setToolbarLike(false)
                    setToolbarEndText("")
                    binding.toolbar.visibility = View.VISIBLE
//                    binding.toolbar.navigationIcon = null
                }
                R.id.item_chat -> {
                    setToolbarTitle("")
                    setToolbarLike(false)
                    setToolbarEndText("")
                    binding.toolbar.visibility = View.VISIBLE
                    binding.toolbar.navigationIcon = null
                }
                else -> {
                    setToolbarTitle("")
                    setToolbarLike(false)
                    setToolbarEndText("")
                }
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
