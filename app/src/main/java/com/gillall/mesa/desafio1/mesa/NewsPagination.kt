package com.gillall.mesa.desafio1.mesa

data class NewsPagination(
    val current_page: Int,
    val per_page: Int,
    val total_pages: Int,
    val total_items: Int
) {
}