package com.byfrunze.memshelper.fragments

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.afollestad.materialdialogs.MaterialDialog
import com.byfrunze.memshelper.R
import com.byfrunze.memshelper.data.RealmController
import com.byfrunze.memshelper.data.RealmDB
import com.byfrunze.memshelper.presenters.PresenterChackNorris
import com.byfrunze.memshelper.views.ViewChackNorris
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cell_card_with_q_author.*
import kotlinx.android.synthetic.main.fragment_chack_norris.*
import kotlinx.android.synthetic.main.fragment_chack_norris.txt_ya
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

/**
 * A simple [Fragment] subclass.
 */
class ChackNorrisFragment : MvpAppCompatFragment(), ViewChackNorris {

    @InjectPresenter
    lateinit var presenter: PresenterChackNorris
    lateinit var realmController: RealmController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chack_norris, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.loadQuotes()
        btn_next.setOnClickListener {
            presenter.loadQuotes()
        }

        txt_ya.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://translate.yandex.ru/")))
        }

        swipe_refresh_chack.setOnRefreshListener {
            presenter.refreshLoadQuotes()
            swipe_refresh_chack.isRefreshing = false
        }

        btn_translate.setOnClickListener {
            presenter.translateText(
                txt = txt_quote_eng.text.toString()
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        realmController = context as RealmController
    }

    override fun load() {
        cpv_chack_loader.visibility = View.VISIBLE
        btn_next.isEnabled = false
    }

    override fun errorLoad(textError: String?) {
        MaterialDialog(requireContext()).show {
            title(R.string.error)
            message(text = textError)
            icon(R.drawable.ic_uncheck)
        }
    }

    override fun completeLoadingEn(icon: String, quoteEn: String) {
        mcv_rus.visibility = View.GONE
        mcv_eng.visibility = View.VISIBLE
        cpv_chack_loader.visibility = View.GONE
        btn_next.isEnabled = true
        txt_quote_eng.text = quoteEn
        txt_author_eng.text = getString(R.string.chack_norris_en)
        Picasso.get()
            .load(icon)
            .placeholder(R.drawable.ic_uncheck)
            .into(img_chack)
        val anim = ObjectAnimator.ofFloat(mcv_eng, "translationX", -1000f, 0f)
        anim.duration = 300
        anim.interpolator = FastOutSlowInInterpolator()
        anim.start()
    }

    override fun completeLoadingRu(quoteRu: String) {
        cpv_chack_loader.visibility = View.GONE
        btn_next.isEnabled = true
        txt_quote_rus.text = quoteRu
        txt_author_rus.text = getString(R.string.chack_norris_ru)

    }

    override fun saveQuote() {
        val from = getString(R.string.chack_norris_ru)
        val quoteEn = txt_quote_eng.text.toString()
        val quoteRu = txt_quote_rus.text.toString()
        val author = txt_author_eng.text.toString()
        realmController.transQuote(
            from = from,
            quoteEn = quoteEn,
            quoteRu = quoteRu,
            author = author
        )
    }

    override fun translateQuote() {
        mcv_rus.visibility = View.VISIBLE
        val anim = ObjectAnimator.ofFloat(mcv_rus, "translationX", -1000f, 0f)
        anim.duration = 300
        anim.interpolator = FastOutSlowInInterpolator()
        anim.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        RealmDB.initDB().close()
    }
}
