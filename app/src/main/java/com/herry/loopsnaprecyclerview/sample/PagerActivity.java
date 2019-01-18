package com.herry.loopsnaprecyclerview.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.herry.loopsnaprecyclerview.R;
import com.herry.loopsnaprecyclerview.lib.PagerSnapExHelper;

import java.util.ArrayList;
import java.util.List;

public class PagerActivity extends AppCompatActivity {
    private TextView indicator = null;
    private PagerSnapExHelper snapHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Normal Pager");
        setContentView(R.layout.activity_pager);

        List<String> items = new ArrayList<>();
        for (int index = 0; index < 3; index++) {
            items.add(String.valueOf(index + 1));
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        if (null != recyclerView) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setItemAnimator(null);

            recyclerView.setAdapter(new Adapter(items));

            snapHelper = new PagerSnapExHelper();
            snapHelper.setOnSnappedListener(
                    new PagerSnapExHelper.OnSnappedListener() {
                        @Override
                        public void onSnapped(int position, int itemCount) {
                            if (null == snapHelper || null == snapHelper.getRecyclerView() || null == snapHelper.getRecyclerView().getAdapter()) {
                                return;
                            }

                            updateIndicator(position, itemCount);
                        }

                        @Override
                        public void onUnsnapped(int position, int itemCount) {
                        }
                    }
            );
            snapHelper.attachToRecyclerView(recyclerView);
        }

        indicator = findViewById(R.id.indicator);

        View previous = findViewById(R.id.previous);
        if (null != previous) {
            previous.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != snapHelper) {
                                snapHelper.snapToPrevious();
                            }
                        }
                    }
            );
        }
        View next = findViewById(R.id.next);
        if (null != next) {
            next.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != snapHelper) {
                                snapHelper.snapToNext();
                            }
                        }
                    }
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null != snapHelper) {
            snapHelper.scrollToSnapPosition(0);
        }
    }

    private void updateIndicator(int position, int total) {
        if (null == indicator) {
            return;
        }

        indicator.setText(String.valueOf(position + 1).concat("/").concat(String.valueOf(total)));
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
        Adapter(List<String> items) {
            setItems(items);
        }

        @NonNull
        private final List<String> items = new ArrayList<>();
        private void setItems(List<String> items) {
            this.items.clear();
            this.items.addAll(items);

            notifyDataSetChanged();
        }

        private String getItem(int position) {
            if (0 <= position && position < items.size()) {
                return items.get(position);
            }

            return null;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.page, viewGroup, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            String item = getItem(position);
            if (null != holder.text) {
                holder.text.setText(item);
            }
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView text;
            Holder(@NonNull View itemView) {
                super(itemView);

                text = itemView.findViewById(R.id.page_text);
            }
        }
    }
}
