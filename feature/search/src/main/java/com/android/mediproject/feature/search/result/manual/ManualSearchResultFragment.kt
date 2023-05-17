package com.android.mediproject.feature.search.result.manual

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.paging.map
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.mediproject.core.common.constant.MedicationType
import com.android.mediproject.core.common.util.navigateByDeepLink
import com.android.mediproject.core.model.local.navargs.MedicineInfoArgs
import com.android.mediproject.core.model.remote.medicineapproval.ApprovedMedicineItemDto
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.core.ui.base.view.listfilter.MediPopupMenu
import com.android.mediproject.feature.search.R
import com.android.mediproject.feature.search.SearchMedicinesViewModel
import com.android.mediproject.feature.search.databinding.FragmentManualSearchResultBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import repeatOnStarted

@AndroidEntryPoint
class ManualSearchResultFragment :
    BaseFragment<FragmentManualSearchResultBinding, ManualSearchResultViewModel>(FragmentManualSearchResultBinding::inflate) {

    private val searchMedicinesViewModel: SearchMedicinesViewModel by viewModels({ requireParentFragment().requireParentFragment() })

    override val fragmentViewModel: ManualSearchResultViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = fragmentViewModel

            searchResultRecyclerView.apply {
                setHasFixedSize(true)
                setItemViewCacheSize(8)
                itemAnimator = null
                addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
                    setDrawable(ContextCompat.getDrawable(requireContext(), com.android.mediproject.core.ui.R.drawable.divider)!!)
                })
            }

            val searchResultListAdapter = ApprovedMedicinesAdapter().also {
                it.withLoadStateHeaderAndFooter(header = PagingLoadStateAdapter { it.retry() },
                    footer = PagingLoadStateAdapter { it.retry() })
            }


            searchResultRecyclerView.adapter = searchResultListAdapter

            filterBtn.setOnClickListener { it ->
                MediPopupMenu.showMenu(
                    it, R.menu.search_result_list_filter_menu
                ) { menuItem ->

                    when (menuItem.itemId) {
                        R.id.listOnlySpecialtyMedicines -> fragmentViewModel.searchMedicinesByMedicationType(MedicationType.SPECIALTY)

                        R.id.listOnlyGenericMedicines -> fragmentViewModel.searchMedicinesByMedicationType(MedicationType.GENERAL)

                        R.id.listAllMedicines -> fragmentViewModel.searchMedicinesByMedicationType(MedicationType.ALL)
                    }
                    true
                }
            }

            viewLifecycleOwner.repeatOnStarted {
                // 검색 결과를 수신하면 리스트에 표시한다.
                launch {
                    fragmentViewModel.searchResultFlow.collect {
                        it.let { pager ->
                            pager.map { item ->
                                item.onClick = this@ManualSearchResultFragment::openMedicineInfo
                                item
                            }
                        }.also { pagingData ->
                            searchResultListAdapter.submitData(pagingData)
                        }
                    }
                }

                // 검색어가 변경되면 검색을 다시 수행한다.
                launch {
                    searchMedicinesViewModel.searchQuery.first().also { query ->
                        fragmentViewModel.searchMedicinesByItemName(query)
                    }
                }
            }
        }

    }

    private fun openMedicineInfo(approvedMedicineItemDto: ApprovedMedicineItemDto) {
        activity?.findNavController(com.android.mediproject.core.common.R.id.fragmentContainerView)?.navigateByDeepLink(
            "medilens://search/medicine/medicine_detail_nav", MedicineInfoArgs(
                medicineName = approvedMedicineItemDto.itemName,
                imgUrl = approvedMedicineItemDto.bigPrdtImgUrl ?: "",
                entpName = approvedMedicineItemDto.entpName ?: "",
                itemSequence = approvedMedicineItemDto.itemSeq ?: "",
                medicineEngName = approvedMedicineItemDto.itemEngName ?: ""
            )
        )

    }
}