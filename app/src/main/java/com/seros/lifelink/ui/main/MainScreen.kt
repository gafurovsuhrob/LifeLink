package com.seros.lifelink.ui.main


import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.seros.lifelink.domain.FamilyMember
import com.seros.lifelink.utils.bitmapDescriptorFromVector
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = koinViewModel()
) {
    val members by mainViewModel.members.collectAsState()
    val moscow = LatLng(55.6700, 37.5500)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(moscow, 14f)
    }

    var backPressedOnce by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(backPressedOnce) {
        if (backPressedOnce) {
            delay(2000)
            backPressedOnce = false
        }
    }

    BackHandler {
        if (backPressedOnce) {
            (context as? Activity)?.finish()
        } else {
            backPressedOnce = true
            Toast.makeText(context, "Нажмите еще раз чтобы выйти", Toast.LENGTH_SHORT).show()
        }
    }


    var selectedMember by remember { mutableStateOf<FamilyMember?>(null) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val coroutineScope = rememberCoroutineScope()

    var contentAlpha by remember { mutableFloatStateOf(0f) }
    val alphaAnim by animateFloatAsState(
        targetValue = contentAlpha,
        animationSpec = tween(durationMillis = 400), label = ""
    )

    var contentScale by remember { mutableFloatStateOf(0.95f) }
    val scaleAnim by animateFloatAsState(
        targetValue = contentScale,
        animationSpec = tween(durationMillis = 400), label = ""
    )

    val sheetVisible = sheetState.isVisible



    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            members.forEach { member ->
                Marker(
                    state = rememberMarkerState(position = member.location),
                    title = member.name,
                    icon = member.iconRes?.let { bitmapDescriptorFromVector(it, sizeDp = 32) },
                    onClick = {
                        selectedMember = member
                        false
                    }
                )
            }

        }

        selectedMember?.let { member ->
            AlertDialog(
                onDismissRequest = { selectedMember = null },
                confirmButton = {
                    TextButton(onClick = { selectedMember = null }) {
                        Text("OK")
                    }
                },
                title = { Text(member.name) },
                text = {
                    Text("Сейчас находится: ${member.currentPlace}")
                }
            )
        }

        if (!sheetVisible) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        contentAlpha = 0f
                        contentScale = 0.95f
                        sheetState.show()
                        contentAlpha = 1f
                        contentScale = 1f
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(end = 48.dp, bottom = 16.dp)
            ) {
                Text("Открыть меню")
            }
        }
    }

    if (sheetVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide()
                }
            },
            sheetState = sheetState,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .alpha(alphaAnim)
                    .scale(scaleAnim)
            ) {
                val context = LocalContext.current

                Text(
                    text = "Где я сейчас",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Москва, улица Ленина, 10",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Члены семьи:",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                members.forEach { member ->
                    Text(
                        text = "${member.name}: ${member.currentPlace}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        coroutineScope.launch {
                            sheetState.hide()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Закрыть")
                }
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            mainViewModel.sendNotification(context)
                            sheetState.hide()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Событие")
                }
            }
        }
    }
}
