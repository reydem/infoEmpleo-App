package com.example.infoempleo.vacantes.data.network

import com.google.gson.annotations.SerializedName

data class PaginatedResponse<T>(
    @SerializedName("docs")
    val docs: List<T>,

    @SerializedName("totalDocs")
    val totalDocs: Int,

    @SerializedName("limit")
    val limit: Int,

    @SerializedName("page")
    val page: Int,

    @SerializedName("totalPages")
    val totalPages: Int,

    @SerializedName("hasPrevPage")
    val hasPrevPage: Boolean,

    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,

    @SerializedName("prevPage")
    val prevPage: Int?,

    @SerializedName("nextPage")
    val nextPage: Int?
)
