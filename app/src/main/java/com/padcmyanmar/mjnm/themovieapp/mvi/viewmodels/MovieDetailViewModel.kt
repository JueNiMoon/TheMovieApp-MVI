package com.padcmyanmar.mjnm.themovieapp.mvi.viewmodels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.padcmyanmar.mjnm.themovieapp.mvi.intents.MovieDetailsIntent
import com.padcmyanmar.mjnm.themovieapp.mvi.mvibase.MVIViewModel
import com.padcmyanmar.mjnm.themovieapp.mvi.processor.MovieDetailProcessor
import com.padcmyanmar.mjnm.themovieapp.mvi.states.MainState
import com.padcmyanmar.mjnm.themovieapp.mvi.states.MovieDeatilState

class MovieDetailViewModel(override var state: MutableLiveData<MovieDeatilState> = MutableLiveData(MovieDeatilState.idle()))
    : MVIViewModel<MovieDeatilState, MovieDetailsIntent>, ViewModel(){

    private val  mProcessor = MovieDetailProcessor

    override fun processIntent(intent: MovieDetailsIntent, lifecycleOwner: LifecycleOwner) {
       when(intent){
           is MovieDetailsIntent.LoadMovieDetailsData -> {
               state.value.let {
                   val movieId = intent.movieId ?: 0
                   it?.let { it1 ->
                       mProcessor.loadMovieDetailsData(
                           movieId = movieId.toString(),
                           previousState = it1
                       ).observe(lifecycleOwner,state::postValue)
                   }
               }
           }
       }
    }
}

