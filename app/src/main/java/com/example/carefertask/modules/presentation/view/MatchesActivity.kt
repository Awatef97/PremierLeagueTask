package com.example.carefertask.modules.presentation.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carefertask.databinding.ActivityMatchesBinding
import com.example.carefertask.modules.domain.entity.MatchesEntity
import com.example.carefertask.modules.presentation.adapter.MatchesAdapter
import com.example.carefertask.modules.presentation.adapter.PinnedMatchesAdapter
import com.example.carefertask.modules.presentation.viewmodel.MatchesViewModel
import com.google.android.material.checkbox.MaterialCheckBox
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MatchesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMatchesBinding
    private val viewModel by viewModels<MatchesViewModel>()
    private val concatAdapter = ConcatAdapter()

    @Inject
    lateinit var matchesAdapter: MatchesAdapter

    @Inject
    lateinit var pinnedMatchesAdapter: PinnedMatchesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        renderState()
        renderEffect()
        initListeners()
    }

    private fun initRecyclerView() {
        with(binding.matchesRv) {
            layoutManager =
                LinearLayoutManager(this@MatchesActivity)
            adapter = concatAdapter
        }
    }

    private fun initListeners() {
        matchesAdapter.onItemClickListener = {
            viewModel.changeMatchFavoriteStatus(it)
        }
        pinnedMatchesAdapter.onItemClickListener = {
            viewModel.changeMatchFavoriteStatus(it)
        }
        binding.favoritesCb.addOnCheckedStateChangedListener { _, state ->
            viewModel.changeFavoriteSelection(state == MaterialCheckBox.STATE_CHECKED)
        }

    }


    private fun renderState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    binding.loadingPb.isVisible = it.isLoading
                    if (it.isFavoriteSelected) {
                        matchesAdapter.submitList(it.matchesEntity.favoriteMatches)
                        pinnedMatchesAdapter.submitList(mutableListOf(MatchesEntity(matches = it.matchesEntity.pinnedFavoritesMatches)))
                    } else {
                        matchesAdapter.submitList(it.matchesEntity.matches)
                        pinnedMatchesAdapter.submitList(mutableListOf(MatchesEntity(matches = it.matchesEntity.pinnedMatches)))
                    }
                    concatAdapter.addAdapter(0, pinnedMatchesAdapter)
                    concatAdapter.addAdapter(matchesAdapter)
                }
            }
        }
    }

    private fun renderEffect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collectLatest { error ->
                    binding.errorCl.isVisible = error != null
                    binding.errorTv.text = error?.let { getString(it) }
                }
            }
        }
    }
}