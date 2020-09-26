package com.example.openweatherapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.openweatherapp.databinding.ActivityMainBinding
import com.example.openweatherapp.weatherData.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private var cityName = "tyler"
    private val API = "4cba5dd445c51e36ffd7fdb3568ab036"
    private lateinit var binding:ActivityMainBinding
   private lateinit var context:Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_main)
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this)
        context = this
        weatherInfo

        binding.textfield.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {

                            cityName= binding.textfield.text.toString()
                            weatherInfo
                            return true
                        }
                        else -> {
                        }
                    }
                }
                return false
            }
        })
    }

    private val weatherInfo: Unit
        get() {
            WeatherApiService.WeatherApi.retrofitService.getWeather(cityName,API,"metric").enqueue(object:Callback<Weather>{
                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                    if(response.code()==404){
                        Toast.makeText(context,"Please enter a valid city",Toast.LENGTH_LONG)
                    }
                    else if (!(response.isSuccessful)){
                        Toast.makeText(context,response.code(),Toast.LENGTH_LONG).show()
                    }

                    val weather = response.body();
                    if (weather?.name == null){
                        Toast.makeText(context,"Please enter a valid city",Toast.LENGTH_LONG)
                    }
                    Log.d("response", weather.toString())
                    val wind = weather?.wind?.speed

                    val date = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                        Date((weather?.dt?.toLong() ?:1 ) *1000)
                    )
                    val temp = weather?.main?.temp.toString() + "°C"
                    val tempMin = "Min Temp: " + weather?.main?.temp_min.toString() + "°C"
                    val tempMax = "Max Temp: " +weather?.main?.temp_max.toString() + "°C"
                    val feelsLike = "Feels Like: " +weather?.main?.feels_like.toString() + "°C"
                    val pressure = weather?.main?.pressure.toString() + "hPa"
                    val humidity = weather?.main?.humidity.toString()
                    val sunrise = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(
                        Date(
                            (weather?.sys?.sunrise?.toLong() ?: 1) * 1000
                        )
                    )
                    val sunset = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(
                        Date(
                            (weather?.sys?.sunset?.toLong() ?: 1) * 1000
                        )
                    )
                    val weatherDescription = weather?.weather?.get(0)?.description
                    val address = weather?.name + ", " + weather?.sys?.country

////                    populating the data
                    binding.address.text = address
                    binding.date.text = date
                    binding.weatherDescription.text = weatherDescription
                    binding.temp.text = temp
                    binding.feelsLike.text = feelsLike
                    binding.tempMin.text = tempMin
                    binding.tempMax.text = tempMax
                    binding.sunrise.text = sunrise
                    binding.sunset.text = sunset
                    binding.wind.text = "${wind.toString()}m/s"
                    binding.pressure.text = pressure
                    binding.humidity.text = humidity
                }

                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    Toast.makeText(context,"Something went wrong while fetching the data",Toast.LENGTH_LONG)
                }

            })
        }


}


