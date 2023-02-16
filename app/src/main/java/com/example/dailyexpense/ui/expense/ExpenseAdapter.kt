package com.example.dailyexpense.ui.expense

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyexpense.databinding.ExpenseItemBinding
import com.example.dailyexpense.model.Expense

class ExpenseAdapter: RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    inner class ExpenseViewHolder(val binding: ExpenseItemBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Expense>(){
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        return ExpenseViewHolder(ExpenseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = differ.currentList[position]
        holder.binding.apply {
            txtDate.text = expense.date
            txtType.text = expense.type
            txtPrice.text = expense.price.toString()
        }
        holder.binding.root.setOnClickListener {
            val action = ExpenseFragmentDirections.actionExpenseFragmentToUpdateFragment(expense)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}