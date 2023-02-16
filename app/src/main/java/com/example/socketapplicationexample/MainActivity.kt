package com.example.socketapplicationexample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.socketapplicationexample.Const.Companion.socketURL
import com.example.socketapplicationexample.databinding.ActivityMainBinding
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.net.URISyntaxException


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mSocket: Socket? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.socketconnectBtn.setOnClickListener {
            val data: HashMap<String, String> = HashMap()
            data["user_id"] = "1165"
            val gson = Gson()
            val obj = JSONObject(gson.toJson(data))
            mSocket!!.emit("user_connect", obj)
        }

        binding.teacheridBtn.setOnClickListener {
            startActivity(Intent(this, TeacherActivity::class.java))
        }

        binding.studentBtn.setOnClickListener {
            startActivity(Intent(this, SchoolActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        connectionSocket()
    }

    private fun connectionSocket() {
        try {
            mSocket = IO.socket(socketURL)
            mSocket!!.connect()
            mSocket?.on(Socket.EVENT_CONNECT, connect)
            mSocket?.on(Socket.EVENT_DISCONNECT, disConnect)
            mSocket?.on("receive_message", receiveMessage)
            mSocket?.on("connect_user", userConnect)

        } catch (e: URISyntaxException) {
            //Toast.makeText(this, "problems", Toast.LENGTH_SHORT).show()
        }
    }

    private val connect = Emitter.Listener {
        Log.e("Socket-->", "Connect")
    }

    private val disConnect = Emitter.Listener {
        Log.e("Socket-->", "Dis-Connect")
    }

    private val receiveMessage = Emitter.Listener {
        Log.e("Socket-->Received", "Data" + it)
    }

    private val userConnect = Emitter.Listener {
        Log.e("Socket-->Received", "Data" + it)
    }

    override fun onPause() {
        super.onPause()
        mSocket?.disconnect()
    }

    override fun onStop() {
        super.onStop()
        mSocket?.off("receive_message", receiveMessage)
        mSocket?.off("connect_user", userConnect)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}