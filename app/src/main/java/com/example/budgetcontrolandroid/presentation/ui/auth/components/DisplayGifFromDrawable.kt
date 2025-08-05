package com.example.budgetcontrolandroid.presentation.ui.auth.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest

@Composable
fun DisplayGifFromDrawable() {
    val context = LocalContext.current
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(
                context.resources.getIdentifier(
                    "coin_flip_transparent_unscreen",
                    "drawable",
                    context.packageName
                )
            )
            .decoderFactory(GifDecoder.Factory())
            .crossfade(true)
            .build(),
        contentDescription = "Animated GIF",
        modifier = Modifier.size(100.dp)
    )
}
