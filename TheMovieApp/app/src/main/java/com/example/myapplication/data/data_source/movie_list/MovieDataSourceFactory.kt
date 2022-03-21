package com.example.myapplication.data.data_source.movie_list
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.myapplication.data.data_source.movie_list.MovieDataSource
import com.example.myapplication.data.pojo.Movie
import com.example.myapplication.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

//this class mainly used to help generate PagedList
//we need a dataSourceFactory to do that so we will use the DataSourceFactory as a bridge between the DataSource class and generation
//of the PagedList from this DataSource
class MovieDataSourceFactory
@Inject
constructor(private var movieDataSource: MovieDataSource):DataSource.Factory<Int, Movie>()
{
    lateinit var listName: String
    //when creating object of the class MovieDataSource that extends the class DataSource.Factory , the object now is triggered to run all the
    //callback methods
    //the same concept as recyclerview adapter when creating object of the class that extends the recyclerViewAdapter class

    val _networkState:LiveData<NetworkState> =movieDataSource._networkState

    override fun create(): DataSource<Int, Movie> {
        movieDataSource.getData(listName)
        return movieDataSource
    }
    fun clearComposite()
    {
        movieDataSource.removeObservables()
    }
    fun getData(listName:String){
        this.listName=listName
    }
}