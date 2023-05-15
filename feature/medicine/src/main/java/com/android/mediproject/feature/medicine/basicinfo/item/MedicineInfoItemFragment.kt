package com.android.mediproject.feature.medicine.basicinfo.item

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.medicine.basicinfo.host.MedicineBasicInfoViewModel
import com.android.mediproject.feature.medicine.databinding.FragmentMedicineInfoItemBinding
import com.android.mediproject.feature.medicine.main.BasicInfoType
import com.android.mediproject.feature.medicine.main.MedicineInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

@AndroidEntryPoint
class MedicineInfoItemFragment :
    BaseFragment<FragmentMedicineInfoItemBinding, MedicineBasicInfoViewModel>(FragmentMedicineInfoItemBinding::inflate) {

    override val fragmentViewModel: MedicineBasicInfoViewModel by viewModels()

    private val medicineInfoViewModel by viewModels<MedicineInfoViewModel>(ownerProducer = {
        requireParentFragment().requireParentFragment()
    })

    companion object {
        fun newInstance(basicInfoType: BasicInfoType): MedicineInfoItemFragment {
            val args = Bundle().apply {
                putParcelable("basicInfoType", basicInfoType)
            }
            val fragment = MedicineInfoItemFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.repeatOnStarted {
            fragmentViewModel.basicInfoType.collect { uiState ->
                binding.contentsTextView.text = medicineInfoViewModel.getDetailInfo(uiState).let { detailInfo ->
                    detailInfo as String
                }
            }
        }

        arguments?.apply {
            this.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.getParcelable("basicInfoType", BasicInfoType::class.java)
                } else {
                    it.getParcelable("basicInfoType") as BasicInfoType?
                }?.let { basicInfoType ->
                    fragmentViewModel.setBasicInfoType(basicInfoType)
                }
            }
        }


    }
}