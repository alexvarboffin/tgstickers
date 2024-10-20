package com.walhalla.stickers;

import com.walhalla.core.Navigator;
import com.walhalla.core.TypeNavItem;

public class NavigationCls {

    public static final Navigator[] mNav0 = {
            //new Navigator(R.id.action_my_dreams, TypeNavItem.TAG_SHOW_CATEGORY, null), //navItemIndex = 1;
            //new Navigator(R.id.action_all_authors, TypeNavItem.TAG_SHOW_AUTHOR, null), //navItemIndex = 1;
            new Navigator(R.id.action_all, TypeNavItem.TAG_SHOW_CATEGORY, null), //navItemIndex = 1;

            new Navigator(R.id.action_favorite_statuses, TypeNavItem.TAG_SHOW_FAVORITE_STATUSES, null),//navItemIndex = 2;
            new Navigator(R.id.action_random_statuses, TypeNavItem.TAG_SHOW_RANDOM_STATUSES, null)//navItemIndex = 3;

    };
}
