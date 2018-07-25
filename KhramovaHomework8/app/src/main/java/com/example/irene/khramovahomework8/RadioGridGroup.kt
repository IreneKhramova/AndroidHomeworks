package com.example.irene.khramovahomework8

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RadioButton
import android.widget.TableLayout
import android.widget.TableRow

class RadioGridGroup : TableLayout, View.OnClickListener {

    private var activeRadioButton: RadioButton? = null
    var onChildClick: OnChildClick? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onClick(view: View) {
        val radioButton =  view as RadioButton
        activeRadioButton?.isChecked = false
        radioButton.isChecked = true
        activeRadioButton = radioButton
        onChildClick?.onChildClick(view)
    }

    override fun addView(child: View, index: Int, params: android.view.ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        setChildrenOnClickListener(child as TableRow)
    }

    override fun addView(child: View, params: android.view.ViewGroup.LayoutParams) {
        super.addView(child, params)
        setChildrenOnClickListener(child as TableRow)
    }

    private fun setChildrenOnClickListener(tableRow: TableRow) {
        var childCount = tableRow.childCount
        while(childCount >= 0) {
            val view = tableRow.getChildAt(childCount)
            if(view is RadioButton) {
                view.setOnClickListener(this)
            }
            childCount--
        }
    }

    fun getCheckedRadioButtonId() : Int {
        return activeRadioButton?.id ?: -1
    }

    interface OnChildClick {
        fun onChildClick(view: View)
    }
}