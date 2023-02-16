package com.example.socketapplicationexample

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socketapplicationexample.Const.Companion.socketURL
import com.example.socketapplicationexample.adpter.RecyclerViewAdapter
import com.example.socketapplicationexample.databinding.ActivityTeacherBinding
import com.example.socketapplicationexample.modelclass.NameValuePairs
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.net.URISyntaxException
import kotlin.math.log


class TeacherActivity : AppCompatActivity() {
    private var mSocket: Socket? = null
    private lateinit var binding: ActivityTeacherBinding

    var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher)
        recyclerViewCode()
        connectionSocket()
        binding.sendmassageteacherBtn.setOnClickListener {

            val dataforuser = binding.chattinglayoutStudent.text.toString()
            Log.d(TAG, "onCreate:$dataforuser")

            var hashMap: HashMap<String, String> = HashMap<String, String>()
            hashMap.put("sender_id", "1162")
            hashMap.put("receiver_id", "1167")
            hashMap.put("sender_type", "teacher")
            hashMap.put("receiver_type", "school")
            hashMap.put("message",dataforuser)
            val gson = Gson()
            val obj = JSONObject(gson.toJson(hashMap))
            mSocket?.emit("send_message",obj)
            Log.d(ContentValues.TAG, "onCreate  For Teacher: $obj")
        }
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
        Log.e("Socket-->", "ConnectTeacher")
        val data: HashMap<String, String> = HashMap()
        data["user_id"] = "1162"
        val gson = Gson()
        val obj = JSONObject(gson.toJson(data))
        mSocket!!.emit("connect_user", obj)
    }

    private val disConnect = Emitter.Listener {
        Log.e("Socket-->", "Dis-ConnectTeacher")
    }

    private val receiveMessage = Emitter.Listener {
        Log.e("Socket-->Received", "Data" + it)
        val receive: Any? = it[0]
        //  Toast.makeText(baseContext, "onReceiveMessage ... ${receive.toString()}", Toast.LENGTH_LONG)
        //    .show()
        Log.d(
            ContentValues.TAG,
            "0000000000000000000000000000000000000000000000000000000000:$receive"
        )
        val data = it[0]
        val chatModel = gson.fromJson(data.toString(), NameValuePairs::class.java)
        Log.e("TAG", ": " + chatModel!!.message)
        binding.setdatateacherdata.setText(chatModel.message)

    }

    private val userConnect = Emitter.Listener {
        Log.e("Socket-->Received", "Data" + it)
        //  Toast.makeText(this, "User Connect", Toast.LENGTH_SHORT).show()

    }

    private fun recyclerViewCode() {
        val massageArrayList: ArrayList<NameValuePairs> = ArrayList<NameValuePairs>()
        massageArrayList.add(NameValuePairs("a", 2, "Hii", "d", "d", 2, "d", "d", "d", "d"))
        massageArrayList.add(NameValuePairs("a", 2, "helloo", "d", "d", 2, "d", "d", "d", "d"))
        massageArrayList.add(NameValuePairs("a", 2, "String", "d", "d", 2, "d", "d", "d", "d"))
        massageArrayList.add(NameValuePairs("a", 2, "tttttt", "d", "d", 2, "d", "d", "d", "d"))
        massageArrayList.add(NameValuePairs("a", 2, "pppp", "d", "d", 2, "d", "d", "d", "d"))

        binding.teacherRecyclerview.layoutManager = LinearLayoutManager(this)
        val adapter = RecyclerViewAdapter(this, massageArrayList)
        Log.d("modeldata", "recyclerViewCode: ")
        binding.teacherRecyclerview.adapter = adapter
    }
}