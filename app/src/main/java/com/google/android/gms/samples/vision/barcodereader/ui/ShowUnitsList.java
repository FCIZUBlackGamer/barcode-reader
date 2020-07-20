package com.google.android.gms.samples.vision.barcodereader.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavArgs;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.samples.vision.barcodereader.databinding.FragmentShowUnitsListBinding;
import com.google.android.gms.samples.vision.barcodereader.databinding.UnitListItemBinding;
import com.google.android.gms.samples.vision.barcodereader.model.accept_service_request_model.Product;
import com.google.android.gms.samples.vision.barcodereader.ui.display_and_validate_units.DisplayAndValidateUnitsViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowUnitsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowUnitsList extends Fragment {
    UnitsListAdapter unitsListAdapter;
    FragmentShowUnitsListBinding binding;
    ArrayList<Product> productArrayList = new ArrayList<>();
    private static final String KEY_PRODUCTS_LIST = "productList";
    NavController navController;
    DisplayAndValidateUnitsViewModel viewModel;

    public ShowUnitsList() {
        // Required empty public constructor
    }


    public static ShowUnitsList newInstance() {
        ShowUnitsList fragment = new ShowUnitsList();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null)
            productArrayList = getArguments().getParcelableArrayList(KEY_PRODUCTS_LIST);
        if(savedInstanceState!=null)
            productArrayList = savedInstanceState.getParcelableArrayList(KEY_PRODUCTS_LIST);

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_units_list, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.show_units_list_fragment);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.unitListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        unitsListAdapter = new UnitsListAdapter(getContext(), productArrayList);
        binding.unitListRecyclerView.setAdapter(unitsListAdapter);
        binding.showAddDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_showUnitsList2_to_showAndAddUnitDialog);
            }
        });

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultReceived(Product product) {
        productArrayList.add(product);
        unitsListAdapter.notifyDataSetChanged();
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_PRODUCTS_LIST,productArrayList);
    }
}
