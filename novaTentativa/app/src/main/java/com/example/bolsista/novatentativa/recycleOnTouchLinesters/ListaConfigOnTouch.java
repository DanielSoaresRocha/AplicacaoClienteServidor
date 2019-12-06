package com.example.bolsista.novatentativa.recycleOnTouchLinesters;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListaConfigOnTouch implements RecyclerView.OnItemTouchListener {
    GestureDetector myGestureDetector;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public ListaConfigOnTouch(Context context, RecyclerView recyclerView, OnItemClickListener listener){
        myGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                super.onSingleTapUp(motionEvent);
                View childView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (childView != null){
                    listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                    Log.i("Teste", "onSingleTapUp");
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                super.onLongPress(motionEvent);
                View childView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if(childView != null){
                    listener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                    Log.i("Teste", "onLongPress");
                }
            }
        });
    }


    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        myGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
