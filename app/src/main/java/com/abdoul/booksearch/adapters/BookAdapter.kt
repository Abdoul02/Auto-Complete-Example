package com.abdoul.booksearch.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abdoul.booksearch.R
import com.abdoul.booksearch.model.Item

class BookAdapter : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    private var items: ArrayList<Item> = ArrayList()

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var bookCover: ImageView = itemView.findViewById(R.id.imgBookImage)
        private var bookTitle: TextView = itemView.findViewById(R.id.txtName)
        private var publisher: TextView = itemView.findViewById(R.id.txtPublisher)

        fun bind(item: Item) {
            bookTitle.text = item.volumeInfo.title
            publisher.text = item.volumeInfo.publisher
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.auto_complete_layout, parent, false)

        return BookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun clearData() {
        this.items.clear()
        notifyDataSetChanged()
    }

    fun setData(listOfItems: List<Item>) {
        if (this.items.isNotEmpty()) items.clear()
        this.items.addAll(listOfItems)
        notifyDataSetChanged()
    }
}