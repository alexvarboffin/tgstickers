package com.walhalla.core;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

@StringDef({
        TypeNavItem.TAG_SHOW_CATEGORY, TypeNavItem.TAG_SHOW_AUTHOR,
        TypeNavItem.__TAG_CATEGORY_LIST,
        TypeNavItem.TAG_SHOW_FAVORITE_STATUSES,
        TypeNavItem.TAG_SHOW_RANDOM_STATUSES,
})
@Retention(SOURCE)
public @interface TypeNavItem {
    String TAG_SHOW_CATEGORY = "home";
    String TAG_SHOW_AUTHOR = "authors";

    String __TAG_CATEGORY_LIST = "tag_dictionary";
    String TAG_SHOW_FAVORITE_STATUSES = "tag_favorite";
    String TAG_SHOW_RANDOM_STATUSES = "tag_random";
}
