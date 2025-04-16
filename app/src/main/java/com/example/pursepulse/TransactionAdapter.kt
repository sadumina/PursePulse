package com.example.pursepulse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(private val transactionList: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.textViewDate)
        val amountText: TextView = itemView.findViewById(R.id.textViewAmount)
        val categoryText: TextView = itemView.findViewById(R.id.textViewCategory)
        val typeText: TextView = itemView.findViewById(R.id.textViewType)
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
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
}
