package br.com.tmoura.gists.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class AbstractViewHolder<Model>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: Model, onItemSelected: ((Model) -> Unit)?)
}