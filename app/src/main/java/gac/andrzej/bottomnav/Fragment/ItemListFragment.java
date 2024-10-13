package gac.andrzej.bottomnav.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import gac.andrzej.bottomnav.Adapter.ItemAdapter;
import gac.andrzej.bottomnav.Database.DatabaseHelper;
import gac.andrzej.bottomnav.Model.Item;
import gac.andrzej.bottomnav.R;
import gac.andrzej.bottomnav.RecyclerTouchListener;

public class ItemListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private DatabaseHelper databaseHelper;
    private List<Item> itemList;

    public ItemListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration itemDecorator = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider));
        recyclerView.addItemDecoration(itemDecorator);

        /*DividerItemDecoration itemDecorator2 = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        itemDecorator2.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider_vertical));
        recyclerView.addItemDecoration(itemDecorator2);*/


        databaseHelper = new DatabaseHelper(getContext());
        itemList = databaseHelper.getAllItems();

        itemAdapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(itemAdapter);

        FloatingActionButton addButton = rootView.findViewById(R.id.button_add_item);
        addButton.setOnClickListener(v -> {
            //databaseHelper.addItem("kakao", 23.99F,"best product","aaa");
            itemList.clear();
            itemList.addAll(databaseHelper.getAllItems());
            itemAdapter.notifyDataSetChanged();
            showAddDialog();
        });

        // Adding swipe-to-delete functionality
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // We don't want drag & drop functionality
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Get the position of the swiped item
                int position = viewHolder.getAdapterPosition();

                // Get the item ID from the swiped position
                Item item = itemList.get(position);
                int itemId = item.getId();

                // Delete the item from the database
                databaseHelper.deleteItem(itemId);

                // Remove the item from the list
                itemList.remove(position);

                // Notify the adapter that an item was removed
                itemAdapter.notifyItemRemoved(position);
            }
        };

        // Attach the ItemTouchHelper to the RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Handle item update on long click
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Optional: Handle single click for other actions if needed
            }

            @Override
            public void onLongClick(View view, final int position) {
                // Show the update dialog when an item is long-clicked
                showUpdateDialog(position);
            }
        }));

        return rootView;
    }

    private void showAddDialog() {
       // final Item currentItem = null;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_update_item, null);

        EditText nameInput = dialogView.findViewById(R.id.edit_item_name);
        EditText priceInput = dialogView.findViewById(R.id.edit_item_price);
        EditText descriptionInput = dialogView.findViewById(R.id.edit_item_description);
        EditText pictureInput = dialogView.findViewById(R.id.edit_item_picture);

        // Set current values
//        nameInput.setText(currentItem.getName());
//        priceInput.setText(String.valueOf(currentItem.getPrice()));
//        descriptionInput.setText(currentItem.getDescription());
//        pictureInput.setText(currentItem.getPicture());

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle("Add new product")
                .setView(dialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get updated values
                        String newName = nameInput.getText().toString();
                        float newPrice = Float.parseFloat(priceInput.getText().toString());
                        String newDescription = descriptionInput.getText().toString();
                        String newPicture = pictureInput.getText().toString();

                        // Add the item in the database using individual fields
                        databaseHelper.addItem(newName,newPrice,newDescription,newPicture);

                        itemList.clear();
                        itemList.addAll(databaseHelper.getAllItems());
                        itemAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", null);

        dialogBuilder.show();
    }

    // Method to show a dialog for updating an item
    // Inside ItemListFragment.java
    private void showUpdateDialog(final int position) {
        // Get the current item based on its position
        final Item currentItem = itemList.get(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_update_item, null);

        EditText nameInput = dialogView.findViewById(R.id.edit_item_name);
        EditText priceInput = dialogView.findViewById(R.id.edit_item_price);
        EditText descriptionInput = dialogView.findViewById(R.id.edit_item_description);
        EditText pictureInput = dialogView.findViewById(R.id.edit_item_picture);

        // Set current values
        nameInput.setText(currentItem.getName());
        priceInput.setText(String.valueOf(currentItem.getPrice()));
        descriptionInput.setText(currentItem.getDescription());
        pictureInput.setText(currentItem.getPicture());

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle("Update product")
                .setView(dialogView)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get updated values
                        String newName = nameInput.getText().toString();
                        float newPrice = Float.parseFloat(priceInput.getText().toString());
                        String newDescription = descriptionInput.getText().toString();
                        String newPicture = pictureInput.getText().toString();

                        // Update the item in the database using individual fields
                        databaseHelper.updateItem(currentItem.getId(), newName, newPrice, newDescription, newPicture);

                        // Refresh the item list
                        itemList.set(position, new Item(currentItem.getId(), newName, newPrice, newDescription, newPicture));
                        itemAdapter.notifyItemChanged(position);
                    }
                })
                .setNegativeButton("Cancel", null);

        dialogBuilder.show();
    }







}