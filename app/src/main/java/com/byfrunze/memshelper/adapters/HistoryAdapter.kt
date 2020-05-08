package com.byfrunze.memshelper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.byfrunze.memshelper.R
import com.byfrunze.memshelper.data.ModelSaveQuotes
import com.byfrunze.memshelper.data.RealmDB
import com.byfrunze.memshelper.helpers.ItemTouchHelperAdapter
import io.realm.RealmResults
import kotlinx.android.synthetic.main.cell_history.view.*


class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    val realm = RealmDB.initDB()
    private val listHistory = ArrayList<ModelSaveQuotes>()

    override fun onItemDismiss(position: Int) {
        realm.executeTransaction { realm ->
            val a =
                realm.where(ModelSaveQuotes::class.java).findAll()
            a.deleteFromRealm(position)
            var i: Long = 1
            for (s in a) {
                s.id = i++
            }
            listHistory.clear()
            listHistory.addAll(a)
        }
        notifyDataSetChanged()
        notifyItemRemoved(position)

    }

    fun setupHistory(listHistory: RealmResults<ModelSaveQuotes>) {
        this.listHistory.clear()
        this.listHistory.addAll(listHistory)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cell_history, parent, false)
        )


    override fun getItemCount() = listHistory.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(model = listHistory[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val item_from = itemView.txt_from_history
        private val item_quoteEn = itemView.txt_quote_en_history
        private val item_quoteRu = itemView.txt_quote_ru_history
        private val item_author = itemView.txt_author_history
        private val item_id = itemView.txt_id_history

        fun bind(model: ModelSaveQuotes) {
            with(model) {
                item_id.text = "$id"
                item_from.text = from
                item_quoteEn.text = quoteEn
                item_quoteRu.text = quoteRu
                item_author.text = author
            }
        }
    }
}