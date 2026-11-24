package com.example.exam2;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ModeActivity extends AppCompatActivity {

    private static final String[] TAB_TITLES = {"First", "Second", "Third"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.modeRoot),
                (view, insets) -> {
                    Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                    return insets;
                });

        MaterialToolbar toolbar = findViewById(R.id.modeToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TabLayout tabs = findViewById(R.id.modeTabs);
        ViewPager2 pager = findViewById(R.id.modePager);

        pager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return TabPageFragment.newInstance(TAB_TITLES[position]);
            }

            @Override
            public int getItemCount() {
                return TAB_TITLES.length;
            }
        });

        new TabLayoutMediator(tabs, pager,
                (tab, position) -> tab.setText(TAB_TITLES[position])).attach();

        findViewById(R.id.btnSetMode).setOnClickListener(this::showModeMenu);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (pager.getCurrentItem() == 0) {
                    finish();
                } else {
                    pager.setCurrentItem(0, false);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void showModeMenu(View anchor) {
        PopupMenu menu = new PopupMenu(this, anchor);

        menu.getMenu().add("Light").setOnMenuItemClickListener(item -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            return true;
        });

        menu.getMenu().add("Night").setOnMenuItemClickListener(item -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            return true;
        });

        menu.getMenu().add("System").setOnMenuItemClickListener(item -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            return true;
        });

        menu.show();
    }

    public static class TabPageFragment extends Fragment {

        public static TabPageFragment newInstance(String title) {
            Bundle arguments = new Bundle();
            arguments.putString("title", title);

            TabPageFragment fragment = new TabPageFragment();
            fragment.setArguments(arguments);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(
                @NonNull LayoutInflater inflater,
                @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState
        ) {
            TextView pageText = new TextView(requireContext());
            pageText.setGravity(Gravity.CENTER);
            pageText.setTextSize(28);
            pageText.setText(requireArguments().getString("title"));
            return pageText;
        }
    }
}