package com.padcmyanmar.mjnm.themovieapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.padcmyanmar.mjnm.themovieapp.R
import com.padcmyanmar.mjnm.themovieapp.data.models.MovieModel
import com.padcmyanmar.mjnm.themovieapp.data.models.MovieModelImpl
import com.padcmyanmar.mjnm.themovieapp.data.vos.GenreVO
import com.padcmyanmar.mjnm.themovieapp.data.vos.MovieVO
import com.padcmyanmar.mjnm.themovieapp.mvi.intents.MainIntent
import com.padcmyanmar.mjnm.themovieapp.mvi.intents.MovieDetailsIntent
import com.padcmyanmar.mjnm.themovieapp.mvi.mvibase.MVIView
import com.padcmyanmar.mjnm.themovieapp.mvi.states.MainState
import com.padcmyanmar.mjnm.themovieapp.mvi.states.MovieDeatilState
import com.padcmyanmar.mjnm.themovieapp.mvi.viewmodels.MainViewModel
import com.padcmyanmar.mjnm.themovieapp.mvi.viewmodels.MovieDetailViewModel
import com.padcmyanmar.mjnm.themovieapp.utils.IMAGE_BASE_URL
import com.padcmyanmar.mjnm.themovieapp.viewpods.ActorListViewPod
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.activity_movie_details.tvMovieName
import kotlinx.android.synthetic.main.view_holder_movie.*

class MovieDetailsActivity : AppCompatActivity() , MVIView<MovieDeatilState> {

 //   private val mMovieModel: MovieModel = MovieModelImpl

    //view model
    private lateinit var mViewModel: MovieDetailViewModel

    companion object{

        private const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"

        fun newIntent(context: Context, movieId: Int): Intent {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra(EXTRA_MOVIE_ID, movieId)
            return intent
        }
    }

    lateinit var actorsViewPod : ActorListViewPod
    lateinit var creatorViewPod: ActorListViewPod

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        setUpViewModel()

        setUpViewPods()
        setUpListener()

        val movieId = intent?.getIntExtra(EXTRA_MOVIE_ID,0)



        movieId?.let {
            //requestData(it)
            setInitialIntents(it.toString())
            observeState()
        }
    }

    private fun setInitialIntents(movieId: String){
        mViewModel.processIntent(MovieDetailsIntent.LoadMovieDetailsData(movieId),this)
    }

    private fun setUpViewModel(){
        mViewModel = ViewModelProvider(this)[MovieDetailViewModel::class.java]
    }

    private fun observeState(){
        mViewModel.state.observe(this,this::render)
    }

//    private fun requestData(movieId: Int){
//        mMovieModel.getMovieDetails(
//            movieId = movieId.toString(),
//            onFailure = {showError(it)}
//        )?.observe(this) {
//            it?.let { movieDetails -> bindData(movieDetails) }
//        }
//
//        mMovieModel.getCreditsByMovie(
//            movieId = movieId.toString(),
//            onSuccess = {
//                actorsViewPod.setData(it.first)
//                creatorViewPod.setData(it.second)
//            }, onFailure = {
//                showError(it)
//            }
//        )
//    }

    private fun bindData(movie : MovieVO){
        Glide.with(this)
            .load("$IMAGE_BASE_URL${movie.posterPath}")
            .into(ivMovieDetails)
        tvMovieName.text = movie.title ?: ""
        tvMovieReleaseYear.text = movie.releaseDate?.substring(0,4)
        tvRating.text = movie.voteAverage?.toString()?:""
        movie.voteCount?.let {
            tvNumbersofVotes.text = "$it VOTES"
        }
        rbRatingMovieDetials.rating = movie.getRatingBasedOnFiveStars()

        bindGenres(movie, movie.genres ?: listOf())

        tvOverview.text = movie.overView?:""
        tvOriginalTitle.text = movie.title ?: ""
        tvType.text = movie.getGenresAsCommaSeparatedString()
        tvProduction.text = movie.getProductionCountriesAsCommaSeparatedString()
        tvPremiere.text = movie.releaseDate ?: ""
        tvDescription.text = movie.overView ?: ""
    }

    private fun bindGenres(movie: MovieVO,genres: List<GenreVO>){
        movie.genres?.count()?.let{
            tvFirstGenre.text = genres.firstOrNull()?.name ?: ""
            tvSecondGenre.text = genres.getOrNull(1)?.name ?: ""
            tvThirdGenre.text = genres.getOrNull(2)?.name ?: ""

            if(it< 3){
                tvThirdGenre.visibility = View.GONE
            }else if (it < 2) {
                tvSecondGenre.visibility = View.GONE
            }
        }
    }

    private fun setUpListener(){
        ivSearch.setOnClickListener {
            startActivity(MovieSearchActivity.newIntent(this))
        }
        btnBack.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun showError(message: String){
        Snackbar.make(window.decorView,message,Snackbar.LENGTH_LONG).show()
    }

    private fun setUpViewPods(){
        actorsViewPod = vpActors as ActorListViewPod
        actorsViewPod.setUpActorViewPod(
            backgroundcolorReference =  R.color.colorPrimary,
            titleText = getString(R.string.lbl_actors),
            moreTitleText = ""
        )

        creatorViewPod = vpCreators as ActorListViewPod
        creatorViewPod.setUpActorViewPod(
            backgroundcolorReference = R.color.colorPrimary,
            titleText = getString(R.string.lbl_creators),
            moreTitleText = getString(R.string.lbl_more_creators)
        )
    }

    override fun render(state: MovieDeatilState) {
        if(state.errorMessage.isNotEmpty()){
            showError(state.errorMessage)
        }
        state.movieDetail?.let { bindData(it) }
        actorsViewPod.setData(state.creditByMovie.first)
        creatorViewPod.setData(state.creditByMovie.second)
    }
}