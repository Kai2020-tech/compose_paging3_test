package com.example.compose_paging3_test.data.dto.home

import com.google.gson.annotations.SerializedName


data class HotTagsDto(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("total")
    val total: Int // 36
) {

    data class Data(
        @SerializedName("book")
        val book: Book?,
        @SerializedName("count")
        val count: Int, // 36
        @SerializedName("name")
        val name: String // pekopeko
    ) {

        data class Book(
            @SerializedName("cover_photo")
            val coverPhoto: String, // https://storage.googleapis.com/storage-cxc-dev/cxc-fs/book/61/coverphoto1639124526-sm.jpg
            @SerializedName("id")
            val id: Int, // 61
            @SerializedName("name")
            val name: String, // 永遠的免費作品2
            @SerializedName("read_count")
            val readCount: Int, // 2
            @SerializedName("store_id")
            val storeId: Int // 5
        )
    }
}