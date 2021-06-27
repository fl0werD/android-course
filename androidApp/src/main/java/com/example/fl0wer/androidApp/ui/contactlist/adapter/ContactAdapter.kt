package com.example.fl0wer.androidApp.ui.contactlist.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ShapeDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fl0wer.R
import com.example.fl0wer.databinding.ItemContactCardBinding
import com.example.fl0wer.databinding.ItemContactlistFooterBinding
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import kotlin.math.roundToInt

fun contactDelegate(
    contactClickListener: (Int) -> Unit,
) = adapterDelegateViewBinding<ContactListItem.Contact, ContactListItem, ItemContactCardBinding>(
    viewBinding = { layoutInflater, root ->
        ItemContactCardBinding.inflate(layoutInflater, root, false)
    },
    on = { item, _, _ ->
        item is ContactListItem.Contact
    },
) {
    fun contactClicked(position: Int) {
        contactClickListener(position)
    }

    with(binding) {
        root.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                contactClicked(position)
            }
        }
    }

    bind {
        val contact = item.contact
        with(binding) {
            if (contact.photo == 0) {
                photo.setImageResource(R.drawable.ic_contact)
            } else {
                photo.setImageResource(contact.photo)
            }
            name.text = contact.name
        }
    }
}

fun contactFooterDelegate() =
    adapterDelegateViewBinding<ContactListItem.Footer, ContactListItem, ItemContactlistFooterBinding>(
        viewBinding = { layoutInflater, root ->
            ItemContactlistFooterBinding.inflate(layoutInflater, root, false)
        },
        on = { item, _, _ ->
            item is ContactListItem.Footer
        },
    ) {
        bind {
            binding.footer.text = context.resources
                .getQuantityString(R.plurals.contacts_count, item.count, item.count)
        }
    }

val contactDiffCallback = object : DiffUtil.ItemCallback<ContactListItem>() {
    override fun areItemsTheSame(oldItem: ContactListItem, newItem: ContactListItem): Boolean {
        return when {
            oldItem is ContactListItem.Contact && newItem is ContactListItem.Contact -> {
                return oldItem.contact.id == newItem.contact.id
            }
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: ContactListItem, newItem: ContactListItem): Boolean {
        return when {
            oldItem is ContactListItem.Contact && newItem is ContactListItem.Contact -> {
                return oldItem.contact.name == newItem.contact.name
            }
            else -> false
        }
    }
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
    contactClickListener: (Int) -> Unit,
) : AsyncListDifferDelegationAdapter<ContactListItem>(contactDiffCallback) {
    init {
        delegatesManager.apply {
            addDelegate(
                contactDelegate(
                    contactClickListener,
                )
            )
            addDelegate(contactFooterDelegate())
        }
    }
}
