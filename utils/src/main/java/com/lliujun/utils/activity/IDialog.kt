package com.lliujun.utils.activity

interface IDialog {

    fun showDialog(
        config: Config,
        onPositive: (() -> Unit)? = null,
        onNegative: (() -> Unit)? = null
    )

    class Config(
        var content: String? = "",
        var positiveText: String? = null,
        var negativeText: String? = null,
        var isCancelable: Boolean = true,
        var isOnlyOneOption: Boolean = false
    )
}