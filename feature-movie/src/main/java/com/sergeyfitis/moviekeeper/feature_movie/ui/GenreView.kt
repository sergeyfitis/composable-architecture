package com.sergeyfitis.moviekeeper.feature_movie.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.Preview
import com.sergeyfitis.moviekeeper.common.ext.horizontalRoundedGradientBackground
import com.sergeyfitis.moviekeeper.common.theme.MovieAppTheme
import com.sergeyfitis.moviekeeper.common.theme.gradient0

@Composable
private fun GenreItemView(genre: String) {
    Text(
        modifier = Modifier
            .horizontalRoundedGradientBackground(gradient0, filled = false, borderWidth = 2f)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        text = genre)
}

@Composable
fun GenreList(genres: List<String>) = LazyRowFor(
    items = genres,
    contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
) { genre ->
    GenreItemView(genre)
    Spacer(modifier = Modifier.width(8.dp))
}

@Preview(device = Devices.NEXUS_5, showBackground = true, backgroundColor = 0xffffffL)
@Composable
private fun PreviewGenreList() {
    MovieAppTheme {
        GenreList(genres = listOf("Action", "Adventure", "Fantasy"))
    }
}