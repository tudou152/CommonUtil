package com.lliujun.utils.extensions.android

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.lliujun.utils.R

fun FragmentManager.addWithAnimation(@IdRes containerId: Int, fragment: Fragment, tag: String) {
    beginTransaction()
        .setCustomAnimations(
            R.anim.common_alpha_0to1,
            R.anim.common_alpha_1to0,
            R.anim.common_alpha_0to1,
            R.anim.common_alpha_1to0
        )
        .add(containerId, fragment, tag)
        .addToBackStack(tag)
        .commit()
}

/**
 * 逐渐消失的动画
 * */
fun FragmentTransaction.animationFade() : FragmentTransaction {
    return setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
}

/**
 * 从右边到左的进场动画
 * */
fun FragmentTransaction.animationSlideIn() : FragmentTransaction {
    return setCustomAnimations(
            R.anim.common_in_from_right,
            R.anim.common_out_to_left,
            R.anim.common_in_from_left,
            R.anim.common_out_to_right
    )
}

fun FragmentManager.add(@IdRes containerId: Int, fragment: Fragment, tag: String) {
    beginTransaction()
        .add(containerId, fragment, tag)
        .addToBackStack(tag)
        .commit()
}

fun FragmentManager.replaceTo(@IdRes containerId: Int, fragment: Fragment, tag: String) {
    beginTransaction().replace(containerId, fragment, tag)
        .addToBackStack(tag)
        .commit()
}