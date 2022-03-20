package com.example.myapplication.data.api
object TheMovieDBClient
{
    const val FIRST_PAGE=1
    const val POSTS_PER_PAGE=20
     const val API_KEY = "ecbbdd15ebc92cd950aa05bcd6872e17"
     const val BASE_URL = "https://api.themoviedb.org/3/"
    const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342/"
//this is the base_url to get any poster link
//to use it for ex to get spider man poster : https://image.tmdb.org/t/p/w342/nogV4th2P5QWYvQIMiWHj4CFLU9.jpg
//creating a object TheMovieDBClient which contain a method that generates the retrofit api we will use
//however we'll build an interceptor by using okHTTP library as to be the client used by the retrofit api
}