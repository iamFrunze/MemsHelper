package com.byfrunze.memshelper.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.byfrunze.memshelper.R
import com.byfrunze.memshelper.data.RealmController
import com.byfrunze.memshelper.data.RealmDB
import com.byfrunze.memshelper.presenters.PresenterMotivation
import com.byfrunze.memshelper.views.ViewMotivation
import kotlinx.android.synthetic.main.fragment_motivation.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

/**
 * A simple [Fragment] subclass.
 */
class MotivationFragment : MvpAppCompatFragment(), ViewMotivation {

    @InjectPresenter
    lateinit var presenter: PresenterMotivation
    lateinit var realmController: RealmController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_motivation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.loadQuotes()
        btn_next_motivation.setOnClickListener {
            presenter.loadQuotes()
        }

        txt_ya.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://translate.yandex.ru/")))
        }

        swipe_refresh_motivation.setOnRefreshListener {
            presenter.refreshLoadQuotes()
            swipe_refresh_motivation.isRefreshing = false
        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        realmController = context as RealmController
    }

    override fun load() {
        cpv_motivation_loader.visibility = View.VISIBLE
        btn_next_motivation.isEnabled = false
    }

    override fun errorLoad(textError: String?) {
        MaterialDialog(requireContext()).show {
            title(R.string.error)
            message(text = textError)
            icon(R.drawable.ic_uncheck)
        }
    }

    override fun completeLoadingEn(quoteEn: String, quoteAuthorEn: String) {
        cpv_motivation_loader.visibility = View.GONE
        btn_next_motivation.isEnabled = true
        txt_quote_eng_motivation.text = quoteEn
        txt_author_eng_motivation.text = quoteAuthorEn
    }

    override fun completeLoadingRu(quoteRu: String, quoteAuthorRu: String) {
        cpv_motivation_loader.visibility = View.GONE
        btn_next_motivation.isEnabled = true
        txt_quote_ru_motivation.text = quoteRu
        txt_author_ru_motivation.text = quoteAuthorRu
    }

    override fun saveQuote() {
        val from = getString(R.string.menu_motivation)
        val quoteEn = txt_quote_eng_motivation.text.toString()
        val quoteRu = txt_quote_ru_motivation.text.toString()
        val author = txt_author_eng_motivation.text.toString()
        realmController.transQuote(from = from, quoteEn = quoteEn, quoteRu = quoteRu, author = author)
    }

    override fun onDestroy() {
        super.onDestroy()
        RealmDB.initDB().close()
    }
}
