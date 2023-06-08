package com.seadox.aquamanpro.Interface;

import android.net.Uri;

import com.seadox.aquamanpro.Models.DrillList;

import java.util.ArrayList;

public interface DBCallBack {
    void initAdapter(ArrayList<DrillList> list);
    void saveImageToUser(Uri downloadURi);
}
