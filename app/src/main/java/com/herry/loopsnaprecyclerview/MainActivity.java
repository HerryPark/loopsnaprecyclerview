package com.herry.loopsnaprecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.herry.loopsnaprecyclerview.sample.AutoLoopPagerActivity;
import com.herry.loopsnaprecyclerview.sample.LoopPagerActivity;
import com.herry.loopsnaprecyclerview.sample.PagerActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<ListItemID> items = new ArrayList<>();
        items.add(ListItemID.NORMAL);
        items.add(ListItemID.LOOP);
        items.add(ListItemID.AUTO_LOOP);

        RecyclerView recyclerView = findViewById(R.id.list);
        if (null != recyclerView) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setItemAnimator(null);

            recyclerView.setAdapter(new Adapter(items));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private enum ListItemID {
        NORMAL,
        LOOP,
        AUTO_LOOP
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
        @NonNull
        private final List<ListItemID> items = new ArrayList<>();

        Adapter(List<ListItemID> items) {
            this.items.clear();
            if (null != items) {
                this.items.addAll(items);
            }

            notifyDataSetChanged();
        }

        private ListItemID getItem(int position) {
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
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            ListItemID item = getItem(position);

            String text = "";
            if (null != item) {
                switch (item) {
                    case NORMAL: {
                        text = "Normal Pager";
                        break;
                    }
                    case LOOP: {
                        text = "Manual Looping Pager";
                        break;
                    }
                    case AUTO_LOOP: {
                        text = "Automatic Looping Pager";
                        break;
                    }
                }
            }
            if (null != holder.text) {
                holder.text.setText(text);
            }
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView text;
            Holder(@NonNull View itemView) {
                super(itemView);

                text = itemView.findViewById(R.id.list_item_text);

                itemView.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ListItemID item = getItem(Holder.this.getAdapterPosition());
                                if (null != item) {
                                    Intent intent = null;
                                    switch (item) {
                                        case NORMAL: {
                                            intent = new Intent(MainActivity.this, PagerActivity.class);
                                            break;
                                        }
                                        case LOOP: {
                                            intent = new Intent(MainActivity.this, LoopPagerActivity.class);
                                            break;
                                        }
                                        case AUTO_LOOP: {
                                            intent = new Intent(MainActivity.this, AutoLoopPagerActivity.class);
                                            break;
                                        }
                                    }

                                    startActivity(intent);
                                }
                            }
                        }
                );
            }
        }
    }
}
