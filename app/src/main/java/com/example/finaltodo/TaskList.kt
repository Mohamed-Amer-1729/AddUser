package com.example.finaltodo

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finaltodo.data.TaskViewModel
import com.example.finaltodo.databinding.FragmentTaskListBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class TaskList : Fragment() {
    lateinit var tasks: ArrayList<Task>
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MyAdapter
    private lateinit var binding: FragmentTaskListBinding
    lateinit var mTaskViewModel: TaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentTaskListBinding.inflate(layoutInflater, container, false)
        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasks = arrayListOf()
        recyclerView = binding.listOfTasks
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(false)
        adapter = MyAdapter(tasks)
        recyclerView.adapter = adapter

        mTaskViewModel.readAllData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            task -> adapter.setData(task)
        })

        adapter.setOnItemClickListener(object : MyAdapter.OnItemClickListener{
            override fun onItemClick(position: Int){
                //Make the change from fragment A to fragment B
                val bundle = bundleOf(
                    "Title" to adapter.tasksList[position].title,
                    "Description" to adapter.tasksList[position].description,
                    "Deadline" to adapter.tasksList[position].deadline.toString()
                )
                findNavController().navigate(R.id.action_taskList_to_taskMain, bundle)
            }
        })

        adapter.setOnEditClickListener(object : MyAdapter.OnEditClickListener{
            override fun onEditClick(position: Int) {

                showEditTask(adapter.tasksList[position], position)
            }
        })

        adapter.setOnDeleteClickListener(object  : MyAdapter.OnDeleteClickListener{
            override fun onDeleteClick(position: Int) {
                val task = adapter.tasksList[position]
                val deletedTask: com.example.finaltodo.data.Task =
                    com.example.finaltodo.data.Task(task.id, task.title, task.description, task.deadline.toString())
                mTaskViewModel.deleteTask(deletedTask)
                adapter.delete(position)
            }
        })
        showAddTask()
        binding.fabDeleteAll.setOnClickListener{
            showDeleteAll()
        }
    }

    private fun showDeleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _->
            mTaskViewModel.deleteAllTasks()
            Toast.makeText(context, "Deleted All Active Tasks", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("No"){_, _->}
        builder.setTitle("Delete All tasks")
        builder.setMessage("Are you sure you want to clear all tasks?")
        builder.create().show()
    }

    private fun showAddTask(){
        binding.fabAddTask.setOnClickListener(){
            val builder = AlertDialog.Builder(requireContext())
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.task_input, null)
            builder.setCancelable(false).setView(dialogLayout)
            val titleView: TextView = dialogLayout.findViewById(R.id.input_task_title)
            val descView: TextView = dialogLayout.findViewById(R.id.input_task_desc)
            val deadlineView: TextView = dialogLayout.findViewById(R.id.input_task_deadline)
            var deadline: LocalDate = LocalDate.now()
            deadlineView.setOnClickListener(){
                val c = Calendar.getInstance()

                // on below line we are getting
                // our day, month and year.
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                // on below line we are creating a
                // variable for date picker dialog.
                val datePickerDialog = context?.let { it1 ->
                    DatePickerDialog(
                        // on below line we are passing context.
                        it1,
                        { view, year, monthOfYear, dayOfMonth ->
                            // on below line we are setting
                            // date to our edit text.
                            val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                            deadline = LocalDate.of(year, monthOfYear+1, dayOfMonth)
                            deadlineView.setText(dat)
                        },
                        // on below line we are passing year, month
                        // and day for the selected date in our date picker.
                        year,
                        month,
                        day
                    )
                }

                if (datePickerDialog != null) {
                    datePickerDialog.show()
                }
            }
            with(builder){
                setTitle("Enter User Info")
                setPositiveButton("Add Task", null)

                setNegativeButton("Cancel"){ dialog, which-> dialog.cancel() }
            }

            val taskCreateDialog = builder.create()
            taskCreateDialog.setOnShowListener{
                val positiveButton = taskCreateDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setOnClickListener{
                    val title =titleView.text.toString()
                    val desc = descView.text.toString()


                    if(title.isEmpty() || desc.isEmpty() || deadline.toString().isEmpty()){
                        Toast.makeText(context, "Fill all required fields", Toast.LENGTH_SHORT).show()
                    }else{
                        val databaseTask: com.example.finaltodo.data.Task =
                            com.example.finaltodo.data.Task(0, title, desc, deadline.toString())
                        mTaskViewModel.addTask(databaseTask)
                        val newTask: Task = Task(databaseTask.id, title, desc, deadline)
                        adapter.addTask(newTask)
                        taskCreateDialog.dismiss()
                    }
                }
            }
            taskCreateDialog.show()
        }
    }

    private fun showEditTask(task: Task, pos: Int){
        Log.d("Test", "Reached Inside Edit Dialog")
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.task_input, null)
        builder.setCancelable(false).setView(dialogLayout)
        val titleView: TextView = dialogLayout.findViewById(R.id.input_task_title)
        val descView: TextView = dialogLayout.findViewById(R.id.input_task_desc)
        val deadlineView: TextView = dialogLayout.findViewById(R.id.input_task_deadline)
        var deadline: LocalDate = task.deadline
        deadlineView.setOnClickListener(){
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = context?.let { it1 ->
                DatePickerDialog(
                    // on below line we are passing context.
                    it1,
                    { view, year, monthOfYear, dayOfMonth ->
                        // on below line we are setting
                        // date to our edit text.
                        val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                        deadline = LocalDate.of(year, monthOfYear+1, dayOfMonth)
                        deadlineView.setText(dat)
                    },
                    // on below line we are passing year, month
                    // and day for the selected date in our date picker.
                    year,
                    month,
                    day
                )
            }

            datePickerDialog?.show()
        }
        with(builder){
            setTitle("Update User Info")
            setPositiveButton("Edit Task", null)

            setNegativeButton("Cancel"){ dialog, _ -> dialog.cancel() }
        }
        titleView.text = task.title
        descView.text = task.description
        val formatter = DateTimeFormatter.ofPattern("d-MM-yyyy")
        deadlineView.text = task.deadline.format(formatter)
        val taskCreateDialog = builder.create()
        taskCreateDialog.setOnShowListener{

            val positiveButton = taskCreateDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener{
                val title =titleView.text.toString()
                val desc = descView.text.toString()


                if(title.isEmpty() || desc.isEmpty() || deadline.toString().isEmpty()){
                    Toast.makeText(context, "Fill all required fields", Toast.LENGTH_SHORT).show()
                }else{
                    val updatedTask: com.example.finaltodo.data.Task =
                        com.example.finaltodo.data.Task(task.id, title, desc, deadline.toString())
                    mTaskViewModel.updateTask(updatedTask)

                    adapter.tasksList[pos] = Task(task.id, title, desc, deadline)
                    adapter.notifyDataSetChanged()
                    taskCreateDialog.dismiss()
                }
            }
        }
        taskCreateDialog.show()
    }
}