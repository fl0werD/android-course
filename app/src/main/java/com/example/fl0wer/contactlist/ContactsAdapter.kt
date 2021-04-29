package com.example.fl0wer.contactlist

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
import com.example.fl0wer.Contact
import com.example.fl0wer.R
import com.example.fl0wer.databinding.ItemContactCardBinding
import kotlin.math.roundToInt

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

val contactDiffCallback = object : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.rowId == newItem.rowId
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.name == newItem.name &&
                oldItem.phone == newItem.phone
    }
}

class ContactsAdapter(
    private val scrollToTopListener: () -> Unit,
    private val contactClickListener: (Int) -> Unit,
) : ListAdapter<Contact, ContactsAdapter.ViewHolder>(contactDiffCallback) {
    var items = listOf<Contact>()
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
        previousList: MutableList<Contact>,
        currentList: MutableList<Contact>
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
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    contactClicked(position)
                }
            }
        }

        fun bind(contact: Contact) = with(binding) {
            photo.setImageResource(contact.photo)
            name.text = contact.name
            phoneNumber.text = contact.phone
        }
    }
}
