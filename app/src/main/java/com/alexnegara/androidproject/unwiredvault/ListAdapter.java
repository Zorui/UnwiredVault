package com.alexnegara.androidproject.unwiredvault;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    // declare list of object and interface
    private ArrayList<Account> listAccount;
    private OnClickCallback onClickCallback;

    // construct with list
    public ListAdapter(ArrayList<Account> list) {
        this.listAccount = list;
    }

    // set interface from loose coupled interface
    public void setOnClickCallback(OnClickCallback onClickCallback) {
        this.onClickCallback = onClickCallback;
    }

    // create holder with view group based on determined layout
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_account, viewGroup, false);
        return new ListViewHolder(view);
    }

    // binding each holder with value
    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        // give space for custom floating action button
        if (position + 1 == getItemCount()) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            params.bottomMargin = (int) calcPxFromDp(holder.itemView.getContext(), 72);
            holder.itemView.setLayoutParams(params);
        }

        // binding values either with value or listener
        Account account = listAccount.get(position);
        Glide.with(holder.itemView.getContext()).load(account.getPhoto()).apply(new RequestOptions().override(144, 144)).into(holder.imgPhoto);
        holder.tvName.setText(account.getName());
        holder.tvPswd.setText(account.getPswd());
        holder.tvId.setText(account.getId());
        holder.ibCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCallback.onCopyClicked(listAccount.get(holder.getAdapterPosition()).getPswd());
            }
        });
        holder.ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCallback.onEditClicked(listAccount.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCallback.onDeleteClicked(holder.getAdapterPosition());
            }
        });
    }

    // calculate px for margin bottom params
    public static float calcPxFromDp(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    // get the size of list
    @Override
    public int getItemCount() {
        return listAccount.size();
    }

    // nested class as holder for each view with list value
    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        ImageButton ibCopy, ibEdit, ibDelete;
        TextView tvName, tvId, tvPswd;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_account);
            tvName = itemView.findViewById(R.id.account_name);
            tvId = itemView.findViewById(R.id.account_id);
            tvPswd = itemView.findViewById(R.id.account_pswd);
            ibCopy = itemView.findViewById(R.id.copy_button);
            ibEdit = itemView.findViewById(R.id.edit_button);
            ibDelete = itemView.findViewById(R.id.delete_button);
        }
    }

    // declare loose coupled interface
    public interface OnClickCallback {
        void onEditClicked(Account data, int pos);

        void onCopyClicked(String password);

        void onDeleteClicked(int pos);
    }
}
