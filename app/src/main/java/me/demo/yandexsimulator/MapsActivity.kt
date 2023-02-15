package me.demo.yandexsimulator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import me.demo.yandexsimulator.databinding.ActivityMapsBinding
import kotlin.properties.Delegates

class MapsActivity : AppCompatActivity() {

    private var binding: ActivityMapsBinding by Delegates.notNull()

    private val navController: NavController by lazy { findNavController(R.id.nav_host_container) }

    private var lastBackPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp() || super.onSupportNavigateUp()

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.mapFragment -> {
                if (System.currentTimeMillis() - lastBackPressedTime > 1000)
                    Toast.makeText(
                        this,
                        getString(R.string.confirm_exit_app),
                        Toast.LENGTH_SHORT
                    ).show()
                else
                    finish()

                lastBackPressedTime = System.currentTimeMillis()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }


}