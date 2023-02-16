package com.example.socketapplicationexample

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socketapplicationexample.Const.Companion.socketURL
import com.example.socketapplicationexample.adpter.RecyclerViewAdapter
import com.example.socketapplicationexample.databinding.ActivityStudentBinding
import com.example.socketapplicationexample.modelclass.NameValuePairs
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.net.URISyntaxException


class SchoolActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentBinding

    companion object {
        var massageArrayList = ArrayList<NameValuePairs>()
    }

    private var mSocket: Socket? = null
    var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student)
        recyclerViewCode()
        connectionSocket()

        binding.sendteacherBtn.setOnClickListener {
            val dataforuser = binding.chattinglayoutStudent.text.toString()
            var hashMap: HashMap<String, String> = HashMap<String, String>()
            hashMap.put("sender_id", "1167")
            hashMap.put("receiver_id", "1162")
            hashMap.put("sender_type", "school")
            hashMap.put("receiver_type", "teacher")
            hashMap.put("message", dataforuser)
            val gson = Gson()
            val obj = JSONObject(gson.toJson(hashMap))
            mSocket?.emit("send_message", obj)
            Log.d(TAG, "This is For School: $obj")
        }
    }

    override fun onResume() {
        super.onResume()
        //   recyclerViewCode()
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
            Toast.makeText(this, "problems", Toast.LENGTH_SHORT).show()
        }
    }

    private val connect = Emitter.Listener {
        Log.e("Socket-->", "ConnectSchool")
        val data: HashMap<String, String> = HashMap()
        data["user_id"] = "1167"
        val gson = Gson()
        val obj = JSONObject(gson.toJson(data))
        mSocket!!.emit("connect_user", obj)
    }

    private val disConnect = Emitter.Listener {
        Log.e("Socket-->", "Dis-Connectschool")
    }

    private val receiveMessage = Emitter.Listener {
        Log.e("Socket-->Received", "Data" + it)
        runOnUiThread {
            val receive: Any? = it[0]
            Toast.makeText(
                baseContext,
                "onReceiveMessage ... ${receive.toString()}",
                Toast.LENGTH_LONG
            )
                .show()
            Log.d(TAG, ":$receive")

            val data = it[0]
            val chatModel = gson.fromJson(data.toString(), NameValuePairs::class.java)



            binding.setdatainstudent.setText(chatModel.message)
            Log.e("size", "recyclerViewCode and Size: " + massageArrayList.size)
            Log.e("TAG", ": " + chatModel!!.message)
           massageArrayList.add(NameValuePairs("", 0, chatModel.message, "", "", 0, "", "", "", ""))
            // massageArrayList.add(NameValuePairs("", 0, aaaa, "", "", 0, "", "", "", ""))
            Log.d(TAG, " _________________________________________________ ${massageArrayList.size}: ")
            //  Log.d(TAG, "000000000000000000000:$aaaa ")
        }
    }

    private val userConnect = Emitter.Listener {
        Log.e("Socket-->Received data ", "Data" + it)
        //     Toast.makeText(this, "User Connect", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun recyclerViewCode() {
        binding.studentRecyclerview.layoutManager = LinearLayoutManager(this)
        Log.e("size", "recyclerViewCode for size: " + massageArrayList.size)
        val adapter = RecyclerViewAdapter(this, massageArrayList)
        binding.studentRecyclerview.adapter = adapter
       // massageArrayList.add(NameValuePairs("a", 1, "Hii", "d", "d", 1, "d", "d", "d", "d"))
        //  massageArrayList.add(NameValuePairs("a", 1, "Hii", "d", "d", 1, "d", "d", "d", "d"))
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        mSocket?.disconnect()
        super.onDestroy()
    }
}