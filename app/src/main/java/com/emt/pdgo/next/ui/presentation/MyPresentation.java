package com.emt.pdgo.next.ui.presentation;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;

import com.pdp.rmmit.pdp.R;


public class MyPresentation extends Presentation {


    public MyPresentation(Context outerContext, Display display) {
        super(outerContext,display);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_screen);

    }

}
