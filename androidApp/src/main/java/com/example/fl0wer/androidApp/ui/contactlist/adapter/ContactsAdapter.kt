package com.example.fl0wer.androidApp.ui.contactlist.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ShapeDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fl0wer.R
import com.example.fl0wer.databinding.ItemContactCardBinding
import kotlin.math.roundToInt

val contactDiffCallback = object : DiffUtil.ItemCallback<ContactListItem>() {
    override fun areItemsTheSame(oldItem: ContactListItem, newItem: ContactListItem) =
        oldItem.contact.id == newItem.contact.id

    override fun areContentsTheSame(oldItem: ContactListItem, newItem: ContactListItem) =
        oldItem.contact.name == newItem.contact.name &&
            oldItem.contact.phone == newItem.contact.phone
}

fun contactItemDecorator(context: Context) = object : RecyclerView.ItemDecoration() {
    private val divider: ShapeDrawable = ShapeDrawable().apply {
        paint.color = ContextCompat.getColor(context, R.color.dividerColor)
        intrinsicHeight = context.resources.getDimension(R.dimen.dividerHeight).toInt()
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        canvas.save()
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val bounds = Rect()
            parent.getDecoratedBoundsWithMargins(child, bounds)
            val bottom: Int = bounds.bottom + child.translationY.roundToInt()
            val top: Int = bottom - divider.intrinsicHeight
            divider.setBounds(0, top, parent.width, bottom)
            divider.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        outRect.set(0, 0, 0, divider.intrinsicHeight)
    }
}

class ContactsAdapter(
    private val scrollToTopListener: () -> Unit,
    private val contactClickListener: (Int) -> Unit,
    private val contactStateChangedListener: (Int) -> Unit,
) : ListAdapter<ContactListItem, ContactsAdapter.ViewHolder>(contactDiffCallback) {
    var items = listOf<ContactListItem>()
        set(value) = submitList(value)

    fun contactClicked(position: Int) {
        contactClickListener(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemContactCardBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.item_contact_card,
                        parent,
                        false
                    )
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCurrentListChanged(
        previousList: MutableList<ContactListItem>,
        currentList: MutableList<ContactListItem>
    ) {
        if (previousList.isEmpty() || currentList.isEmpty()) {
            return
        }
        scrollToTopListener()
    }

    inner class ViewHolder(
        private val binding: ItemContactCardBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding) {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        contactClicked(position)
                    }
                }
                root.setOnLongClickListener {
                    root.isChecked = !root.isChecked
                    true
                }
                root.setOnCheckedChangeListener { _, checked ->
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        contactStateChangedListener(position)
                    }
                }
                photo.setOnClickListener {
                    root.isChecked = !root.isChecked
                }
            }
        }

        fun bind(item: ContactListItem) = with(binding) {
            if (item.checked) {
                photo.setImageResource(R.drawable.ic_done)
            } else {
                if (item.contact.photo == 0) {
                    photo.setImageResource(R.drawable.ic_contact)
                } else {
                    photo.setImageResource(item.contact.photo)
                }
            }
            name.text = item.contact.name
            phoneNumber.text = item.contact.phone
        }
    }
}