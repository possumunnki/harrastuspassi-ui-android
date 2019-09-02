package fi.haltu.harrastuspassi.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import fi.haltu.harrastuspassi.R
import fi.haltu.harrastuspassi.models.HobbyEvent

class FilterTagsRecyclerViewAdapter(private val categoryTagsList: ArrayList<String>, private val clickListener: (categryName: String) -> Unit):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.filter_tags_item, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val filterTag : String = categoryTagsList[position]
        (holder as TagViewHolder).bind(filterTag, clickListener)
    }

    override fun getItemCount(): Int {
        return categoryTagsList.size
    }

    class TagViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private var tag: TextView = itemView.findViewById(R.id.textView)
        private var deleteButton: ImageButton = itemView.findViewById(R.id.delete_tag)
        fun bind(filterTag: String, clickListener: (String) -> Unit) {
            tag.text = filterTag
            Log.d("idToCategory", "filter tags")

            deleteButton.setOnClickListener{clickListener(filterTag)}
        }
    }
}