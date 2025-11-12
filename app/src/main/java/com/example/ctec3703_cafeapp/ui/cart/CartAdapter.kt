package com.example.ctec3703_cafeapp.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ctec3703_cafeapp.R
import com.example.ctec3703_cafeapp.data.model.CartItem

class CartAdapter(
    private val onIncrease: (CartItem) -> Unit,
    private val onDecrease: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.cartItemName)
        private val priceText: TextView = itemView.findViewById(R.id.cartItemPrice)
        private val quantityText: TextView = itemView.findViewById(R.id.cartItemQuantity)
        private val increaseButton: ImageButton = itemView.findViewById(R.id.increaseButton)
        private val decreaseButton: ImageButton = itemView.findViewById(R.id.decreaseButton)

        fun bind(item: CartItem) {
            nameText.text = item.name
            priceText.text = String.format("£%.2f", item.price)
            quantityText.text = item.quantity.toString()

            increaseButton.setOnClickListener { onIncrease(item) }
            decreaseButton.setOnClickListener { onDecrease(item) }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {

        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem) =
            oldItem.itemId == newItem.itemId

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem) =
            oldItem == newItem
    }
}