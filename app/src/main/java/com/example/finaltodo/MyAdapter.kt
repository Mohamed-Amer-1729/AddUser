package com.example.finaltodo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class MyAdapter(var tasksList: MutableList<Task>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var customListener: OnItemClickListener
    lateinit var editListener: OnEditClickListener
    lateinit var deleteListener: OnDeleteClickListener
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        customListener = listener
    }
    interface OnEditClickListener{
        fun onEditClick(position: Int)
    }

    fun setOnEditClickListener(listener: OnEditClickListener){
        editListener = listener
    }

    interface OnDeleteClickListener{
        fun onDeleteClick(position: Int)
    }

    fun setOnDeleteClickListener(listener: OnDeleteClickListener){
        deleteListener = listener
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return when(p1){
            0 -> RedViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.task_red, p0, false), customListener, editListener)
            1 -> YellowViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.task_yellow, p0, false), customListener,  editListener)
            2 -> GreenViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.task_green, p0, false), customListener,  editListener)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {

        return when(ChronoUnit.DAYS.between(LocalDate.now(), tasksList[position].deadline)){
            in 3..5 -> 1
            in 6..Int.MAX_VALUE -> 2
            else -> 0
        }
    }

    override fun getItemCount() = tasksList.size

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val currentItem = tasksList[p1]
        when(ChronoUnit.DAYS.between(LocalDate.now(), currentItem.deadline)){
            in 3..5 ->(p0 as YellowViewHolder).bind(currentItem, p1)
            in 6..Int.MAX_VALUE -> (p0 as GreenViewHolder).bind(currentItem, p1)
            else ->(p0 as RedViewHolder).bind(currentItem, p1)
        }
    }

    inner class RedViewHolder(itemView: View, listener: OnItemClickListener, editlistener: OnEditClickListener): RecyclerView.ViewHolder(itemView){
        val TitleTextView: TextView = itemView.findViewById(R.id.listItem_task_title_red)
        val ClearButton: MaterialButton = itemView.findViewById(R.id.listItem_clear_btn_red)
        val EditButton: MaterialButton = itemView.findViewById(R.id.listItem_task_edit_red)
        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }

            EditButton.setOnClickListener{
                editlistener.onEditClick(adapterPosition)
            }

            ClearButton.setOnClickListener{
                deleteListener.onDeleteClick(adapterPosition)
            }
        }

        fun bind(item: Task, position: Int){
            this.TitleTextView.text = item.title
        }
    }

    inner class YellowViewHolder(itemView: View, listener: OnItemClickListener, editlistener: OnEditClickListener): RecyclerView.ViewHolder(itemView){
        val TitleTextView: TextView = itemView.findViewById(R.id.listItem_task_title_yellow)
        val ClearButton: MaterialButton = itemView.findViewById(R.id.listItem_clear_btn_yellow)
        val EditButton: MaterialButton = itemView.findViewById(R.id.listItem_task_edit_yellow)
        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }

            EditButton.setOnClickListener{
                editlistener.onEditClick(adapterPosition)
            }

            ClearButton.setOnClickListener{
                deleteListener.onDeleteClick(adapterPosition)
            }
        }

        fun bind(item: Task, position: Int){
            this.TitleTextView.text = item.title
        }
    }

    inner class GreenViewHolder(itemView: View, listener: OnItemClickListener, editlistener: OnEditClickListener): RecyclerView.ViewHolder(itemView){
        val TitleTextView: TextView = itemView.findViewById(R.id.listItem_task_title_green)
        val ClearButton: MaterialButton = itemView.findViewById(R.id.listItem_clear_btn_green)
        val EditButton: MaterialButton = itemView.findViewById(R.id.listItem_task_edit_green)
        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }

            EditButton.setOnClickListener{
                editlistener.onEditClick(adapterPosition)
            }

            ClearButton.setOnClickListener{
                deleteListener.onDeleteClick(adapterPosition)
            }
        }

        fun bind(item: Task, position: Int){
            this.TitleTextView.text = item.title
        }
    }

    fun delete(position: Int){
        tasksList.removeAt(position)
        notifyDataSetChanged()
    }

    fun addTask(newTask: Task){
        tasksList.add(newTask)
        notifyDataSetChanged()
    }

    fun setData(tasksList: List<com.example.finaltodo.data.Task>){
        var tasks: MutableList<Task> = mutableListOf()
        tasksList.forEach {
            tasks.add(Task(it.id, it.title, it.description, LocalDate.parse(it.deadline, DateTimeFormatter.ISO_DATE)))
        }
        this.tasksList = tasks
        notifyDataSetChanged()
    }



}