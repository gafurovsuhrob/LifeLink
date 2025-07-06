package com.seros.lifelink

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.seros.lifelink.theme.LifeLinkTheme
import com.seros.lifelink.ui.login.LoginScreen
import com.seros.lifelink.ui.main.MainScreen
import com.seros.lifelink.ui.register.RegistrationScreen
import com.seros.lifelink.utils.ProvideSystemBarsStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProvideSystemBarsStyle {
                LifeLinkTheme {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }

                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.systemBars),
                        contentWindowInsets = WindowInsets.systemBars,
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            val navController = rememberNavController()
                            NavHost(navController, startDestination = "login") {
                                composable("login") { LoginScreen(navController) }
                                composable("main") { MainScreen() }
                                composable("register") {
                                    RegistrationScreen(navController)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(this, "Разрешение на уведомления не получено", Toast.LENGTH_SHORT).show()
        }
    }
}


