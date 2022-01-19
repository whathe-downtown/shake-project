package org.techtown.air.pollution.shake_project

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import org.techtown.air.pollution.shake_project.databinding.ActivityMainBinding
import kotlin.math.sqrt
import render.animations.*
class MainActivity : AppCompatActivity() , SensorEventListener{

    val TAG: String = "로그"
    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!


    private var accel:Float =0.0f
    private var accelCurrent: Float = 0.0f
    private var accelLast: Float = 0.0f

    //센서매니저
    private lateinit var sensorManager: SensorManager

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
       this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

       accel = 10f
       accelCurrent = SensorManager.GRAVITY_EARTH
       accelLast = SensorManager.GRAVITY_EARTH
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d(TAG, "onAccuracyChanged: called")
    }
    override fun onSensorChanged(event: SensorEvent?) {
        val x: Float = event?.values?.get(0) as Float
        val y: Float = event?.values?.get(1) as Float
        val z: Float = event?.values?.get(2) as Float

        binding.xText.text = "X:" + x.toInt().toString()
        binding.yText.text = "X:" + y.toInt().toString()
        binding.zText.text = "X:" + z.toInt().toString()

        accelLast = accelCurrent

        accelCurrent = sqrt((x*x+ y*y + z*z).toDouble()).toFloat()
        
        val delta: Float =accelCurrent - accelLast
        
        accel = accel * 0.9f + delta

        if(accel> 30){
            Log.d(TAG, "onSensorChanged: 흔들었음")
            Log.d(TAG, "onSensorChanged: MainActivity- accel : ${accel}")

            binding.faceImg.setImageResource(R.drawable.ic_pyetrosyan_smile)
            // Create Render Class
            val render = Render(this)

            // Set Animation
            render.setAnimation(Bounce().InDown(binding.faceImg))
            render.start()
            Handler().postDelayed({
                // Declare TextView

            },1000)
        }else{
            binding.faceImg.setImageResource(R.drawable.ic_babavanga_ugly)
        }

    }


    override fun onResume() {
        Log.d(TAG, "onResume:  called")
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause: called")
        sensorManager.unregisterListener(this)
        super.onPause()
    }
    
}
