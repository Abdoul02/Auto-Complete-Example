package com.abdoul.booksearch.model

data class VolumeInfo(
    val allowAnonLogging: Boolean,
    val authors: List<String>,
    val canonicalVolumeLink: String,
    val categories: List<String>,
    val contentVersion: String,
    val description: String,
    val imageLinks: ImageLinks,
    val infoLink: String,
    val language: String,
    val maturityRating: String,
    val panelizationSummary: PanelizationSummary,
    val previewLink: String,
    val printType: String,
    val publisher: String,
    val readingModes: ReadingModes,
    val title: String
)