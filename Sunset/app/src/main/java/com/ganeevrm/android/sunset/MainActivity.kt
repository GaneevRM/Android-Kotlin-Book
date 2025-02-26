package com.ganeevrm.android.sunset

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.ganeevrm.android.sunset.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isSunset = true
    private var currentAnimatorSet: AnimatorSet? = null

    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.blue_sky)
    }
    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }
    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scene.setOnClickListener {
            toggleAnimation()
        }
    }

    private fun toggleAnimation() {
        if (currentAnimatorSet?.isRunning == true) {
            currentAnimatorSet?.cancel()
        }
        if (isSunset) {
            startSunsetAnimation()
        } else {
            startSunriseAnimation()
        }
        isSunset = !isSunset
    }

    private fun startSunsetAnimation() {
        val sunYStart = binding.sun.top.toFloat()
        val sunYEnd = binding.sky.height.toFloat()

        val sunReflectionYStart = binding.sunReflection.top.toFloat()
        val sunReflectionYEnd = sunReflectionYStart - (sunYEnd - sunYStart)

        val heightAnimator = ObjectAnimator
            .ofFloat(binding.sun, "y", sunYStart, sunYEnd + 60)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", blueSkyColor, sunsetSkyColor)
            .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunsetSkyColor, nightSkyColor)
            .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        val sunRotationAnimator = ObjectAnimator
            .ofFloat(binding.sun, "rotation", 0f, 360f)
            .setDuration(5000)
        sunRotationAnimator.repeatCount = ObjectAnimator.INFINITE

        val sunReflectionRotationAnimator = ObjectAnimator
            .ofFloat(binding.sunReflection, "rotation", 0f, 360f)
            .setDuration(5000)
        sunReflectionRotationAnimator.repeatCount = ObjectAnimator.INFINITE

        val sunReflectionHeightAnimator = ObjectAnimator
            .ofFloat(binding.sunReflection, "y", sunReflectionYStart, sunReflectionYEnd - 140)
            .setDuration(3000)
        sunReflectionHeightAnimator.interpolator = AccelerateInterpolator()

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
            .with(sunsetSkyAnimator)
            .before(nightSkyAnimator)
            .with(sunRotationAnimator)
            .with(sunReflectionHeightAnimator)
            .with(sunReflectionRotationAnimator)

        animatorSet.start()
        currentAnimatorSet = animatorSet
    }

    private fun startSunriseAnimation() {
        val sunYStart = binding.sun.top.toFloat()
        val sunYEnd = binding.sky.height.toFloat()

        val sunReflectionYEnd = binding.sunReflection.top.toFloat()
        val sunReflectionYStart = sunReflectionYEnd - (sunYEnd - sunYStart)

        val heightAnimator = ObjectAnimator
            .ofFloat(binding.sun, "y", sunYEnd, sunYStart)
            .setDuration(3000)
        heightAnimator.interpolator = DecelerateInterpolator()

        val sunriseSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", nightSkyColor, sunsetSkyColor)
            .setDuration(3000)
        sunriseSkyAnimator.setEvaluator(ArgbEvaluator())

        val blueSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunsetSkyColor, blueSkyColor)
            .setDuration(1500)
        blueSkyAnimator.setEvaluator(ArgbEvaluator())

        val sunRotationAnimator = ObjectAnimator
            .ofFloat(binding.sun, "rotation", 0f, 360f)
            .setDuration(5000)
        sunRotationAnimator.repeatCount = ObjectAnimator.INFINITE

        val sunReflectionRotationAnimator = ObjectAnimator
            .ofFloat(binding.sunReflection, "rotation", 0f, 360f)
            .setDuration(5000)
        sunReflectionRotationAnimator.repeatCount = ObjectAnimator.INFINITE

        val sunReflectionHeightAnimator = ObjectAnimator
            .ofFloat(binding.sunReflection, "y", sunReflectionYStart, sunReflectionYEnd)
            .setDuration(3000)
        sunReflectionHeightAnimator.interpolator = DecelerateInterpolator()


        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
            .with(sunriseSkyAnimator)
            .before(blueSkyAnimator)
            .with(sunRotationAnimator)
            .with(sunReflectionHeightAnimator)
            .with(sunReflectionRotationAnimator)

        animatorSet.start()
        currentAnimatorSet = animatorSet
    }
}