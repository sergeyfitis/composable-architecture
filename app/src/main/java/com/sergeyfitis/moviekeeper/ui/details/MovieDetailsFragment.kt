package com.sergeyfitis.moviekeeper.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.navigation.fragment.navArgs
import com.sergeyfitis.moviekeeper.R
import com.sergeyfitis.moviekeeper.prelude.pullback
import com.sergeyfitis.moviekeeper.statemanagement.action.MovieAction
import com.sergeyfitis.moviekeeper.statemanagement.action.MovieViewAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.MovieState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.MovieViewState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.movieStateLens
import com.sergeyfitis.moviekeeper.statemanagement.store.*
import kotlinx.coroutines.launch

class MovieDetailsFragment(
    store: Store<MovieViewState, MovieViewAction>
) : Fragment(R.layout.fragment_movie_details) {

    private val liveStore = store.asLiveData()
    private val args by navArgs<MovieDetailsFragmentArgs>()

    init {
        lifecycleScope.launch {
            whenCreated {
                val movieId = args.movieId
                liveStore.send(MovieViewAction.getDetails(this, movieId))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

private val movieStateReducer =
    fun(state: MovieState, action: MovieAction): Reduced<MovieState, MovieAction> {
        return when (action) {
            is MovieAction.GetDetails -> TODO() // load cast, etc
            is MovieAction.Loaded -> reduced(
                value = state.copy(movie = action.movie),
                effects = noEffects()
            )
        }
    }

val movieViewReducer =
    pullback<MovieState, MovieViewState, MovieAction, MovieViewAction>(
        reducer = movieStateReducer,
        valueGet = MovieViewState.movieStateLens::get,
        valueSet = MovieViewState.movieStateLens::set,
        toLocalAction = MovieViewAction.moviePrism::getOption,
        toGlobalAction = { map(MovieViewAction.moviePrism::reverseGet) }
    )
