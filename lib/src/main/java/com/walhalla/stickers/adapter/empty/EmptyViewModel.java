package com.walhalla.stickers.adapter.empty;

import com.walhalla.stickers.adapter.ViewModel;

import java.util.Objects;

public class EmptyViewModel implements ViewModel
{

    public EmptyViewModel(String error) {
        this.error = error;
    }

    public String error;

    @Override
    public int getItemType() {
        return 112;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof EmptyViewModel)) {
            return false;
        }
        EmptyViewModel other = (EmptyViewModel) obj;
        return Objects.equals(error, other.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error);
    }
}
