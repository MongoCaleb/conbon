package ca13b.conbon.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.todo.R;
import ca13b.conbon.model.Item;

public class TasksRecyclerAdapter extends RealmRecyclerViewAdapter<Item, TasksRecyclerAdapter.MyViewHolder> {

    public TasksRecyclerAdapter(OrderedRealmCollection<Item> data) {
        super(data, true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Item item = getItem(position);
        holder.setItem(item);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        CheckBox checkBox;
        Item item;

        MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.body);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setOnClickListener(this);
        }

        void setItem(Item item){
            this.item = item;
            this.textView.setText(item.getBody());
           // this.checkBox.setChecked(item.getState());
        }

        @Override
        public void onClick(View v) {
            String itemId = item.getItemId();
           // boolean isDone = this.item.getIsDone();
            this.item.getRealm().executeTransactionAsync(realm -> {
                Item item = realm.where(Item.class).equalTo("itemId", itemId).findFirst();
                if (item != null) {
                   // item.setIsDone(!isDone);
                }
            });
        }
    }
}
