package com.byfrunze.memshelper.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.byfrunze.memshelper.R
import com.byfrunze.memshelper.adapters.HistoryAdapter
import com.byfrunze.memshelper.data.ModelSaveQuotes
import com.byfrunze.memshelper.data.RealmDB
import com.byfrunze.memshelper.helpers.ItemTouchHelperCallback
import com.byfrunze.memshelper.presenters.PresenterHistory
import com.byfrunze.memshelper.views.ViewHistory
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_history.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

/**
 * A simple [Fragment] subclass.
 */
class HistoryFragment : MvpAppCompatFragment(), ViewHistory {

    @InjectPresenter
    lateinit var presenter: PresenterHistory
    lateinit var mAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = HistoryAdapter()
        recycler_view_history.adapter = mAdapter
        recycler_view_history.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_view_history.setHasFixedSize(true)
        presenter.loadHistory()

        val callBack = ItemTouchHelperCallback(mAdapter)
        val touchHelper = ItemTouchHelper(callBack)
        touchHelper.attachToRecyclerView(recycler_view_history)

        search_history.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.searchInfo(query = s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                presenter.searchInfo(query = s.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.searchInfo(query = s.toString())
            }

        })

        swipe_refresh_history.setOnRefreshListener {
            presenter.refreshLoadQuotes()
            swipe_refresh_history.isRefreshing = false
        }
    }

    override fun load() {
        cpv_history_loader.visibility = View.VISIBLE
    }

    override fun showEmptyHistory() {
        cpv_history_loader.visibility = View.GONE
        recycler_view_history.visibility = View.GONE
        txt_empty_history.visibility = View.VISIBLE
    }

    override fun completeLoading(model: RealmResults<ModelSaveQuotes>) {
        cpv_history_loader.visibility = View.GONE
        recycler_view_history.visibility = View.VISIBLE
        mAdapter.setupHistory(model)
    }

    override fun errorLoading(textError: String?) {
        cpv_history_loader.visibility = View.GONE
        txt_empty_history.visibility = View.GONE
        MaterialDialog(requireContext()).show {
            title(R.string.error)
            message(text = textError)
            icon(R.drawable.ic_uncheck)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        RealmDB.initDB().close()
    }
}
