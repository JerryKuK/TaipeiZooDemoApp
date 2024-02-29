package com.jerrypeng31.taipeizoodemoapp.mvvm.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AreaApiModel(
    @SerializedName("result")
    val result: Result?
) {
    data class Result(
        @SerializedName("count")
        val count: Int?,
        @SerializedName("limit")
        val limit: Int?,
        @SerializedName("offset")
        val offset: Int?,
        @SerializedName("results")
        val results: List<AreaDataResult?>?,
        @SerializedName("sort")
        val sort: String?
    ) {
        data class AreaDataResult(
            @SerializedName("e_category")
            val eCategory: String?,
            @SerializedName("e_geo")
            val eGeo: String?,
            @SerializedName("e_info")
            val eInfo: String?,
            @SerializedName("e_memo")
            val eMemo: String?,
            @SerializedName("e_name")
            val eName: String?,
            @SerializedName("e_no")
            val eNo: String?,
            @SerializedName("e_pic_url")
            val ePicURL: String?,
            @SerializedName("e_url")
            val eURL: String?,
            @SerializedName("_id")
            val id: Int?
        ): Serializable
    }
}