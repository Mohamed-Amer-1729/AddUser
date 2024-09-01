package com.example.finaltodo

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

class MyAdapter(val tasksList: ArrayList<Task>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var customListener: onItemClickListener
    lateinit var editListener: onEditClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: MyAdapter.onItemClickListener) {
        customListener = listener
    }
    interface onEditClickListener{
        fun onEditClick(position: Int)
    }

    fun setOnEditClickListener(listener: onEditClickListener){
        editListener = listener
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
            in 0..2 -> 0
            in 3..5 -> 1
            else -> 2
        }
    }

    override fun getItemCount() = tasksList.size

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val currentItem = tasksList[p1]
        when(ChronoUnit.DAYS.between(LocalDate.now(), currentItem.deadline)){
            in 0..2 ->(p0 as RedViewHolder).bind(currentItem, p1)
            in 3..5 ->(p0 as YellowViewHolder).bind(currentItem, p1)
            else -> (p0 as GreenViewHolder).bind(currentItem, p1)
        }
    }

    inner class RedViewHolder(itemView: View, listener: onItemClickListener, editlistener: onEditClickListener): RecyclerView.ViewHolder(itemView){
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
        }

        fun bind(item: Task, position: Int){
            this.TitleTextView.text = item.title
            this.ClearButton.setOnClickListener{
                delete(position)
            }
        }
    }

    inner class YellowViewHolder(itemView: View, listener: onItemClickListener, editlistener: onEditClickListener): RecyclerView.ViewHolder(itemView){
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
        }

        fun bind(item: Task, position: Int){
            this.TitleTextView.text = item.title
            this.ClearButton.setOnClickListener{
                delete(position)
            }
        }
    }

    inner class GreenViewHolder(itemView: View, listener: onItemClickListener, editlistener: onEditClickListener): RecyclerView.ViewHolder(itemView){
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
        }

        fun bind(item: Task, position: Int){
            this.TitleTextView.text = item.title
            this.ClearButton.setOnClickListener{
                delete(position)
            }
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



}