package com.example.mychatapplication.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapplication.Adapters.AdapterUsers;
import com.example.mychatapplication.Models.UsersModel;
import com.example.mychatapplication.R;
import com.example.mychatapplication.UI.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.mychatapplication.UI.RegisterActivity.USERS_REF;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    RecyclerView usersRecyclerView;
    List<UsersModel> userList;
    AdapterUsers adapterUsers;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        initView(view);
        usersRecyclerView.setHasFixedSize(true);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        getAllUsers(null);

        return view;
    }

    private void getAllUsers(final String query) {
        final FirebaseUser fUsers = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(USERS_REF);
        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("Assert")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    userList.clear();
                    for (DataSnapshot ds:
                            dataSnapshot.getChildren()) {
                        UsersModel usersModel = ds.getValue(UsersModel.class);
                        if (!usersModel.getUid().equals(fUsers.getUid())){
                            if (usersModel.getName().toLowerCase().contains(query.toLowerCase())
                            || usersModel.getEmail().toLowerCase().contains(query.toLowerCase()))
                            userList.add(usersModel);
                        }
                        adapterUsers = new AdapterUsers(getActivity(), userList);
                        adapterUsers.notifyDataSetChanged();
                        usersRecyclerView.setAdapter(adapterUsers);
                    }
                } catch (Exception e){
                    userList.clear();
                    for (DataSnapshot ds:
                            dataSnapshot.getChildren()) {
                        UsersModel usersModel = ds.getValue(UsersModel.class);
                        if (!usersModel.getUid().equals(fUsers.getUid())){
                            userList.add(usersModel);
                        }
                        adapterUsers = new AdapterUsers(getActivity(), userList);
                        adapterUsers.notifyDataSetChanged();
                        usersRecyclerView.setAdapter(adapterUsers);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView(View rootView) {
        usersRecyclerView = (RecyclerView) rootView.findViewById(R.id.users_recyclerView);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!TextUtils.isEmpty(query.trim())){
                    getAllUsers(query);
                } else {
                    getAllUsers(null);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText.trim())){
                    getAllUsers(newText);
                } else {
                    getAllUsers(null);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_logout:
                firebaseAuth.signOut();
                checkUserStatus();
                break;

            case R.id.action_search:

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //stay here

        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }
}
