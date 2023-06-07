package com.android.mediproject.core.data.remote.interestedmedicine


import com.android.mediproject.core.model.medicine.interestedmedicine.InterestedMedicineListResponse
import kotlinx.coroutines.flow.Flow

interface InterestedMedicineRepository {
    suspend fun getInterestedMedicineList() : Flow<Result<List<InterestedMedicineListResponse.Medicine>>>
}