package com.seros.lifelink.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
@Composable
fun bitmapDescriptorFromVector(vectorResId: Int, sizeDp: Int = 48): BitmapDescriptor {
    val context = LocalContext.current
    val density = LocalDensity.current

    return remember(vectorResId) {
        val drawable = ContextCompat.getDrawable(context, vectorResId)
        val sizePx = with(density) { sizeDp.dp.roundToPx() }
        val bitmap = Bitmap.createBitmap(
            sizePx,
            sizePx,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable?.setBounds(0, 0, sizePx, sizePx)
        drawable?.draw(canvas)
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}
