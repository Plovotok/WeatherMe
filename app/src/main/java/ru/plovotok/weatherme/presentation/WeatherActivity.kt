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
import ru.plovotok.weatherme.databinding.ActivityWeatherBinding

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

    fun setTimeImage(imageId : Int){
//        binding.timeImage.setImageResource(imageId)

        Glide.with(this)
            .load(imageId)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.timeImage)

//        binding.weatherView.speed = 2200
//        binding.weatherView.precipType = PrecipType.RAIN
//        binding.weatherView.fadeOutPercent = 100f
//        binding.weatherView.resetWeather()
    }
}