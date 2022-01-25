package com.example.hikers.ui.main;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {

            if (input == 1) {
                return "TAB1の内容: ";
            } else if (input == 2) {
                return "TAB2の内容: ";
            }

            //本来起こりえないタブ遷移
            return "Error";
        }
    });

    public void setIndex(int index) {

        mIndex.setValue(index);
    }

    public LiveData<String> getText() {

        return mText;
    }
}