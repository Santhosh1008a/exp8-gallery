package com.example.exp8

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView

class FrameGridAdapter(
    private val context: Context,
    private val items: List<FrameItem>,
    private val onItemClick: (FrameItem, Int) -> Unit,
    private val onPopupMenuClick: (View, FrameItem, Int) -> Unit,
    private val onSelectionChanged: (Int, Int) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): FrameItem = items[position]

    override fun getItemId(position: Int): Long = items[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_item_frame, parent, false)
            holder = ViewHolder(
                cardFrame = view.findViewById(R.id.card_frame),
                imgFrame = view.findViewById(R.id.img_frame),
                btnPopupMenu = view.findViewById(R.id.btn_popup_menu),
                imgSelectionBadge = view.findViewById(R.id.img_selection_badge),
                tvFrameTitle = view.findViewById(R.id.tv_frame_title),
                tvFrameFilename = view.findViewById(R.id.tv_frame_filename)
            )
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val item = getItem(position)

        holder.imgFrame.setImageResource(item.drawableResId)
        holder.tvFrameTitle.text = item.title
        holder.tvFrameFilename.text = item.filename

        // Selection styling update
        val accentColor = ContextCompat.getColor(context, R.color.frame_accent)
        val surfaceColor = ContextCompat.getColor(context, R.color.frame_surface)
        val selectedSurfaceColor = ContextCompat.getColor(context, R.color.frame_surface_selected)
        val borderMutedColor = ContextCompat.getColor(context, R.color.frame_card_border)

        if (item.isSelected) {
            holder.cardFrame.setCardBackgroundColor(selectedSurfaceColor)
            holder.cardFrame.strokeColor = accentColor
            holder.cardFrame.strokeWidth = context.resources.getDimensionPixelSize(R.dimen.border_selected_width)
            holder.imgSelectionBadge.visibility = View.VISIBLE
        } else {
            holder.cardFrame.setCardBackgroundColor(surfaceColor)
            holder.cardFrame.strokeColor = borderMutedColor
            holder.cardFrame.strokeWidth = context.resources.getDimensionPixelSize(R.dimen.border_unselected_width)
            holder.imgSelectionBadge.visibility = View.GONE
        }

        // Tapping card toggles selection or triggers click
        holder.cardFrame.setOnClickListener {
            toggleSelection(position)
            onItemClick(item, position)
        }

        // Tapping popup menu icon anchors real PopupMenu without triggering normal card click
        holder.btnPopupMenu.setOnClickListener { anchorView ->
            onPopupMenuClick(anchorView, item, position)
        }

        return view
    }

    fun toggleSelection(position: Int) {
        if (position in items.indices) {
            items[position].isSelected = !items[position].isSelected
            notifyDataSetChanged()
            onSelectionChanged(getSelectedCount(), count)
        }
    }

    fun selectAll() {
        items.forEach { it.isSelected = true }
        notifyDataSetChanged()
        onSelectionChanged(getSelectedCount(), count)
    }

    fun deselectAll() {
        items.forEach { it.isSelected = false }
        notifyDataSetChanged()
        onSelectionChanged(getSelectedCount(), count)
    }

    fun getSelectedCount(): Int = items.count { it.isSelected }

    fun getSelectedItems(): List<FrameItem> = items.filter { it.isSelected }

    private class ViewHolder(
        val cardFrame: MaterialCardView,
        val imgFrame: ImageView,
        val btnPopupMenu: ImageButton,
        val imgSelectionBadge: ImageView,
        val tvFrameTitle: TextView,
        val tvFrameFilename: TextView
    )
}