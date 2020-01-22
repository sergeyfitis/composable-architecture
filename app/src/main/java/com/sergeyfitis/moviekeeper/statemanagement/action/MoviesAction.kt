package com.sergeyfitis.moviekeeper.statemanagement.action

import com.sergeyfitis.moviekeeper.models.Movie
import kotlinx.coroutines.CoroutineScope

sealed class AppAction

fun <T : AppAction> T.asAppAction() = this as AppAction
fun <T : AppAction> AppAction.asLocalAction() = this as? T

sealed class MoviesAction : AppAction() {
    data class Load(val scope: CoroutineScope) : MoviesAction()
    data class Loaded(val movies: List<Movie>) : MoviesAction()
}

sealed class FavoriteAction : AppAction() {
    data class SaveFavorite(val movie: String) : FavoriteAction()
    data class RemoveFavorite(val movie: String) : FavoriteAction()
}

sealed class MovieDetailsAction : AppAction() {
    data class MovieDetails(val movie: String) : MovieDetailsAction()
}