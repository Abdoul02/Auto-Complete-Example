package com.abdoul.booksearch

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdoul.booksearch.adapters.BookAdapter
import com.abdoul.booksearch.adapters.SearchCompleteAdapter
import com.abdoul.booksearch.model.Item
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var dialog: Dialog
    lateinit var searchCompleteAdapter: SearchCompleteAdapter
    lateinit var bookAdapter: BookAdapter
    lateinit var mainViewModel: MainViewModel

    var delay: Long = 1000 // 1 seconds after user stops typing

    var last_text_edit: Long = 0
    var handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        bookAdapter = BookAdapter()
        searchCompleteAdapter = SearchCompleteAdapter(this, R.layout.auto_complete_layout)
        edtSearch.setOnClickListener { showDialog() }
        mainViewModel.items.observe(this, Observer {
            it?.let {
                logData(it)
            }
        })

/*        autoSearch.threshold = 3
        autoSearch.setAdapter(searchCompleteAdapter)
        autoSearch.setLoadingIndicator(pb_loading_indicator)
        autoSearch.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val book = parent.getItemAtPosition(position) as Item
                autoSearch.setText(book.volumeInfo.title)
            }*/
        basicACT.threshold = 3
        basicACT.setAdapter(searchCompleteAdapter)
        // autoSearch.setLoadingIndicator(pb_loading_indicator)
        basicACT.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val book = parent.getItemAtPosition(position) as Item
                basicACT.setText(book.volumeInfo.title)
            }


        basicACT.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (basicACT.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay)
                    pb_loading_indicator.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handler.removeCallbacks(input_finish_checker);
            }

        })
    }

    private val input_finish_checker = Runnable {
        if (System.currentTimeMillis() > last_text_edit + delay - 500) {
            pb_loading_indicator.visibility = View.GONE
        }
    }

    private fun logData(items: List<Item>) {
        bookAdapter.setData(items)
    }

    private fun showDialog() {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_spinner)

        val bookRecyclerView = dialog.findViewById(R.id.recyclerView) as RecyclerView
        bookRecyclerView.adapter = bookAdapter
        bookRecyclerView.layoutManager = LinearLayoutManager(this)

        val bookSearch = dialog.findViewById(R.id.edtAutoSearch) as EditText

        bookSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable) {
                if (text.isEmpty()) {
                    bookAdapter.clearData()
                }
                mainViewModel.onTextInputStateChanged(bookSearch.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        dialog.show()
    }
}
