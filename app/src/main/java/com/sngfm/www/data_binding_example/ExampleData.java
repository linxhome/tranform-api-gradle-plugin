package com.sngfm.www.data_binding_example;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by juliandai
 * on 2017/3/31.
 */

public class ExampleData extends BaseObservable{
    String userName;
    String password;
    String defaultPass = "123456";

    @Bindable
    public String getUserName() {
        return userName;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(BR.userName);
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    public String getDefaultPass() {
        return defaultPass;
    }

    public void setDefaultPass(String defaultPass) {
        this.defaultPass = defaultPass;
    }

    public ExampleData(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }


}
