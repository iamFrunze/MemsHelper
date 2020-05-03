package com.byfrunze.memshelper.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.byfrunze.memshelper.R
import com.byfrunze.memshelper.presenters.PresenterChackNorris
import com.byfrunze.memshelper.views.ViewChackNorris
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_chack_norris.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

/**
 * A simple [Fragment] subclass.
 */
class ChackNorrisFragment : MvpAppCompatFragment(), ViewChackNorris {

    @InjectPresenter
    lateinit var presenter: PresenterChackNorris

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
        btn_next_chack.setOnClickListener {
            presenter.loadQuotes()
        }

        txt_ya.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://translate.yandex.ru/")))
        }
    }

    override fun load() {
        cpv_chack_loader.visibility = View.VISIBLE
        btn_next_chack.isEnabled = false
    }

    override fun errorLoad(textError: String?) {
        MaterialDialog(requireContext()).show {
            title(R.string.error)
            message(text = textError)
            icon(R.drawable.ic_uncheck)
        }
    }

    override fun completeLoadingEn(icon: String, quoteEn: String) {
        cpv_chack_loader.visibility = View.GONE
        txt_author_eng_chack.isEnabled = true
        txt_author_eng_chack.text = quoteEn
        txt_author_eng_chack.text = getString(R.string.chack_norris_en)
        Picasso.get()
            .load(icon)
            .placeholder(R.drawable.ic_uncheck)
            .into(img_chack)
    }

    override fun completeLoadingRu(quoteRu: String) {
        cpv_chack_loader.visibility = View.GONE
        btn_next_chack.isEnabled = true
        txt_quote_ru_chack.text = quoteRu
        txt_author_ru_chack.text = getString(R.string.chack_norris_ru)
    }
}
