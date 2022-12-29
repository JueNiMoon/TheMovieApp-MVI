package com.padcmyanmar.mjnm.themovieapp.mvi.processor

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import com.padcmyanmar.mjnm.themovieapp.data.models.MovieModel
import com.padcmyanmar.mjnm.themovieapp.data.models.MovieModelImpl
import com.padcmyanmar.mjnm.themovieapp.mvi.states.MovieDeatilState
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable

object MovieDetailProcessor {
    private val mMovieModel: MovieModel = MovieModelImpl

    fun loadMovieDetailsData(
        previousState: MovieDeatilState,
        movieId : String
    ) : LiveData<MovieDeatilState>{
        return Observable.zip(
            mMovieModel.getMovieByIdObservable(movieId),
            mMovieModel.getCreditByMovieObservable(movieId)
        ){ movieDetail, creditByMovie ->
            return@zip previousState.copy(
                errorMessage = "",
                movieDetail = movieDetail,
                creditByMovie = creditByMovie,
            )

        }.toFlowable(BackpressureStrategy.BUFFER)
            .toLiveData()
    }
}