package com.example.irene.khramovahomework8

import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView

class NotesAdapter(private var onNoteClick: OnNoteClick, private var onLongNoteClickListener: OnLongNoteClick) :
        RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
    var notes: ArrayList<Note> = ArrayList()
        set(value) {
            notes.clear()
            notes.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        val holder = ViewHolder(view)

        holder.itemView.setOnClickListener {
            onNoteClick.onClick(notes[holder.adapterPosition])
        }

        holder.itemView.setOnLongClickListener {
            onLongNoteClickListener.onLongClick(notes[holder.adapterPosition])
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount() = notes.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        private val textViewContent: TextView = itemView.findViewById(R.id.textViewContent)
        private val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraintLayout)

        fun bind(note: Note) {
            constraintLayout.setBackgroundColor(ContextCompat.getColor(constraintLayout.context, note.color))
            if (note.title.isNullOrEmpty()) {
                textViewTitle.visibility = GONE
            } else {
                textViewTitle.visibility = VISIBLE
                textViewTitle.text = note.title
            }
            textViewContent.text = note.content
        }
    }

    interface OnNoteClick {
        fun onClick(note: Note)
    }

    interface OnLongNoteClick {
        fun onLongClick(note: Note): Boolean
    }
}