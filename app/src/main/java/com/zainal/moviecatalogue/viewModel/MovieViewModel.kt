package com.zainal.moviecatalogue.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.zainal.moviecatalogue.model.Movie
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MovieViewModel : ViewModel() {

    companion object {
        private const val API_KEY = "9948fd2e03f89290b464bd94b560b078"
    }
    val listMovies = MutableLiveData<ArrayList<Movie>>()

    internal fun setMovie() {
        val client = AsyncHttpClient()
        val listItems = ArrayList<Movie>()
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=$API_KEY&language=en-US"
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int,headers: Array<Header>?,responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
                        val movieItems = Movie()
                        movieItems.id = movie.getInt("id")
                        movieItems.original_name = movie.getString("original_title")
                        movieItems.overview = movie.getString("overview")
                        movieItems.release_date = movie.getString("release_date")
                        val poster = movie.getString("poster_path")
                        movieItems.poster = "https://image.tmdb.org/t/p/w154$poster"
                        listItems.add(movieItems)
                    }
                    listMovies.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }
            override fun onFailure(statusCode: Int,headers: Array<Header>?,responseBody: ByteArray?,error: Throwable?) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    internal fun getMovie(): LiveData<ArrayList<Movie>> {
        return listMovies
    }
}