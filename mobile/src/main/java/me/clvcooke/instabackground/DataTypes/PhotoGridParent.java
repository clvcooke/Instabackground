package me.clvcooke.instabackground.DataTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colin on 2015-05-21.
 */
public class PhotoGridParent {

    private List<ChildItem> childItemList;

    public PhotoGridParent() {
        childItemList = new ArrayList<>();
    }

    public List<ChildItem> getChildItemList() {
        return childItemList;
    }

}
