package com.example.smartbox_dup.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SocialLogin extends ViewModel {
    private MutableLiveData<String> socialType = new MutableLiveData<>();
    private MutableLiveData<String> token = new MutableLiveData<>();


    public void setSocialType(String _type) {
        socialType.setValue(_type);
    }

    public String getSocialType() {
        if(socialType.getValue() == null) return "null";
        return socialType.getValue();
    }

    public void setToken(String _token) {
        token.setValue(_token);
    }

    public String getToken() {
        if(token.getValue() == null) return "null";
        return token.getValue();
    }
}
