package com.sergeyfitis.moviekeeper.feature_movie.reducer

import com.sergeyfitis.moviekeeper.feature_movie.action.MovieAction
import com.sergeyfitis.moviekeeper.feature_movie.action.MovieFeatureAction
import com.sergeyfitis.moviekeeper.feature_movie.action.movieAction
import com.sergeyfitis.moviekeeper.feature_movie.environment.MovieFeatureEnvironment
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieFeatureState
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieState
import com.sergeyfitis.moviekeeper.feature_movie.state.movieState
import com.syaremych.composable_architecture.prelude.id
import com.syaremych.composable_architecture.store.Reducer
import com.syaremych.composable_architecture.store.combine
import com.syaremych.composable_architecture.store.pullback

internal val movieViewReducer: Reducer<MovieState, MovieAction, MovieFeatureEnvironment> =
    Reducer { state, action, environment ->
        when(action) {
            is MovieAction.LoadDetails -> TODO()
            is MovieAction.DetailsLoaded -> TODO()
            is MovieAction.Favorite -> TODO()
            is MovieAction.UnFavorite -> TODO()
        }
    }

val movieFeatureReducer: Reducer<MovieFeatureState, MovieFeatureAction, MovieFeatureEnvironment> =
    Reducer.combine(
        movieViewReducer.pullback(
            value = MovieFeatureState.movieState,
            action = MovieFeatureAction.movieAction,
            environment = ::id
        )
    )