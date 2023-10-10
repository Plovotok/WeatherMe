package ru.plovotok.weatherme.presentation

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.matteobattilana.weather.PrecipType
import dagger.hilt.android.AndroidEntryPoint
import ru.plovotok.weatherme.databinding.ActivityWeatherBinding
import ru.plovotok.weatherme.presentation.base.PrecipRate
import ru.plovotok.weatherme.presentation.base.TypeOfPrecip

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {

    private var _binding: ActivityWeatherBinding? = null

    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    fun showLoading(view : CoordinatorLayout) {
        binding.loadingLayout.loadingLottie.isActivated = true

        if (binding.loadingLayout.root.visibility == View.VISIBLE) return
        binding.loadingLayout.root.visibility = View.VISIBLE

        if (binding.loadingLayout.root.parent != null)
            (binding.loadingLayout.root.parent as ViewGroup).removeView(binding.loadingLayout.root)

        view.addView(binding.loadingLayout.root)
    }

    fun hideLoading(view : CoordinatorLayout) {
        binding.loadingLayout.loadingLottie.isActivated = false
        if (binding.loadingLayout.root.visibility == View.INVISIBLE) return
        binding.loadingLayout.root.visibility = View.INVISIBLE

        view.removeView(binding.loadingLayout.root)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    fun setTimeImage(imageId : Int, precipType: TypeOfPrecip, rate : PrecipRate){


        Glide.with(this)
            .load(imageId)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.timeImage)

//        val drawable = ContextCompat.getDrawable(applicationContext, R.drawable.like)
//        val bitmap =
//            drawable?.let { Bitmap.createBitmap((it.intrinsicWidth*0.2).toInt(), (drawable.intrinsicHeight*0.2).toInt(), Bitmap.Config.ARGB_8888) }
//
//        val canvas = Canvas(bitmap!!)
//        drawable.setBounds(0, 0, canvas.width, canvas.height)
//        drawable.draw(canvas)

        when(precipType) {
            TypeOfPrecip.CLEAR -> binding.weatherView.setWeatherData(PrecipType.CLEAR)
            TypeOfPrecip.RAIN -> binding.weatherView.setWeatherData(PrecipType.RAIN)
            TypeOfPrecip.SNOW -> binding.weatherView.setWeatherData(PrecipType.SNOW)
        }

        when(rate) {
            PrecipRate.CLEAR -> {}
            PrecipRate.LIGHT -> {
                with(binding.weatherView) {
                    fadeOutPercent = 0.6f
                    scaleFactor = 2f
                    emissionRate = 100f
                    speed = 1000
                    resetWeather()
                }
            }
            PrecipRate.MEDIUM -> {
                with(binding.weatherView) {
                    fadeOutPercent = 0.6f
                    scaleFactor = 2f
                    emissionRate = 200f
                    speed = 1500
                    resetWeather()
                }
            }
            PrecipRate.HARD -> {
                with(binding.weatherView) {
                    fadeOutPercent = 0.7f
                    scaleFactor = 3f
                    emissionRate = 120f
                    speed = 2200
                    resetWeather()
                }
            }
        }

    }
}