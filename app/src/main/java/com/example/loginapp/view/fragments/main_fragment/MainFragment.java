package com.example.loginapp.view.fragments.main_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentMainBinding;
import com.example.loginapp.presenter.MainFragmentPresenter;
import com.example.loginapp.view.commonUI.AppConfirmDialog;
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
        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
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
        EventBus.getDefault().removeStickyEvent(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.clear();
        presenter = null;
        listOfVerifiedDestinations = null;
        listOfUnconfirmedDestinations = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (viewPager != null) {
            viewPager.setAdapter(null);
            viewPager = null;
        }
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(null);
            tabLayout = null;
        }
        binding = null;
        viewPagerAdapter = null;
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
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void bindNumberOfProductInShoppingCart(int number, boolean isShoppingCartEmpty) {
        TabLayout.Tab shoppingCartTab = tabLayout.getTabAt(2);
        if (shoppingCartTab != null) {
            BadgeDrawable drawable = shoppingCartTab.getOrCreateBadge();
            if (isShoppingCartEmpty) shoppingCartTab.removeBadge();
            else drawable.setNumber(number);
        }
    }

    @Override
    public void hasNewProductInFavoritesList(boolean hasNewProduct) {
        TabLayout.Tab favoritesListTab = tabLayout.getTabAt(3);
        if (favoritesListTab != null) {
            if (hasNewProduct) favoritesListTab.getOrCreateBadge();
            else favoritesListTab.removeBadge();
        }
    }

    @Override
    public void setAdapter(boolean logged) {
        Log.d(TAG, "setAdapter: " + logged);
        if (logged) {
            viewPagerAdapter = new ViewPagerAdapter(this, listOfVerifiedDestinations);
            viewPager.setOffscreenPageLimit(listOfVerifiedDestinations.size());
            ((HomeFragment) listOfVerifiedDestinations.get(0)).setMainViewpager(viewPager);
        } else {
            viewPagerAdapter = new ViewPagerAdapter(this, listOfUnconfirmedDestinations);
            viewPager.setOffscreenPageLimit(listOfUnconfirmedDestinations.size());
            ((HomeFragment) listOfUnconfirmedDestinations.get(0)).setMainViewpager(viewPager);
        }
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void showLoginPopup() {
        AppConfirmDialog.show(
                requireContext(),
                getString(R.string.log_in),
                getString(R.string.login_popup_message),
                new AppConfirmDialog.AppConfirmDialogButtonListener() {
                    @Override
                    public void onPositiveButtonClickListener() {
                        NavHostFragment.findNavController(MainFragment.this).navigate(R.id.overviewFragment);
                    }

                    @Override
                    public void onNegativeButtonClickListener() {

                    }
                }
        );
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