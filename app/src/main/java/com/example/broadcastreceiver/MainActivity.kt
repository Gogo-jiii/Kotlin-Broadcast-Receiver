package com.example.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.broadcastreceiver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var implicitIntentFilter: IntentFilter? = null
    private var localBroadcastIntentFilter: IntentFilter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        implicitIntentFilter = IntentFilter()
        implicitIntentFilter!!.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)

        localBroadcastIntentFilter = IntentFilter()
        localBroadcastIntentFilter!!.addAction("local_broadcast")

        binding.button.setOnClickListener {
            val intent = Intent("local_broadcast")
            intent.putExtra("key", "IT wala")
            LocalBroadcastManager.getInstance(this@MainActivity).sendBroadcast(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(implicitBroadCastReceiver, implicitIntentFilter)
        LocalBroadcastManager.getInstance(this).registerReceiver(
            localBroadCastReceiver,
            localBroadcastIntentFilter!!
        )
    }

    override fun onStop() {
        unregisterReceiver(implicitBroadCastReceiver)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localBroadCastReceiver)
        super.onStop()
    }

    private var implicitBroadCastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val sb = StringBuilder()
            sb.append("Action: ${intent.action}".trimIndent())
            sb.append("URI: ${intent.toUri(Intent.URI_INTENT_SCHEME)}".trimIndent())

            val log = sb.toString()
            Log.d("TAG", log)
            Toast.makeText(context, log, Toast.LENGTH_LONG).show()
            binding.textView.text = log
        }
    }

    private var localBroadCastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val name = intent.getStringExtra("key")
            Log.d("TAG", name!!)
            Toast.makeText(context, name, Toast.LENGTH_LONG).show()
            binding.textView.text = name
        }
    }
}