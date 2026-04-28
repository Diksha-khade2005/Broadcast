package com.example.today

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var airplaneModeReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)

        updateAirplaneModeStatus(isAirplaneModeOn(this))

        airplaneModeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                val isEnabled = intent?.getBooleanExtra("state", false) ?: false

                updateAirplaneModeStatus(isEnabled)

                val message = if (isEnabled) "Airplane Mode is ON" else "Airplane Mode is OFF"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateAirplaneModeStatus(isOn: Boolean) {
        textView.text = if (isOn) "Airplane Mode is ON" else "Airplane Mode is OFF"
    }


    private fun isAirplaneModeOn(context: Context): Boolean {
        return Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(airplaneModeReceiver, filter)
    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(airplaneModeReceiver)
    }
}
