package com.example.wishlist;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class DesejoAdapter(val lista: MutableList<Desejo>): RecyclerView.Adapter<DesejoAdapter.DesejoViewHolder>() {
    var listener: OnItemClickRecycleView? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DesejoViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.recycleview_item, parent, false)
        return DesejoViewHolder(view)
    }

    override fun onBindViewHolder(holder: DesejoViewHolder, position: Int) {
        val desejo = this.lista.get(position)
        holder.tvID.text = desejo.nota.toString()
        holder.tvNome.text = desejo.descricao
    }

    override fun getItemCount(): Int = this.lista.size

    fun add(desejo: Desejo){
        this.lista.add(desejo)
    }

    fun del(position: Int){
        this.lista.removeAt(position)
        notifyItemRemoved(position)
    }
    fun swap(from: Int, to: Int){
        Collections.swap(this.lista, from, to)
        notifyItemMoved(from, to)
    }
    inner class DesejoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var tvID: TextView
        var tvNome: TextView

        init {
            this.tvID = itemView.findViewById(R.id.tvItemID)
            this.tvNome = itemView.findViewById(R.id.tvItemNome)

            itemView.setOnClickListener {
                this@DesejoAdapter.listener?.onItemClick(this.adapterPosition)
            }
            itemView.setOnLongClickListener {
                this@DesejoAdapter.listener?.onItemClickLong(this.adapterPosition)
                true
            }
        }
    }
}
