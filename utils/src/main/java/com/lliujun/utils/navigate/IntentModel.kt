package com.lliujun.utils.navigate

data class IntentModel(val name: String, val navigation: INavigation) {
    fun getString(): String {
        return name
    }
}