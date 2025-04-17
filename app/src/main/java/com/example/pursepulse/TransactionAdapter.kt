package com.example.pursepulse

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(
    private var transactionList: MutableList<Transaction>,
    private val onDeleteClick: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.textViewDate)
        val amountText: TextView = itemView.findViewById(R.id.textViewAmount)
        val categoryText: TextView = itemView.findViewById(R.id.textViewCategory)
        val typeText: TextView = itemView.findViewById(R.id.textViewType)
        val updateButton: ImageView = itemView.findViewById(R.id.editIcon)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.dateText.text = transaction.date
        holder.amountText.text = "Rs.${transaction.amount}"
        holder.categoryText.text = transaction.category
        holder.typeText.text = transaction.type

        // Edit click
        holder.updateButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, UpdateTransaction::class.java).apply {
                putExtra("transaction_id", transaction.id)
                putExtra("date", transaction.date)
                putExtra("amount", transaction.amount)
                putExtra("category", transaction.category)
                putExtra("type", transaction.type)
            }
            context.startActivity(intent)
        }

        // Delete click
        holder.deleteButton.setOnClickListener {
            onDeleteClick(transaction)
        }
    }

    override fun getItemCount(): Int = transactionList.size

    fun updateList(newList: List<Transaction>) {
        transactionList.clear()
        transactionList.addAll(newList)
        notifyDataSetChanged()
    }
}
