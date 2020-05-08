package com.byfrunze.memshelper.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.byfrunze.memshelper.R
import kotlinx.android.synthetic.main.cell_text_fonts.view.*

class FontAdapter : RecyclerView.Adapter<FontAdapter.ViewHolder>() {

    private val listFont = ArrayList<String>()
    var onChangeTextFontListener: OnChangeTextFontListener? = null

    interface OnChangeTextFontListener {
        fun onChangeFontText(create: Typeface)
    }

    fun mySetOnChangeTextFontListener(onChangeTextListener: OnChangeTextFontListener) {
        this.onChangeTextFontListener = onChangeTextListener
    }

    fun setupFonts(list: ArrayList<String>) {
        listFont.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cell_text_fonts, parent, false)
        )

    override fun getItemCount() = listFont.count()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFont[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val llFont = itemView.ll_font
        private val txtExample = itemView.cell_text_example
        private val txtFont = itemView.cell_text_font
        fun bind(fontName: String) {
            txtExample.text = "Example text"
            txtFont.text = fontName
            txtExample.typeface = Typeface.create(fontName, Typeface.NORMAL)
            llFont?.setOnClickListener{
                onChangeTextFontListener?.onChangeFontText(Typeface.create(fontName, Typeface.NORMAL))
            }
        }
    }
}