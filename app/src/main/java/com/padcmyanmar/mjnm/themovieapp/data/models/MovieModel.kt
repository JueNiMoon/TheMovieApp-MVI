package com.padcmyanmar.mjnm.themovieapp.data.models

import androidx.lifecycle.LiveData
import com.padcmyanmar.mjnm.themovieapp.data.vos.ActorVO
import com.padcmyanmar.mjnm.themovieapp.data.vos.GenreVO
import com.padcmyanmar.mjnm.themovieapp.data.vos.MovieVO
import io.reactivex.rxjava3.core.Observable

interface MovieModel {
    fun getNowPlayingMovies(
        onFailure : (String) -> Unit
    ) : LiveData<List<MovieVO>>?

    fun getPopularMovies(
        onFailure: (String) -> Unit
    ) : LiveData<List<MovieVO>>?

    fun getTopRatedMovies(
        onFailure: (String) -> Unit
    ) : LiveData<List<MovieVO>>?

    fun getGenre(
        onSuccess: (List<GenreVO>) -> Unit,
        onFailure: (String) -> Unit

    )

    fun getMovieByGenre(
        genreId : String,
        onSuccess: (List<MovieVO>) -> Unit,
        onFailure: (String) -> Unit
    )

    fun getActors(
        onSuccess: (List<ActorVO>) -> Unit,
        onFailure: (String) -> Unit

    )

    fun getMovieDetails(
        movieId : String,
        onFailure: (String) -> Unit
    ) : LiveData<MovieVO?>?

    fun getCreditsByMovie(
        movieId : String,
        onSuccess: (Pair<List<ActorVO>, List<ActorVO>>) -> Unit,
        onFailure: (String) -> Unit
    )

    fun searchMovie(
        query : String
    ) : Observable<List<MovieVO>>

    //Reactive Stram Only
    fun getNowPlayingMoviesObservable(): Observable<List<MovieVO>>?
    fun getPopularMoviesObservable(): Observable<List<MovieVO>>?
    fun getTopRatedMoviesObservable(): Observable<List<MovieVO>>?
    fun getGenresObservable(): Observable<List<GenreVO>>?
    fun getActorsObservable(): Observable<List<ActorVO>>?
    fun getMoviesByGenreObservable(genreId: String): Observable<List<MovieVO>>?
    fun getMovieByIdObservable(movieId: String): Observable<MovieVO?>?
    fun getCreditByMovieObservable(movieId: String): Observable<Pair<List<ActorVO>, List<ActorVO>>>?
}