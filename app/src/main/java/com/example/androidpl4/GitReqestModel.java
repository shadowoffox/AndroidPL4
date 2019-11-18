package com.example.androidpl4;

import com.google.gson.annotations.SerializedName;

public class GitReqestModel {

@SerializedName("name") String name;
@SerializedName("html_url") String htmlUrl;

public String getName(){
    return name;
}

public String getHtmlUrl(){
    return htmlUrl;
}

}
