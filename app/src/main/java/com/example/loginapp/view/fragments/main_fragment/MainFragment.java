package com.example.loginapp.view.fragments.main_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentMainBinding;
import com.example.loginapp.presenter.MainFragmentPresenter;
import com.example.loginapp.view.fragments.cart.CartFragment;
import com.example.loginapp.view.fragments.favorite_product.FavoriteProductFragment;
import com.example.loginapp.view.fragments.home.HomeFragment;
import com.example.loginapp.view.fragments.product_detail.NewProductInWishlistMessage;
import com.example.loginapp.view.fragments.search.SearchProductFragment;
import com.example.loginapp.view.fragments.user_profile.UserProfileDetailFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainFragment extends Fragment implements MainFragmentView {

    private static final String TAG = MainFragment.class.getSimpleName();

    private MainFragmentPresenter presenter;

    private FragmentMainBinding binding;

    private TabLayout tabLayout;

    private List<Fragment> listOfVerifiedDestinations;

    private List<Fragment> listOfUnconfirmedDestinations;

    private ViewPagerAdapter viewPagerAdapter;

    private ViewPager2 viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainFragmentPresenter(this);
        listOfVerifiedDestinations = new ArrayList<>(Arrays.asList(
                new HomeFragment(),
                new SearchProductFragment(),
                new CartFragment(),
                new FavoriteProductFragment(),
                new UserProfileDetailFragment()
        ));

        listOfUnconfirmedDestinations = new ArrayList<>(Arrays.asList(
                new HomeFragment(),
                new SearchProductFragment()
        ));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        viewPager = binding.viewPager;
        tabLayout = binding.tabLayout;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            presenter.initData();
            viewPager.setOffscreenPageLimit(5);
            viewPager.setUserInputEnabled(false);
            tabSelectedListener();
            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    tabLayout.getTabAt(position).select();
                }
            });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getWishlistStatus(NewProductInWishlistMessage message) {
        presenter.viewedFavoritesList(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
        listOfVerifiedDestinations = null;
        listOfUnconfirmedDestinations = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        viewPager = null;
        tabLayout = null;
        viewPagerAdapter = null;
        NewProductInWishlistMessage message = EventBus.getDefault().getStickyEvent(NewProductInWishlistMessage.class);
        if (message != null) EventBus.getDefault().removeStickyEvent(message);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.addValueEventListener();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.removeValueEventListener();
        EventBus.getDefault().unregister(this);
    }

    private void tabSelectedListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
                if (position == 3) presenter.viewedFavoritesList(true);
                int res = 0;
                switch (position) {
                    case 0:
                        res = R.drawable.ichome;
                        break;
                    case 1:
                        res = R.drawable.ic_search_dark;
                        break;
                    case 2:
                        res = R.drawable.ic_cart_dark;
                        break;
                    case 3:
                        res = R.drawable.favorite;
                        break;
                    case 4:
                        res = R.drawable.ic_user_dark;
                        break;
                }
                tab.setIcon(res);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                int res = 0;
                switch (position) {
                    case 0:
                        res = R.drawable.ic_home_gray;
                        break;
                    case 1:
                        res = R.drawable.ic_search_gray;
                        break;
                    case 2:
                        res = R.drawable.ic_cart_gray;
                        break;
                    case 3:
                        res = R.drawable.ic_favorite_gray;
                        break;
                    case 4:
                        res = R.drawable.ic_user_gray;
                        break;
                }
                tab.setIcon(res);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void bindNumberOfProductInShoppingCart(int number, boolean isShoppingCartEmpty) {
        TabLayout.Tab shoppingCartTab = tabLayout.getTabAt(2);
        BadgeDrawable drawable = shoppingCartTab.getOrCreateBadge();
        if (isShoppingCartEmpty) shoppingCartTab.removeBadge();
        else drawable.setNumber(number);
    }

    @Override
    public void hasNewProductInFavoritesList(boolean hasNewProduct) {
        TabLayout.Tab favoritesListTab = tabLayout.getTabAt(3);
        if (hasNewProduct) favoritesListTab.getOrCreateBadge();
        else favoritesListTab.removeBadge();
    }

    @Override
    public void setAdapter(boolean logged) {
        if (logged) viewPagerAdapter = new ViewPagerAdapter(this, listOfVerifiedDestinations);
        else viewPagerAdapter = new ViewPagerAdapter(this, listOfUnconfirmedDestinations);
        viewPager.setAdapter(viewPagerAdapter);
    }

    public static class ViewPagerAdapter extends FragmentStateAdapter {

        private final List<Fragment> fragments;

        public ViewPagerAdapter(@NonNull Fragment fragment, List<Fragment> fragments) {
            super(fragment);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }

    }
}