package gac.andrzej.bottomnav;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

import gac.andrzej.bottomnav.Fragment.GalleryFragment;
import gac.andrzej.bottomnav.Fragment.HomeFragment;
import gac.andrzej.bottomnav.Fragment.ItemListFragment;
import gac.andrzej.bottomnav.Fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private Map<Integer, Fragment> fragmentMap;  // Store fragment instances

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize fragments once and store in a map
        fragmentMap = new HashMap<>();
        fragmentMap.put(R.id.nav_home, new HomeFragment());
        fragmentMap.put(R.id.nav_gallery, new GalleryFragment());
        fragmentMap.put(R.id.nav_search, new SearchFragment());
        fragmentMap.put(R.id.nav_item_list, new ItemListFragment());

        // Handle window insets for edge-to-edge experience
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load default fragment on first creation
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
            loadFragment(fragmentMap.get(R.id.nav_home));
        }

        // Handle bottom navigation item clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = fragmentMap.get(item.getItemId());
            return loadFragment(selectedFragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        // Switch to the selected fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)  // Allow optimized fragment reordering
                    .replace(R.id.main_content, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
