package com.padcmyanmar.mjnm.themovieapp.mvi.mvibase

interface MVIView<S : MVIState> {
    fun render(state: S)
}