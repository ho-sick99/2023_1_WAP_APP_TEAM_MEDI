package com.android.mediproject.feature.medicine.basicinfo.item

import android.os.Bundle
import android.view.View
import com.android.mediproject.feature.medicine.databinding.FragmentDosageInfoItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import repeatOnStarted

@AndroidEntryPoint
class DosageInfoItemFragment : BaseMedicineInfoItemFragment<FragmentDosageInfoItemBinding>(
    FragmentDosageInfoItemBinding::inflate
) {

    companion object : MedicineInfoItemFragmentFactory by BaseMedicineInfoItemFragment.Companion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.repeatOnStarted {
            medicineInfoViewModel.retriveDosage().collectLatest {
                binding.contentsTextView.text = it
            }
        }

    }

}