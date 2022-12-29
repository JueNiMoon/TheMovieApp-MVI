package com.padcmyanmar.mjnm.themovieapp.mvi.states

import com.padcmyanmar.mjnm.themovieapp.data.vos.ActorVO
import com.padcmyanmar.mjnm.themovieapp.data.vos.GenreVO
import com.padcmyanmar.mjnm.themovieapp.data.vos.MovieVO
import com.padcmyanmar.mjnm.themovieapp.mvi.mvibase.MVIState

data class MainState(
    val isLoading: Boolean = false,
    val errorMessage: String ="",
    val nowPlayingMovies: List<MovieVO>,
    val popularMovies: List<MovieVO>,
    val topRatedMovies: List<MovieVO>,
    val genres: List<GenreVO>,
    val moviesByGenre: List<MovieVO>,
    val actors: List<ActorVO>
) : MVIState {
    companion object{
        fun idle(): MainState = MainState(
            isLoading = false,
            errorMessage = "",
            nowPlayingMovies = listOf(),
            popularMovies = listOf(),
            topRatedMovies = listOf(),
            genres = listOf(),
            moviesByGenre = listOf(),
            actors = listOf(),
        )
    }
}