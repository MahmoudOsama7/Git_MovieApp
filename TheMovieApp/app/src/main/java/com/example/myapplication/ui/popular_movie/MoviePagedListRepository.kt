package com.example.myapplication.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.myapplication.data.api.TheMovieDBClient.Companion.POSTS_PER_PAGE
import com.example.myapplication.data.repository.popular_movie_list_data_source.MovieDataSourceFactory
import com.example.myapplication.data.repository.NetworkState
import com.example.myapplication.data.pojo.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository(private val compositeDisposable: CompositeDisposable) {

    private lateinit var moviePagedList: LiveData<PagedList<Movie>>
    private lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList (listName:String) : LiveData<PagedList<Movie>>
    {
        moviesDataSourceFactory = MovieDataSourceFactory(listName,compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POSTS_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()
        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return moviesDataSourceFactory._networkState
    }
    fun clearComposite()
    {
        moviesDataSourceFactory.clearComposite()
    }

}