package com.example.dailyexpense.ui.add

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dailyexpense.R
import com.example.dailyexpense.Utils.verifyDataFromUser
import com.example.dailyexpense.databinding.FragmentAddBinding
import com.example.dailyexpense.db.DatabaseHelper
import com.example.dailyexpense.model.Expense
import java.util.*

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private var databaseHelper: DatabaseHelper? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        databaseHelper = DatabaseHelper(requireContext())

        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        binding.txtDate.text = "$day/${month+1}/$year"

        binding.fabDate.setOnClickListener {
            DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener{ view, mYear, mMonth, mDay ->
                binding.txtDate.text = " $mDay/${mMonth + 1}/$mYear"
            },year, month, day).show()
        }

        binding.btnInsert.setOnClickListener {
            val type = binding.edtType.text.toString()
            val price = binding.edtPrice.text.toString()
            val date = binding.txtDate.text.toString()
            if (verifyDataFromUser(type, price)){
                val insertion = databaseHelper?.insertExpense(Expense(0,type, price.toInt(), date))
                if (insertion!!){
                    Toast.makeText(requireContext(), "Successfully inserted!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_addFragment_to_expenseFragment)
                }else{
                    Toast.makeText(requireContext(), "Failed to insert item.", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(), "Please fill out all the fields.", Toast.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}