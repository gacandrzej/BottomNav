package gac.andrzej.bottomnav.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import gac.andrzej.bottomnav.Adapter.ItemAdapter;
import gac.andrzej.bottomnav.DatabaseHelper;
import gac.andrzej.bottomnav.R;


public class ItemListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private DatabaseHelper databaseHelper;
    private List<String> itemList;


    public ItemListFragment() {
        // Required empty public constructor
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DatabaseHelper(getContext());
        itemList = databaseHelper.getAllItems();

        itemAdapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(itemAdapter);

        Button addButton = rootView.findViewById(R.id.button_add_item);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.addItem("New Item " + (itemList.size() + 1));
                itemList.clear();
                itemList.addAll(databaseHelper.getAllItems());
                itemAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }
}