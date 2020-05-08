package com.byfrunze.memshelper.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.byfrunze.memshelper.R
import kotlinx.android.synthetic.main.cell_create_text.view.*

class TextCreateAdapter : RecyclerView.Adapter<TextCreateAdapter.ViewHolder>() {

    private val listText = ArrayList<String>()
    var onCloseClickListener: OnCloseClickListener? = null
    var onChooseItemListener: OnChooseItemListener? = null
    var onChangeTextListener: OnChangeTextListener? = null

    interface OnChangeTextListener {
        fun onChangeText(text: String, position: Int)
    }

    fun mySetOnChangeTextListener(onChangeTextListener: OnChangeTextListener) {
        this.onChangeTextListener = onChangeTextListener
    }

    interface OnChooseItemListener {
        fun onChooseItem(position: Int)
    }

    fun setOnChooseItemListener1(onChooseItemListener: OnChooseItemListener) {
        this.onChooseItemListener = onChooseItemListener
    }

    interface OnCloseClickListener {
        fun onClose(position: Int)
    }

    fun setOnCloseListener(onCloseClickListener: OnCloseClickListener) {
        this.onCloseClickListener = onCloseClickListener
    }


    fun setupText(text: String) {
        listText.add(text)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cell_create_text, parent, false)
        )

    override fun getItemCount() = listText.count()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listText[position])
    }

    val arrayCheck = ArrayList<ImageView>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgCheck = itemView.cell_img_check
        private val txtExample = itemView.cell_txt_create
        private val btnDelete = itemView.cell_delete_text_create
        fun bind(text: String) {
            arrayCheck.add(imgCheck)
            txtExample.hint = text
            btnDelete.setOnClickListener {
                onCloseClickListener?.onClose(adapterPosition)
                listText.removeAt(adapterPosition)
                arrayCheck.removeAt(adapterPosition)
                notifyDataSetChanged()
                notifyItemRemoved(adapterPosition)
            }

            imgCheck.setOnClickListener {
                for (a in arrayCheck) {
                    a.setImageResource(R.drawable.ic_remove_circle_outline_white_24dp)
                }
                imgCheck.setImageResource(R.drawable.ic_check_white_24dp)
                onChooseItemListener?.onChooseItem(adapterPosition)
            }

            txtExample.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    onChangeTextListener?.onChangeText(s.toString(), adapterPosition)
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    onChangeTextListener?.onChangeText(s.toString(), adapterPosition)
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    onChangeTextListener?.onChangeText(s.toString(), adapterPosition)
                }
            })

        }
    }
}