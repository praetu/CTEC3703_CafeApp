package com.example.ctec3703_cafeapp.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ctec3703_cafeapp.R
import com.example.ctec3703_cafeapp.data.model.MenuItem

class MenuAdapter(
    private val onItemClick: (MenuItem) -> Unit
) : ListAdapter<MenuItem, MenuAdapter.MenuViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)

        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameText: TextView = itemView.findViewById(R.id.menuItemName)
        private val priceText: TextView = itemView.findViewById(R.id.menuItemPrice)
        private val imageView: ImageView = itemView.findViewById(R.id.menuItemImage)

        fun bind(item: MenuItem) {
            nameText.text = item.name
            priceText.text = "£%.2f".format(item.price)

            // Load image with Glide
            Glide.with(itemView)
                .load(item.imageUrl) // Ensure MenuItem has imageUrl property
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(imageView)

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MenuItem>() {

        override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
            return oldItem == newItem
        }
    }
}