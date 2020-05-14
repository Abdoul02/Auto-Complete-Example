package com.abdoul.booksearch.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.abdoul.booksearch.R
import com.abdoul.booksearch.data.ApiUtils
import com.abdoul.booksearch.model.Book
import com.abdoul.booksearch.model.Item
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class SearchCompleteAdapter(private val context: Context, private val resourceId: Int) :
    BaseAdapter(),
    Filterable {

    private val autoCompletePublishSubject = PublishRelay.create<String>()
    private var autoCompleteDisposable: Disposable? = null

    private var resultsList = ArrayList<Item>()

    override fun getCount() = resultsList.size

    override fun getItem(position: Int) = resultsList[position]
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resourceId, parent, false)
        val txtName = view.findViewById<TextView>(R.id.txtName)
        val txtPublisher = view.findViewById<TextView>(R.id.txtPublisher)
        txtName.text = getItem(position).volumeInfo.title
        txtPublisher.text = getItem(position).volumeInfo.publisher
        return view
    }

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            if (constraint.toString().length > 3) {
                autoCompleteDisposable =
                    ApiUtils.getBookApiService().getBookData(constraint.toString(), 5)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ response ->
                            filterResults.values = response.items
                            filterResults.count = response.items.size
                            resultsList.clear()
                            resultsList.addAll(response.items)
                        }, { error -> Log.d("AutoSearch", "${error.message}") })

                Log.d("AutoSearch", "Size: ${resultsList.size}")
            }
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (!resultsList.isNullOrEmpty()) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
            /* if ((results != null) && (results.count > 0)) {
            resultsList.addAll(results.values as ArrayList<Item>)
            Log.d("publishResults", "1st ${resultsList[0].volumeInfo}")
            Log.d("publishResults", "2nd ${resultsList[1].volumeInfo}")
            notifyDataSetChanged()
        } else {
            notifyDataSetInvalidated()
        }*/
        }
    }
}