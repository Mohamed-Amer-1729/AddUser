package com.example.finaltodo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.finaltodo.databinding.FragmentTaskMainBinding

class TaskMain : Fragment() {

    private lateinit var binding: FragmentTaskMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskMainBinding.inflate(layoutInflater, container, false)
        binding.taskDesc.text = requireArguments().getString("Description")
        binding.taskTitle.text = requireArguments().getString("Title")
        binding.taskDeadline.text = requireArguments().getString("Deadline")
        return binding.root
    }



}