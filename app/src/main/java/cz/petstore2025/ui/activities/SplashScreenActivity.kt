package cz.petstore2025.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {
    private val viewModel: SplashScreenActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.splashScreenState.collect { value ->
                    when (value) {
                        is SplashScreenUiState.Default -> {
                            viewModel.checkAppState()
                        }
                        SplashScreenUiState.ContinueToApp -> {
                            continueToAList(showLogin = false)
                        }
                        is SplashScreenUiState.ShowLogin -> {
                            continueToAList(showLogin = true)
                        }
                    }
                }
            }
        }

    }

    private fun continueToAList(showLogin: Boolean){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("login", showLogin)
        startActivity(intent)
        finish()
    }

}
