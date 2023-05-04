package com.android.mediproject.feature.setting

import MutableEventFlow
import androidx.lifecycle.viewModelScope
import asEventFlow
import com.android.mediproject.core.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SettingViewModel : BaseViewModel() {
    private val _eventFlow = MutableEventFlow<SettingEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: SettingEvent) = viewModelScope.launch { _eventFlow.emit(event) }
    
    sealed class SettingEvent {
        data class Notice(val unit : Unit? = null) : SettingEvent()
        data class Introduce(val unit : Unit? = null) : SettingEvent()
        data class Policy(val unit : Unit? = null) : SettingEvent()
        data class Privacy(val unit : Unit? = null) : SettingEvent()
        data class Communicate(val unit : Unit? = null) : SettingEvent()
    }
}