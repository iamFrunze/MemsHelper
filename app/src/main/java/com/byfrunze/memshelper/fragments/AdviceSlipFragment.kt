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
import com.byfrunze.memshelper.presenters.PresenterAdviceSlip
import com.byfrunze.memshelper.views.ViewAdviceSlip
import kotlinx.android.synthetic.main.cell_card_with_q_author.*
import kotlinx.android.synthetic.main.cell_card_with_quote.*
import kotlinx.android.synthetic.main.cell_card_with_quote.btn_translate
import kotlinx.android.synthetic.main.cell_card_with_quote.mcv_rus
import kotlinx.android.synthetic.main.cell_card_with_quote.txt_quote_eng
import kotlinx.android.synthetic.main.cell_card_with_quote.txt_quote_rus
import kotlinx.android.synthetic.main.fragment_advice_slip.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

/**
 * A simple [Fragment] subclass.
 */
class AdviceSlipFragment : MvpAppCompatFragment(), ViewAdviceSlip {

    @InjectPresenter
    lateinit var presenter: PresenterAdviceSlip
    lateinit var realmController: RealmController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advice_slip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.loadQuotes()
        btn_next_advice.setOnClickListener {
            presenter.loadQuotes()
        }

        txt_ya.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://translate.yandex.ru/")))
        }

        swipe_refresh_advice.setOnRefreshListener {
            presenter.refreshLoadQuotes()
            swipe_refresh_advice.isRefreshing = false
        }

        btn_translate.setOnClickListener {
            presenter.translateText(txt = txt_quote_eng.text.toString())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        realmController = context as RealmController
    }

    override fun load() {
        cpv_advice_loader.visibility = View.VISIBLE
        btn_next_advice.isEnabled = false
    }

    override fun errorLoad(textError: String?) {
        MaterialDialog(requireContext()).show {
            title(R.string.error)
            message(text = textError)
            icon(R.drawable.ic_uncheck)
        }
    }

    override fun completeLoadingEn(quoteEn: String) {
        mcv_rus.visibility = View.GONE
        cpv_advice_loader.visibility = View.GONE
        txt_quote_eng.isEnabled = true
        txt_quote_eng.text = quoteEn
    }

    override fun completeLoadingRu(quoteRu: String) {
        cpv_advice_loader.visibility = View.GONE
        btn_next_advice.isEnabled = true
        txt_quote_rus.text = quoteRu
    }

    override fun saveQuote() {
        val from = getString(R.string.advice_slip)
        val quoteEn = txt_quote_eng.text.toString()
        val quoteRu = txt_quote_rus.text.toString()
        realmController.transQuote(
            from = from,
            quoteEn = quoteEn,
            quoteRu = quoteRu,
            author = ""
        )
    }

    override fun translateQuote() {
        mcv_rus.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        RealmDB.initDB().close()
    }
}
