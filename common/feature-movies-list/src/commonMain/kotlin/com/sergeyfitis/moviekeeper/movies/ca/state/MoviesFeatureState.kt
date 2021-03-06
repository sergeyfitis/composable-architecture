package com.sergeyfitis.moviekeeper.movies.ca.state

import com.sergeyfitis.moviekeeper.data.models.dto.GenreDTO
import com.sergeyfitis.moviekeeper.data.models.dto.MovieDTO
import com.syaremych.composable_architecture.prelude.types.Lens

// Should contain all variables of the feature(including all its sub-states)
data class MoviesFeatureState(
    val selectedMovie: MovieDTO?,
    val movies: Map<Int, MovieDTO>,
    val nowPlaying: Set<Int>,
    val upcoming: Set<Int>,
    val topRated: Set<Int>,
    val genres: Map<Int, GenreDTO>
) {
    companion object {
        fun init() = MoviesFeatureState(
            selectedMovie = null,
            movies = emptyMap(),
            nowPlaying = emptySet(),
            upcoming = emptySet(),
            topRated = emptySet(),
            genres = emptyMap()
        )
    }
}

// Should contain domain specific variables
internal data class MoviesState(
    val selectedMovie: MovieDTO?,
    val movies: Map<Int, MovieDTO>,
    val nowPlaying: Set<Int>,
    val upcoming: Set<Int>,
    val topRated: Set<Int>,
    val genres: Map<Int, GenreDTO>
)

internal val MoviesFeatureState.Companion.moviesState
    get() = Lens<MoviesFeatureState, MoviesState>(
        get = { featureState ->
            MoviesState(
                selectedMovie = null,
                movies = featureState.movies,
                nowPlaying = featureState.nowPlaying,
                upcoming = featureState.upcoming,
                topRated = featureState.topRated,
                genres = featureState.genres
            )
        },
        set = { featureState, viewState ->
            featureState.copy(
                selectedMovie = viewState.selectedMovie,
                movies = viewState.movies,
                nowPlaying = viewState.nowPlaying,
                upcoming = viewState.upcoming,
                topRated = viewState.topRated,
            )
        }
    )