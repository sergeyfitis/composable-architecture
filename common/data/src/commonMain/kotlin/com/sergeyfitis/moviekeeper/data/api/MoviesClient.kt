package com.sergeyfitis.moviekeeper.data.api

import com.sergeyfitis.moviekeeper.data.models.GenresResponse
import com.sergeyfitis.moviekeeper.data.models.MoviesResponse

class MoviesClient(
    val nowPlaying: suspend () -> MoviesResponse,
    val upcoming: suspend () -> MoviesResponse,
    val topRated: suspend () -> MoviesResponse,
    val genres: suspend () -> GenresResponse
) {
    companion object
}

internal val moviesApi: TheMovieDbApi = AppRestAPI(http = AppHttpClient())

val MoviesClient.Companion.live: MoviesClient
    get() = MoviesClient(
        nowPlaying = moviesApi::nowPlaying,
        upcoming = moviesApi::upcoming,
        topRated = moviesApi::topRated,
        genres = moviesApi::genres
    )

//val MoviesClient.Companion.fake: MoviesClient
//    get() = TODO()