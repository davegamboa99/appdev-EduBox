package com.elearneur.edubox.ui.bottom_nav;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BottomNavViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BottomNavViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is bottom nav fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}