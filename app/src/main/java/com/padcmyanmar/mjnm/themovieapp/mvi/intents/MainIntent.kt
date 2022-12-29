package com.padcmyanmar.mjnm.themovieapp.mvi.intents

import com.padcmyanmar.mjnm.themovieapp.mvi.mvibase.MVIIntent

sealed class MainIntent : MVIIntent{
    class LoadMoviesByGenreIntent(val genrePosition: Int) : MainIntent()
    object LoadAllHomePageData : MainIntent()
}
