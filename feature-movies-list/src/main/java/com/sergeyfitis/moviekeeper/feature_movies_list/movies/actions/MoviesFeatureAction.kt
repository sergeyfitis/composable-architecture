package com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions

import com.syaremych.composable_architecture.prelude.types.Prism
import com.syaremych.composable_architecture.prelude.types.toOption

sealed class MoviesFeatureAction {
    data class Movies(val action: MoviesAction) : MoviesFeatureAction()

    companion object
}

internal val MoviesFeatureAction.Companion.moviesAction: Prism<MoviesFeatureAction, MoviesAction>
    get() = Prism(
        get = { featureAction -> when(featureAction) {
            is MoviesFeatureAction.Movies -> featureAction.action.toOption()
        } },
        reverseGet = { viewAction -> MoviesFeatureAction.Movies(viewAction) }
    )