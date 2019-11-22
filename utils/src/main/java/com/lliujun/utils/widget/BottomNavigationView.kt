package com.lliujun.utils.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lliujun.utils.R

data class Item(
    val title: String,
    @DrawableRes val res: Int,
    @DrawableRes val selectedRes: Int? = null
)

data class Config(val normalColor: Int, val selectedColor: Int)

@Suppress("MemberVisibilityCanBePrivate")
class SimpleAdapter(private var data: List<Item>?, var config: Config) :
    RecyclerView.Adapter<SimpleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lj_bottom_navigation_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data!![position]

        val titleColor = if (position == selectedIndex) config.selectedColor else config.normalColor

        val image = if (item.selectedRes != null) {
            if (position == selectedIndex) item.selectedRes else item.res
        } else {
            val colorStateList = ColorStateList.valueOf(
                if (position == selectedIndex) config.selectedColor else config.normalColor
            )
            ViewCompat.setBackgroundTintList(holder.icon, colorStateList)
            item.res
        }

        holder.title.text = item.title
        holder.title.setTextColor(titleColor)
        holder.icon.setImageResource(image)

        holder.root.setOnClickListener {
            selectItem(position)
        }
    }

    class ViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        val title: TextView = root.findViewById(R.id.title)
        val icon: ImageView = root.findViewById(R.id.icon)
    }

    var selectedIndex = -1
        private set

    var onItemSelectedListener: OnItemSelectedListener? = null

    interface OnItemSelectedListener {
        fun onItemSelected(index: Int, item: Item)
        fun onReSelected(index: Int, item: Item) {}
    }

    fun selectItem(index: Int) {
        selectItem(index, true)
    }

    fun selectItem(index: Int, isNoticeChild: Boolean = false) {
        if (isNoticeChild && selectedIndex == index) {
            onItemSelectedListener?.onReSelected(index, data!![index])
            return
        }
        val previous = selectedIndex
        selectedIndex = index

        if (isNoticeChild) {
            onItemSelectedListener?.onItemSelected(index, data!![index])
        }
        notifyItemChanged(previous)
        notifyItemChanged(index)
    }
}

@Suppress("MemberVisibilityCanBePrivate")
class BottomNavigationView : ConstraintLayout, SimpleAdapter.OnItemSelectedListener {
    var onTabItemSelectedListener: SimpleAdapter.OnItemSelectedListener? = null

    override fun onItemSelected(index: Int, item: Item) {
        onTabItemSelectedListener?.onItemSelected(index, item)
    }

    override fun onReSelected(index: Int, item: Item) {
        onTabItemSelectedListener?.onReSelected(index, item)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context?, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    private val mContainer: ConstraintLayout
    private val recyclerView: RecyclerView
    private lateinit var adapter: SimpleAdapter

    init {
        LayoutInflater.from(context).inflate(R.layout.lj_bottom_navigation, this, true)
        mContainer = findViewById(R.id.container)
        recyclerView = findViewById(R.id.recycler_view)
    }

    fun setupItems(
        items: List<Item>,
        config: Config = Config(Color.WHITE, Color.BLUE),
        initIndex: Int = 0
    ) {
        recyclerView.layoutManager =
            GridLayoutManager(context, items.size, LinearLayoutManager.VERTICAL, false)
        adapter = SimpleAdapter(items, config).apply {
            onItemSelectedListener = this@BottomNavigationView
        }
        recyclerView.adapter = adapter
        adapter.selectItem(initIndex, false)
    }
}
