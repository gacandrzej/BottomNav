package gac.andrzej.bottomnav.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import gac.andrzej.bottomnav.Adapter.ItemAdapter;
import gac.andrzej.bottomnav.Database.DatabaseHelper;
import gac.andrzej.bottomnav.R;
import gac.andrzej.bottomnav.RecyclerTouchListener;


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
        addButton.setOnClickListener(v -> {
            databaseHelper.addItem("New Item " + (itemList.size() + 1));
            itemList.clear();
            itemList.addAll(databaseHelper.getAllItems());
            itemAdapter.notifyDataSetChanged();
        });

        // Handle item update on long click
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Normal click, we ignore for now
            }

            @Override
            public void onLongClick(View view, final int position) {
                // Show update dialog
                showUpdateDialog(position);
            }
        }));

        return rootView;
    }

    // Method to show a dialog for updating an item
    private void showUpdateDialog(final int position) {
        final EditText input = new EditText(getContext());
        input.setHint("Enter new name");

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle("Update Item")
                .setView(input)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newItemName = input.getText().toString();
                        // Update the item in the database
                        int id = position + 1; // Assuming the position corresponds to the ID, adjust if needed
                        databaseHelper.updateItem(id, newItemName);

                        // Refresh the list after update
                        itemList.clear();
                        itemList.addAll(databaseHelper.getAllItems());
                        itemAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", null);

        dialogBuilder.show();
    }


}