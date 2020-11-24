package com.metaltorchlabs.chatseethrough

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    var chatMessages: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val chatMessageView = ChatMessageView(parent.context)
        val layout = FrameLayout(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            addView(chatMessageView)
        }
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindChatMessage(chatMessages[position])
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindChatMessage(chatMessage: String) {

            val container = itemView as FrameLayout

            if (container.childCount > 0) {

                // Get the existing ChatMessageView in the layout and rebuild it using the string
                // that it should be displaying.
                val chatMessageView = container.getChildAt(0) as ChatMessageView
                chatMessageView.build(chatMessage)

                // Since the height of the overall layout may have changed from when this
                // ChatMessageView may have been recycled, request it to be laid out again. If this
                // isn't requested, recycled items will receive an incorrect height and the chat
                // messages will appear to overlap one another.
                chatMessageView.requestLayout()

            }

        }

    }

}