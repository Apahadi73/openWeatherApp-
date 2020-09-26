package com.example.openweatherapp

import com.example.openweatherapp.weatherData.Weather
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val url = "http://api.openweathermap.org/data/2.5/"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

val retrofit: Retrofit =  Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(url).build()

interface WeatherApiService {
    @GET("weather")
    fun getWeather(@Query("q") cityName:String,@Query("appid") API:String,@Query("units") units:String):
            Call<Weather>

    object WeatherApi{
        val retrofitService: WeatherApiService by lazy {
            retrofit.create(WeatherApiService::class.java)
        }
    }
}