package com.example.aabhaa.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aabhaa.R;
import com.example.aabhaa.models.Address;
import com.example.aabhaa.views.AddAddressActivity;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private List<Address> addressList;
    private Context context;

    public AddressAdapter(Context context, List<Address> addressList) {
        this.context = context;
        this.addressList = addressList;

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
}

