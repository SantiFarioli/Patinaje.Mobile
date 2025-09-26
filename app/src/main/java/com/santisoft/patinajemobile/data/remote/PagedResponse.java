package com.santisoft.patinajemobile.data.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PagedResponse<T> {
    @SerializedName("totalItems") public int totalItems;
    @SerializedName("page") public int page;
    @SerializedName("pageSize") public int pageSize;
    @SerializedName("data") public List<T> data;
}

