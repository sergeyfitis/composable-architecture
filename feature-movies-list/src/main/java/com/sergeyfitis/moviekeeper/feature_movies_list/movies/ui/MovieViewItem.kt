package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.model.MovieItem
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade

@Composable
fun MovieViewItem(
    item: MovieItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    toggleFavorite: (Boolean) -> Unit = {}
) {
    val posterWidth = 78
    Stack(modifier = modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .preferredHeight(130.dp)
                .background(color = Color.White)
                .gravity(Alignment.BottomStart)
                .clickable(onClick = onClick)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        start = (posterWidth + 32).dp,
                        top = 16.dp,
                        bottom = 16.dp,
                        end = 16.dp
                    )
            ) {
                Column(
                    modifier = Modifier.weight(weight = 1f)
                ) {
                    Text(
                        text = item.title,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalGravity = Alignment.CenterVertically) {
                        Icon(asset = Filled.Star, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(
                            text = "${item.rating}",
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Action, Drama",
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        text = "Voted ${item.voted}",
                        style = MaterialTheme.typography.body2
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
        IconToggleButton(
            modifier = Modifier
                .padding(top = 16.dp)
                .gravity(Alignment.TopEnd),
            checked = item.isFavorite,
            onCheckedChange = toggleFavorite
        ) {
            Icon(
                asset = if (item.isFavorite) Filled.Favorite else Outlined.FavoriteBorder,
                tint = if (item.isFavorite) Color.Blue else Color.Black
            )
        }

        MoviePoster(
            url = item.posterUrl,
            posterWidth = posterWidth
        )
    }
}

@Composable
private fun MoviePoster(modifier: Modifier = Modifier, url: String, posterWidth: Int) {
    val posterShape = remember { RoundedCornerShape(4.dp) }
    val posterModifier = modifier
        .padding(start = 16.dp, bottom = 16.dp)
        .preferredWidth(posterWidth.dp)
        .aspectRatio(.6f)
        .drawShadow(elevation = 8.dp, shape = posterShape)
        .background(Color.LightGray)
        .border(0.5.dp, Color.LightGray, posterShape)
        .clip(posterShape)

    CoilImageWithCrossfade(
        data = url,
        contentScale = ContentScale.Crop,
        modifier = posterModifier
    )
}