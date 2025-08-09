package com.example.aabhaa.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.example.aabhaa.controllers.SoilController;
import com.example.aabhaa.models.Soil;
import com.example.aabhaa.models.SoilResponse;
import com.example.aabhaa.views.SoilActivity;

import java.util.List;

public class SoilAdapter extends RecyclerView.Adapter<SoilAdapter.SoilViewHolder> {

    private List<Soil> soilList;
    private SoilController soilController;
    private Context context;

    public SoilAdapter(Context context , List<Soil>  soilList) {
        this.soilList = soilList;
        this.context = context;
        if (context instanceof Activity) {
            soilController = new SoilController( context);
            soilController.setAdapter(this);  // set adapter here once!
        }

    }

    public void updateData(List<Soil>  newData) {
        soilList = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SoilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_soil_data, parent, false);
        return new SoilViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoilViewHolder holder, int position) {
        Soil soil = soilList.get(position);
        // Set Address Title
        holder.tvSoilTitle.setText(soil.getAddressTitle());
        holder.tvSoilId.setText("Lat: " + soil.getLatitude() + ",   Lon: " + soil.getLongitude());
        holder.tvN.setText(String.valueOf(Math.round(soil.getN_kg())));
        holder.tvP.setText(String.valueOf(Math.round(soil.getP_kg())));
        holder.tvK.setText(String.valueOf(Math.round(soil.getK_kg())));
        holder.tvPH.setText(String.valueOf(soil.getPh()));

        holder.btnEdit.setOnClickListener(v->{
            Intent intent = new Intent(context , SoilActivity.class);
            intent.putExtra("address_id",soil.getAddress_id());
            intent.putExtra("id",soil.getId());
            intent.putExtra("N",soil.getN_kg());
            intent.putExtra("P",soil.getP_kg());
            intent.putExtra("K",soil.getK_kg());
            intent.putExtra("pH",soil.getPh());
            context.startActivity(intent);
        });

        holder.btnView.setOnClickListener(v -> {
            showSoilDetailsDialog(context, soilList.get(position));
        });

        holder.btnDelete.setOnClickListener(v ->{
            if(soilController != null){
                soilController.deleteSoilData(position,soil);
            } else {
                Toast.makeText(context , "Unable to delete the Soil Data",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return soilList != null ? soilList.size() : 0;
    }

    public static class SoilViewHolder extends RecyclerView.ViewHolder {
        TextView tvSoilTitle, tvSoilId, tvN, tvP, tvK, tvPH;
        ImageButton btnEdit, btnView, btnDelete;

        public SoilViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSoilTitle = itemView.findViewById(R.id.tvSoilTitle);
            tvSoilId = itemView.findViewById(R.id.tvSoilId);
            tvN = itemView.findViewById(R.id.tvN);
            tvP = itemView.findViewById(R.id.tvP);
            tvK = itemView.findViewById(R.id.tvK);
            tvPH = itemView.findViewById(R.id.tvPH);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnView = itemView.findViewById(R.id.btnView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public  void showSoilDetailsDialog(Context context , Soil soil){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_soil_view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        // Find dialog views
        TextView tvId = dialog.findViewById(R.id.tvId);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        TextView tvLatitude = dialog.findViewById(R.id.tvLatitude);
        TextView tvLongitude = dialog.findViewById(R.id.tvLongitude);
        TextView tvN = dialog.findViewById(R.id.tvN);
        TextView tvP = dialog.findViewById(R.id.tvP);
        TextView tvK = dialog.findViewById(R.id.tvK);
        TextView tvPH = dialog.findViewById(R.id.tvPH);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);

        // Set soil data to dialog views
        tvId.setText(String.valueOf(soil.getId()));
        tvTitle.setText(soil.getAddressTitle()); // or soil.getAddress_id() if no title
        tvLatitude.setText(String.valueOf(soil.getLatitude()));
        tvLongitude.setText(String.valueOf(soil.getLongitude()));
        tvN.setText(String.valueOf(soil.getN_kg()));
        tvP.setText(String.valueOf(soil.getP_kg()));
        tvK.setText(String.valueOf(soil.getK_kg()));
        tvPH.setText(String.valueOf(soil.getPh()));

        // Close button listener
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    public void removeSoilDataAt(int position){
        soilList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,soilList.size());
    }

}
