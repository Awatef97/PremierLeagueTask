package com.example.carefertask.modules.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.carefertask.R
import com.example.carefertask.databinding.ItemDateBinding
import com.example.carefertask.databinding.ItemMatchBinding
import com.example.carefertask.modules.domain.entity.MatchEntity
import com.example.carefertask.modules.domain.entity.MatchStatus
import javax.inject.Inject

class MatchesAdapter @Inject constructor() :
    ListAdapter<MatchEntity, RecyclerView.ViewHolder>(DiffUtilCallback) {

    var onItemClickListener: ((match: MatchEntity) -> Unit)? = null

    private object DiffUtilCallback : DiffUtil.ItemCallback<MatchEntity>() {
        override fun areItemsTheSame(
            oldItem: MatchEntity,
            newItem: MatchEntity
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: MatchEntity,
            newItem: MatchEntity
        ) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val bindingMatch =
            ItemMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val bindingDate =
            ItemDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == DATE) {
            DateViewHolder(bindingDate)
        } else {
            MatchViewHolder(bindingMatch)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MatchViewHolder) {
            holder.bind(currentList[position])
        }
        if (holder is DateViewHolder) {
            holder.bind(currentList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].isDate) {
            DATE
        } else {
            ITEM
        }
    }

    inner class MatchViewHolder(private val binding: ItemMatchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(match: MatchEntity) {
            with(binding) {
                if (match.status == MatchStatus.SCHEDULED.name || match.status == MatchStatus.POSTPONED.name || match.status == MatchStatus.CANCELLED.name) {
                    scoreTv.text = match.date
                } else {
                    scoreTv.text = root.context.getString(
                        R.string.score,
                        match.score?.home.toString(),
                        match.score?.away.toString()
                    )
                }
                competitorsTv.text = root.context.getString(
                    R.string.competitors, match.competitors?.home,
                    match.competitors?.away
                )
                status.text = match.status
                favoriteIv.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context,
                        if (match.isFavorite) R.drawable.ic_round_star_yellow else R.drawable.ic_round_star
                    )
                )
                favoriteIv.setOnClickListener {
                    onItemClickListener?.invoke(match)
                }
            }
        }
    }

    inner class DateViewHolder(private val binding: ItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(match: MatchEntity) {
            binding.dateTv.text = match.date//?.getFormattedDate()
        }
    }

    companion object {
        const val DATE = 1
        const val ITEM = 2
    }
}
