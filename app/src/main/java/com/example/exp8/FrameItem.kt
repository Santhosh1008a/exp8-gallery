package com.example.exp8

data class FrameItem(
    val id: Int,
    val title: String,
    val drawableResId: Int,
    val filename: String,
    val description: String,
    val category: String = "Mixed",
    var isSelected: Boolean = false
)