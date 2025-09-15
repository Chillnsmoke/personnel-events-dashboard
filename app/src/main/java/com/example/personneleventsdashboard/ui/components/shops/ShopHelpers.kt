package com.example.personneleventsdashboard.ui.components.shops

import com.example.personneleventsdashboard.model.Shop

fun getShopNameById(shopId: Int, shopList: List<Shop>): String {
    return shopList.firstOrNull { it.shopId == shopId }?.name ?: "Unknown"
}