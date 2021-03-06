package com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.action

import com.syaremych.composable_architecture.prelude.types.Prism

sealed class FavoriteFeatureAction {
    data class FavoriteMovies(val action: FavoriteMoviesAction) : FavoriteFeatureAction()
    companion object
}


internal val FavoriteFeatureAction.Companion.favoriteMoviesAction: Prism<FavoriteFeatureAction, FavoriteMoviesAction>
    get() = Prism(
        get = { featureAction ->
            when (featureAction) {
                is FavoriteFeatureAction.FavoriteMovies -> featureAction.action
            }
        },
        reverseGet = FavoriteFeatureAction::FavoriteMovies
    )