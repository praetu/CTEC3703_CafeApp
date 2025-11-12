package com.example.ctec3703_cafeapp.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ctec3703_cafeapp.R
import com.example.ctec3703_cafeapp.data.model.Order
import java.text.SimpleDateFormat
import java.util.*

class ProfileAdapter : ListAdapter<Order, ProfileAdapter.OrderViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)

        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val orderIdText: TextView = itemView.findViewById(R.id.orderIdText)
        private val totalText: TextView = itemView.findViewById(R.id.totalText)
        private val statusText: TextView = itemView.findViewById(R.id.statusText)
        private val dateText: TextView = itemView.findViewById(R.id.dateText)

        fun bind(order: Order) {

            orderIdText.text = "Order ID: ${order.orderId}"
            totalText.text = "£%.2f".format(order.totalPrice)
            statusText.text = "Status: ${order.status}"

            val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                .format(Date(order.createdAt))

            dateText.text = formattedDate
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Order>() {

        override fun areItemsTheSame(oldItem: Order, newItem: Order) = oldItem.orderId == newItem.orderId

        override fun areContentsTheSame(oldItem: Order, newItem: Order) = oldItem == newItem
    }
}