package com.example.loginapp.view.fragments.main_fragment;

import android.os.Bundle;
import android.util.Log;
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

    private final String TAG = this.toString();

    private final MainFragmentPresenter presenter = new MainFragmentPresenter(this);

    private FragmentMainBinding binding;

    private TabLayout tabLayout;

    private final List<Fragment> listOfVerifiedDestinations = new ArrayList<>(Arrays.asList(
            new HomeFragment(),
            new SearchProductFragment(),
            new CartFragment(),
            new FavoriteProductFragment(),
            new UserProfileDetailFragment()
    ));

    private final List<Fragment> listOfUnconfirmedDestinations = new ArrayList<>(Arrays.asList(
            new HomeFragment(),
            new SearchProductFragment()
    ));

    private ViewPager2 viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "onHiddenChanged: ");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("viewpager", this + "onViewCreated: ");
        viewPager = binding.viewPager;
        tabLayout = binding.tabLayout;
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
        NewProductInWishlistMessage message = EventBus.getDefault().getStickyEvent(NewProductInWishlistMessage.class);
        if (message != null) EventBus.getDefault().removeStickyEvent(message);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
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
        if (logged) viewPager.setAdapter(new ViewPagerAdapter(this, listOfVerifiedDestinations));
        else viewPager.setAdapter(new ViewPagerAdapter(this, listOfUnconfirmedDestinations));
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