package com.sergeyfitis.moviekeeper.statemanagement.action

import com.sergeyfitis.moviekeeper.feature_movie.action.MovieFeatureAction
import com.sergeyfitis.moviekeeper.feature_movie.action.MovieAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesFeatureAction
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.Prism
import com.syaremych.composable_architecture.prelude.types.toOption

sealed class AppAction {
    data class Movies(val action: MoviesFeatureAction) : AppAction()
    data class Movie(val action: MovieFeatureAction) : AppAction()

    companion object
}

val AppAction.Companion.moviesFeatureAction
    get() = Prism<AppAction, MoviesFeatureAction>(
        get = { appAction ->
            if (appAction is AppAction.Movies)
                appAction.action.toOption()
            else
                Option.empty()
        },
        reverseGet = AppAction::Movies
    )

val AppAction.Companion.movieFeatureAction
    get() = Prism<AppAction, MovieFeatureAction>(
        get = { appAction ->
            if (appAction is AppAction.Movie)
                appAction.action.toOption()
            else
                Option.empty()
        },
        reverseGet = AppAction::Movie
    )

