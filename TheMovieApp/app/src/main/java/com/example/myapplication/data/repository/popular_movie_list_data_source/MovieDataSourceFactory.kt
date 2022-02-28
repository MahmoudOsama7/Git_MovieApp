package com.example.myapplication.data.repository.popular_movie_list_data_source
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.myapplication.data.pojo.Movie
import com.example.myapplication.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

//this class mainly used to help generate PagedList
//we need a dataSourceFactory to do that so we will use the DataSourceFactory as a bridge between the DataSource class and generation
//of the PagedList from this DataSource
class MovieDataSourceFactory(private val listName:String,private val compositeDisposable: CompositeDisposable): DataSource.Factory<Int, Movie>()
{
    //when creating object of the class MovieDataSource that extends the class DataSource.Factory , the object now is triggered to run all the
    //callback methods
    //the same concept as recyclerview adapter when creating object of the class that extends the recyclerViewAdapter class
    private var  movieDataSource = MovieDataSource(listName,compositeDisposable)
    val _networkState:LiveData<NetworkState> =movieDataSource._networkState

    override fun create(): DataSource<Int, Movie> {
        return movieDataSource
    }
    fun clearComposite()
    {
        movieDataSource.removeObservables()
    }
}