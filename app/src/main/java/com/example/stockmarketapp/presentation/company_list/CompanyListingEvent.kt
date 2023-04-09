package com.example.stockmarketapp.presentation.company_list

sealed class CompanyListingEvent {
    object Refresh : CompanyListingEvent()
    data class OnSearchQueryChange(val query: String) : CompanyListingEvent()
}
