package com.sergeyfitis.moviekeeper.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.sergeyfitis.moviekeeper.common.ext.applyIf
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun MoviePoster(
    modifier: Modifier = Modifier,
    url: String,
    posterWidth: Dp = Dp.Unspecified,
    shape: Shape = RoundedCornerShape(8.dp),
    aspectRatio: Float = 0.6f,
    elevation: Dp = 8.dp,
    drawBorder: Boolean = true,
    borderColor: Color = Color.LightGray,
    backgroundColor: Color = Color.LightGray,
    onClick: () -> Unit = {}
) {
    val posterModifier = modifier
        .shadow(elevation = elevation, shape = shape)
        .applyIf(posterWidth != Dp.Unspecified) { width(posterWidth) }
        .aspectRatio(aspectRatio)
        .background(backgroundColor)
        .applyIf(drawBorder) { border(0.5.dp, borderColor, shape) }
        .clickable(onClick = onClick)

    Column {
        CoilImage(
            request = ImageRequest.Builder(LocalContext.current).crossfade(true).data(url).build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = posterModifier
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Preview(backgroundColor = 0xffffffL, showBackground = true)
@Composable
internal fun PosterPreview() {
    MoviePoster(modifier = Modifier.padding(16.dp), url = "", posterWidth = 86.dp)
}