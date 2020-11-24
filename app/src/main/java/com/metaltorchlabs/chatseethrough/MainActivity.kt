package com.metaltorchlabs.chatseethrough

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        initSampleMessages()

    }

    private fun initRecyclerView() {

        chatAdapter = ChatAdapter()
        chatLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        chatRecyclerView = findViewById(R.id.chat_recyclerview)
        chatRecyclerView.apply {
            adapter = chatAdapter
            layoutManager = chatLayoutManager
            setHasFixedSize(true)
        }

    }

    private fun initSampleMessages() {

        chatAdapter.chatMessages = mutableListOf(
            "X",
            "XX",
            "XXX",
            "XXXX",
            "XXXXX",
            "XXXXXX",
            "XXXXXXX",
            "XXXXXXXX",
            "XXXXXXXXX",
            "XXXXXXXXXX",
            "XXXXXXXXXXX",
            "XXXXXXXXXXXX",
            "XXXXXXXXXXXXX",
            "XXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXX",
            "XXXXXXXXXXXXX",
            "XXXXXXXXXXXX",
            "XXXXXXXXXXX",
            "XXXXXXXXXX",
            "XXXXXXXXX",
            "XXXXXXXX",
            "XXXXXXX",
            "XXXXXX",
            "XXXXX",
            "XXXX",
            "XXX",
            "XX",
            "X"
        )

    }

}