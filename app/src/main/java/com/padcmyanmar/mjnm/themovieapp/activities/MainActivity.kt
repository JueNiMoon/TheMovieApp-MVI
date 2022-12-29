package com.padcmyanmar.mjnm.themovieapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.padcmyanmar.mjnm.themovieapp.R
import com.padcmyanmar.mjnm.themovieapp.adapters.BannerAdapter
import com.padcmyanmar.mjnm.themovieapp.adapters.ShowcaseAdapter
import com.padcmyanmar.mjnm.themovieapp.data.models.MovieModel
import com.padcmyanmar.mjnm.themovieapp.data.models.MovieModelImpl
import com.padcmyanmar.mjnm.themovieapp.data.vos.GenreVO
import com.padcmyanmar.mjnm.themovieapp.delegates.BannerViewHolderDelegate
import com.padcmyanmar.mjnm.themovieapp.delegates.MovieViewHolderDelegate
import com.padcmyanmar.mjnm.themovieapp.delegates.ShowcaseViewHolderDelegate
import com.padcmyanmar.mjnm.themovieapp.dummy.dummyGenreList
import com.padcmyanmar.mjnm.themovieapp.mvi.intents.MainIntent
import com.padcmyanmar.mjnm.themovieapp.mvi.mvibase.MVIView
import com.padcmyanmar.mjnm.themovieapp.mvi.states.MainState
import com.padcmyanmar.mjnm.themovieapp.mvi.viewmodels.MainViewModel
import com.padcmyanmar.mjnm.themovieapp.network.dataagents.MovieDataAgent
import com.padcmyanmar.mjnm.themovieapp.network.dataagents.MovieDataAgentImpl
import com.padcmyanmar.mjnm.themovieapp.network.dataagents.OkHttpDataAgentImpl
//import com.padcmyanmar.mjnm.themovieapp.network.dataagents.RetrofitDataAgentImpl
import com.padcmyanmar.mjnm.themovieapp.viewpods.ActorListViewPod
import com.padcmyanmar.mjnm.themovieapp.viewpods.MovieListViewPod
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_movie_details.*

class MainActivity : AppCompatActivity(), BannerViewHolderDelegate, ShowcaseViewHolderDelegate,
    MovieViewHolderDelegate, MVIView<MainState> {

    lateinit var mBannerAdapter : BannerAdapter
    lateinit var mShowcaseAdapter: ShowcaseAdapter

    lateinit var mBestPopularMovieListViewPod: MovieListViewPod
    lateinit var mMoviesByGenreViewPod: MovieListViewPod
    lateinit var mActorListViewPod: ActorListViewPod


    //view model
    private lateinit var mViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViewModel()

        setupToolBar()
        setUpViewPods()
        setupBunnerViewPager()
        setUpListeners()
        setUpShowCaseRecyclerView()

        setInitialIntents()
        observeState()

    }

    private fun setUpViewModel(){
        mViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    private fun setInitialIntents(){
        mViewModel.processIntent(MainIntent.LoadAllHomePageData,this)
    }

    private fun observeState(){
        mViewModel.state.observe(this,this::render)
    }

    private fun showError(message: String){
        Snackbar.make(window.decorView,message,Snackbar.LENGTH_LONG).show()
    }

    private fun setUpViewPods(){
        mBestPopularMovieListViewPod = vpBestPopularMovieList as MovieListViewPod
        mBestPopularMovieListViewPod.setUpMovieListViewPod(this)

        mMoviesByGenreViewPod = vpMoviesByGenre as MovieListViewPod
        mMoviesByGenreViewPod.setUpMovieListViewPod(this)

        mActorListViewPod = vpActorsList as ActorListViewPod
    }

    private fun  setUpShowCaseRecyclerView(){
        mShowcaseAdapter = ShowcaseAdapter(this)
        rvShowCases.adapter = mShowcaseAdapter
        rvShowCases.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL,false)
    }

    private fun setUpListeners(){
        // Genre Tab Layout
        tabLayoutGenre.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                mViewModel.processIntent(
                    MainIntent.LoadMoviesByGenreIntent(tab?.position?:0),
                    this@MainActivity
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    private fun setupToolBar() {
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
    }

    private fun setUpGenreTabLayout(genreList: List<GenreVO>){
        genreList.forEach{
            tabLayoutGenre.newTab().apply {
                text = it.name
                tabLayoutGenre.addTab(this)
            }
        }
    }

    private fun setupBunnerViewPager(){
        mBannerAdapter = BannerAdapter(this)
        viewPagerBanner.adapter = mBannerAdapter

        dotsIndicatorBanner.attachTo(viewPagerBanner)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search_menu ){
            startActivity(MovieSearchActivity.newIntent(this))
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_discover,menu)
//        return true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onTapMovieFromBanner(movieId : Int) {

       startActivity(MovieDetailsActivity.newIntent(this, movieId = movieId))
    }

    override fun onTapMovieFromShowcase(movieId : Int) {
      startActivity(MovieDetailsActivity.newIntent(this, movieId = movieId))
    }

    override fun onTapMovie(movieId : Int) {
        startActivity(MovieDetailsActivity.newIntent(this, movieId = movieId))
    }

    override fun render(state: MainState) {
        if(state.errorMessage.isNotEmpty()){
            showError(state.errorMessage)
        }
        mBannerAdapter.setNewData(state.nowPlayingMovies)
        mBestPopularMovieListViewPod.setData(state.popularMovies)
        mShowcaseAdapter.setNewData(state.topRatedMovies)
        setUpGenreTabLayout(state.genres)
        mMoviesByGenreViewPod.setData(state.moviesByGenre)
        mActorListViewPod.setData(state.actors)
    }


}