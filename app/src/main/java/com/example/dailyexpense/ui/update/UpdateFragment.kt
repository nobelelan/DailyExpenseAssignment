package com.example.dailyexpense.ui.update

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dailyexpense.R
import com.example.dailyexpense.Utils.verifyDataFromUser
import com.example.dailyexpense.databinding.FragmentUpdateBinding
import com.example.dailyexpense.db.DatabaseHelper
import com.example.dailyexpense.model.Expense
import java.util.*

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdateFragmentArgs>()

    private var databaseHelper: DatabaseHelper? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        databaseHelper = DatabaseHelper(requireContext())

        binding.apply {
            edtType.setText(args.currentExpense.type)
            edtPrice.setText(args.currentExpense.price.toString())
            txtDate.text = args.currentExpense.date
        }

        binding.fabDate.setOnClickListener {
            val calender = Calendar.getInstance()
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH)
            val day = calender.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener{ view, mYear, mMonth, mDay ->
                binding.txtDate.text = " $mDay/${mMonth + 1}/$mYear"
            },year, month, day).show()
        }

        binding.btnDelete.setOnClickListener {
            if (databaseHelper?.deleteExpense(args.currentExpense.id)!!){
                findNavController().navigate(R.id.action_updateFragment_to_expenseFragment)
                Toast.makeText(requireContext(), "Deleted Successfully!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnUpdate.setOnClickListener {
            val type = binding.edtType.text.toString()
            val price = binding.edtPrice.text.toString()
            val date = binding.txtDate.text.toString()
            if (verifyDataFromUser(type, price)){
                val update = databaseHelper?.updateExpense(Expense(args.currentExpense.id, type, price.toInt(), date))
                if (update!!){
                    findNavController().navigate(R.id.action_updateFragment_to_expenseFragment)
                    Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(), "Please fill out all the fields!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}