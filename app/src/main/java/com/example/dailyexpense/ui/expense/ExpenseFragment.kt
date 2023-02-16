package com.example.dailyexpense.ui.expense

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dailyexpense.R
import com.example.dailyexpense.Utils
import com.example.dailyexpense.databinding.FragmentExpenseBinding
import com.example.dailyexpense.db.DatabaseHelper
import com.example.dailyexpense.model.Expense
import com.google.android.material.snackbar.Snackbar

class ExpenseFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentExpenseBinding? = null
    private val binding get() = _binding!!

    private val expenseAdapter by lazy { ExpenseAdapter() }

    private var databaseHelper: DatabaseHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentExpenseBinding.inflate(layoutInflater, container, false)

        binding.fabInsert.setOnClickListener {
            findNavController().navigate(R.id.action_expenseFragment_to_addFragment)
        }

        val recyclerView = binding.rvExpense
        recyclerView.adapter = expenseAdapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)

        databaseHelper = DatabaseHelper(requireContext())
        expenseAdapter.differ.submitList(databaseHelper?.getAllExpenses())

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.expense_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(text: String?): Boolean {
        text?.let {
            expenseAdapter.differ.submitList(databaseHelper?.searchExpense(text))
        }
        return true
    }

    override fun onQueryTextChange(text: String?): Boolean {
        text?.let {
            expenseAdapter.differ.submitList(databaseHelper?.searchExpense(text))
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}