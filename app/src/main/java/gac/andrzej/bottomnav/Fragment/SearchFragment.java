package gac.andrzej.bottomnav.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gac.andrzej.bottomnav.Adapter.ItemAdapter;
import gac.andrzej.bottomnav.Database.DatabaseHelper;
import gac.andrzej.bottomnav.Model.Item;
import gac.andrzej.bottomnav.R;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private DatabaseHelper databaseHelper;
    private List<Item> itemList;  // Use List of Item objects instead of List<String>
    private SearchView searchView;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration itemDecorator = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider));
        recyclerView.addItemDecoration(itemDecorator);

        searchView = rootView.findViewById(R.id.search_view);

        databaseHelper = new DatabaseHelper(getContext());
        itemList = databaseHelper.getAllItems();  // Fetch all items initially

        // Initialize the adapter with the full item list
        itemAdapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(itemAdapter);

        // Set up search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search when the user submits a query
                searchItems(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform search as the user types
                searchItems(newText);
                return false;
            }
        });

        return rootView;
    }

    // Method to search and update the RecyclerView
    private void searchItems(String searchTerm) {
        // Fetch the search results from the database
        List<Item> filteredList = databaseHelper.searchItems(searchTerm);

        // Update the adapter with the filtered list
        itemAdapter.updateList(filteredList);
    }
}
