package com.forntoh.EasyRecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

@SuppressWarnings("unused")
public class EasyRecyclerView {

    private boolean dividers;
    private int span = -1;
    private Type type;
    private RecyclerView rv;
    private RecyclerView.Adapter adapter;
    private InsetDecoration decoration;

    public EasyRecyclerView() {
    }

    public EasyRecyclerView setAdapter(@NonNull RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public EasyRecyclerView setRecyclerView(@NonNull RecyclerView rv) {
        this.rv = rv;
        return this;
    }

    public EasyRecyclerView setType(@NonNull Type type) {
        this.type = type;
        return this;
    }

    public EasyRecyclerView setSpan(int span) {
        this.span = span;
        return this;
    }

    public EasyRecyclerView enableDividers(boolean dividers) {
        this.dividers = dividers;
        return this;
    }

    public EasyRecyclerView setItemSpacing(int itemSpacing, InsetDecoration.Sides sides) {
        if (itemSpacing < 0)
            throw new IndexOutOfBoundsException("Only positive values allowed");
        this.decoration = new InsetDecoration(rv.getContext(), itemSpacing);
        this.decoration.applyTo(sides);
        return this;
    }

    public void initialize() {
        if (adapter == null)
            throw new NullPointerException("The adapter for the RecyclerView has not been defined : setAdapter(RecyclerView.Adapter adapter)");
        else if (rv == null)
            throw new NullPointerException("The RecyclerView has not been defined : setRecyclerView(RecyclerView rv)");
        else
            switch (type) {
                case GRID:
                    if (span < 0)
                        throw new IllegalArgumentException("Grid span has not been defined : setSpan(int span)");
                    else setupDisplay(type);
                    break;
                case VERTICAL:
                    setupDisplay(type);
                    break;
                case HORIZONTAL:
                    setupDisplay(type);
                    break;
                default:
                    throw new EnumConstantNotPresentException(Type.class, "Type");
            }
    }

    private void setupDisplay(Type type) {
        RecyclerView.LayoutManager layoutManager;
        switch (type) {
            case GRID:
                layoutManager = new GridLayoutManager(rv.getContext(), span);
                if (dividers) {
                    rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), LinearLayoutManager.VERTICAL));
                    rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), LinearLayoutManager.HORIZONTAL));
                }
                break;
            case VERTICAL:
                layoutManager = new LinearLayoutManager(rv.getContext(), LinearLayoutManager.VERTICAL, false);
                if (dividers)
                    rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), LinearLayoutManager.VERTICAL));
                break;
            default:
                layoutManager = new LinearLayoutManager(rv.getContext(), LinearLayoutManager.HORIZONTAL, false);
        }

        rv.setLayoutManager(layoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
        rv.setNestedScrollingEnabled(false);
        if (decoration != null) rv.addItemDecoration(decoration);
    }

    public Thread initialize(int listSize, int loopDuration, boolean snapToCenter) {
        setupDisplay(this.type);
        Thread thread;
        if (snapToCenter) {
            SnapHelper helper = new LinearSnapHelper();
            helper.attachToRecyclerView(rv);
        }
        if (loopDuration > 0) {
            thread = new Thread(() -> {
                while (true) {
                    try {
                        LinearLayoutManager mL = (LinearLayoutManager) rv.getLayoutManager();
                        int pos = mL.findLastCompletelyVisibleItemPosition();
                        rv.smoothScrollToPosition(pos == listSize - 1 ? 0 : pos + 1);
                        Thread.sleep(loopDuration);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            });
            thread.start();
            thread.setName("Horizontal Scroll:" + rv.getId());
        } else return null;
        return thread;
    }

    public enum Type {
        GRID, VERTICAL, HORIZONTAL
    }
}
