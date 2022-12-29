package com.padcmyanmar.mjnm.themovieapp.mvi.intents

import com.padcmyanmar.mjnm.themovieapp.mvi.mvibase.MVIIntent

sealed class MovieDetailsIntent : MVIIntent{
//    class LoadMovieDetailByMovieId(val movieId: String) : MovieDetailsIntent()
//    class LoadCreditsByMovieId(val movieId: String) : MovieDetailsIntent()
    class LoadMovieDetailsData(val movieId: String) : MovieDetailsIntent()
}