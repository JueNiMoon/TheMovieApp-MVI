package com.padcmyanmar.mjnm.themovieapp.mvi.states

import com.padcmyanmar.mjnm.themovieapp.data.vos.ActorVO
import com.padcmyanmar.mjnm.themovieapp.data.vos.MovieVO
import com.padcmyanmar.mjnm.themovieapp.mvi.mvibase.MVIState

data class MovieDeatilState(
    val errorMessage: String ="",
    val movieDetail: MovieVO?,
    val creditByMovie: Pair<List<ActorVO>,List<ActorVO>>,
) : MVIState {
    companion object{
        fun idle(): MovieDeatilState = MovieDeatilState(
            errorMessage = "",
            movieDetail = null,
            creditByMovie = Pair(listOf(), listOf())
        )
    }
}