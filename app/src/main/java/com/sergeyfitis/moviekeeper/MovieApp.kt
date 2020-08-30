package com.sergeyfitis.moviekeeper

import android.app.Application
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.sergeyfitis.moviekeeper.base.AppActivityLifecycleCallbacks
import com.sergeyfitis.moviekeeper.base.AppFragmentFactory
import com.sergeyfitis.moviekeeper.data.api.MoviesClient
import com.sergeyfitis.moviekeeper.data.api.live
import com.sergeyfitis.moviekeeper.navigation.AppNavigator
import com.sergeyfitis.moviekeeper.navigation.movie.AppMovieNavigator
import com.sergeyfitis.moviekeeper.navigation.movielist.AppMovieListNavigator
import com.sergeyfitis.moviekeeper.statemanagement.action.AppAction
import com.sergeyfitis.moviekeeper.statemanagement.action.movieFeatureAction
import com.sergeyfitis.moviekeeper.statemanagement.action.moviesFeatureAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.AppState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.movieFeatureState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.moviesFeatureState
import com.sergeyfitis.moviekeeper.statemanagement.environment.AppEnvironment
import com.sergeyfitis.moviekeeper.statemanagement.reducer.appReducer
import com.sergeyfitis.moviekeeper.ui.MainActivity
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.getOrThrow
import com.syaremych.composable_architecture.prelude.types.toOption
import com.syaremych.composable_architecture.store.Store

//import kotlinx.android.synthetic.main.activity_main.*

class MovieApp : Application() {

    companion object {
        lateinit var appFragmentFactory: AppFragmentFactory
            private set
    }

    private val appStore = Store.init<AppState, AppAction, AppEnvironment>(
        initialState = AppState.initial(),
        reducer = appReducer,
        environment = AppEnvironment(MoviesClient.live)
    )

    private var mainNavHostActivity: Option<MainActivity> =
        Option.empty() // Is this good enough solution?

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(AppActivityLifecycleCallbacks(
            onMainActivityCreated = { mainNavHostActivity = it.toOption() },
            onMainActivityDestroyed = { mainNavHostActivity = Option.empty() }
        ))

        val navControllerLazy: () -> NavController = {
            mainNavHostActivity
                .getOrThrow()
                .mainNavHost
                .findNavController()
        }

        val appNavigatorLazy = {
            AppNavigator(
                movieListNavigation = AppMovieListNavigator(navControllerLazy.invoke()),
                movieNavigation = AppMovieNavigator(navControllerLazy.invoke())
            )
        }

        val movieStoreLazy = {
            appStore.scope(
                toLocalValue = AppState.movieFeatureState::get,
                toGlobalAction = AppAction.movieFeatureAction::reverseGet
            )
        }
        val moviesStoreLazy = {
            appStore.scope(
                toLocalValue = AppState.moviesFeatureState::get,
                toGlobalAction = AppAction.moviesFeatureAction::reverseGet
            )
        }
        appFragmentFactory = AppFragmentFactory(
            appNavigatorLazy,
            moviesStoreLazy,
            movieStoreLazy
        )
    }
}