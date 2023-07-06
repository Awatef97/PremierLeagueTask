package com.example.carefertask.modules.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.carefertask.databinding.ItemPinnedMatchBinding
import com.example.carefertask.modules.domain.entity.MatchEntity
import com.example.carefertask.modules.domain.entity.MatchesEntity
import javax.inject.Inject

class PinnedMatchesAdapter @Inject constructor() :
    ListAdapter<MatchesEntity, RecyclerView.ViewHolder>(DiffUtilCallback) {

    var onItemClickListener: ((match: MatchEntity) -> Unit)? = null

    private object DiffUtilCallback : DiffUtil.ItemCallback<MatchesEntity>() {
        override fun areItemsTheSame(
            oldItem: MatchesEntity,
            newItem: MatchesEntity
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: MatchesEntity,
            newItem: MatchesEntity
        ) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PinnedMatchViewHolder(
            ItemPinnedMatchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PinnedMatchViewHolder).bind(currentList[position])
    }

    inner class PinnedMatchViewHolder(private val binding: ItemPinnedMatchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(matches: MatchesEntity) {
            binding.pinnedMatchesRv.apply {
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                val matchesAdapter = MatchesAdapter()
                matchesAdapter.submitList(matches.matches)
                adapter = matchesAdapter
                matchesAdapter.onItemClickListener = {
                    onItemClickListener?.invoke(it)
                }
            }
        }
    }
}
