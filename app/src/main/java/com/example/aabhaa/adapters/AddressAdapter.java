package com.example.aabhaa.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aabhaa.R;
import com.example.aabhaa.controllers.AddressController;
import com.example.aabhaa.models.Address;
import com.example.aabhaa.views.AddAddressActivity;
import com.example.aabhaa.views.AddressActivity;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private List<Address> addressList;
    private Context context;

    private AddressController addressController;

    public AddressAdapter(Context context, List<Address> addressList) {
        this.context = context;
        this.addressList = addressList;
        if (context instanceof FragmentActivity) {
            addressController = new AddressController((FragmentActivity) context, context);
            addressController.setAdapter(this);  // set adapter here once!
        }

    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addressList.get(position);
        holder.tvAddressTitle.setText(address.getTitle());
        holder.tvAddressId.setText("ID: " + address.getId());
        holder.tvProvince.setText("Province: " + address.getProvince());
        holder.tvDistrict.setText("District: " + address.getDistrict());
//        Log.d("AdapterBind", "ID: " + address.getId() + ", Title: " + address.getTitle() + ", Lat: " + address.getLatitude() + ", Lng: " + address.getLongitude());
        holder.btnEditAddress.setOnClickListener(v->{
            Intent intent = new Intent(context , AddAddressActivity.class);
            intent.putExtra("id", address.getId()); // Pass data as needed
            intent.putExtra("title", address.getTitle());
            intent.putExtra("province", address.getProvince());
            intent.putExtra("district", address.getDistrict());
            intent.putExtra("latitude", address.getLatitude());
            intent.putExtra("longitude",address.getLongitude());
            intent.putExtra("description",address.getDescription());

            context.startActivity(intent);
        });

        holder.btnViewAddress.setOnClickListener(v -> {
            ((Activity)context).runOnUiThread(() -> {
                showAddressDetailsDialog(context, addressList.get(position));
            });
        });

        holder.btnDeleteAddress.setOnClickListener(v -> {
            if (addressController != null) {
                addressController.deleteAddress(position, address);
            } else {
                Toast.makeText(context, "Unable to delete. Invalid controller.", Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddressTitle, tvAddressId, tvProvince, tvDistrict;
        ImageButton btnEditAddress, btnViewAddress, btnDeleteAddress;;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddressTitle = itemView.findViewById(R.id.tvAddressTitle);
            tvAddressId = itemView.findViewById(R.id.tvAddressId);
            tvProvince = itemView.findViewById(R.id.tvProvince);
            tvDistrict = itemView.findViewById(R.id.tvDistrict);
            btnEditAddress = itemView.findViewById(R.id.btnEditAddress);
            btnViewAddress = itemView.findViewById(R.id.btnViewAddress);
            btnDeleteAddress = itemView.findViewById(R.id.btnDeleteAddress);
        }
    }

    public void showAddressDetailsDialog(Context context, Address address) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_address_details);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        // Bind views
        TextView tvId = dialog.findViewById(R.id.tvId);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        TextView tvProvince = dialog.findViewById(R.id.tvProvince);
        TextView tvDistrict = dialog.findViewById(R.id.tvDistrict);
        TextView tvLatitude = dialog.findViewById(R.id.tvLatitude);
        TextView tvLongitude = dialog.findViewById(R.id.tvLongitude);
        TextView tvDescription = dialog.findViewById(R.id.tvDescription);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);

        // Set data
        tvId.setText(String.valueOf(address.getId()));
        tvTitle.setText(address.getTitle());
        tvProvince.setText(address.getProvince());
        tvDistrict.setText(address.getDistrict());
        tvLatitude.setText(String.valueOf(address.getLatitude()));
        tvLongitude.setText(String.valueOf(address.getLongitude()));
        tvDescription.setText(address.getDescription());

        // Close button
        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void removeAddressAt(int position) {
        addressList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, addressList.size());
    }


}

