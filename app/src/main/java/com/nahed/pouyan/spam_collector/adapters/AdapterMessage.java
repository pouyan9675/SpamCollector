package com.nahed.pouyan.spam_collector.adapters;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nahed.pouyan.spam_collector.CustomTextView;
import com.nahed.pouyan.spam_collector.R;
import com.nahed.pouyan.spam_collector.helpers.PersianHelper;
import com.nahed.pouyan.spam_collector.models.Message;

import java.util.List;

/**
 * Created by pouyan on 3/19/18.
 */

public class AdapterMessage extends RecyclerView.Adapter<AdapterMessage.FilterVH> {

    private List<Message> messages;

    public AdapterMessage(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public FilterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilterVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_message, parent, false));
    }

    @Override
    public void onBindViewHolder(final FilterVH holder, final int position) {
        holder.chkSelected.setChecked(messages.get(position).selected);
        holder.txtBody.setText(messages.get(position).getBody());
        holder.txtId.setText(PersianHelper.convertToPersianNumbers(messages.get(position).getId()));
        holder.lytBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = holder.chkSelected.isChecked();
                holder.chkSelected.setChecked(!checked);
                messages.get(position).selected = !checked;
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class FilterVH extends RecyclerView.ViewHolder {

        CustomTextView txtBody;
        CustomTextView txtId;
        AppCompatCheckBox chkSelected;
        ViewGroup lytBackground;

        FilterVH(View itemView) {
            super(itemView);
            this.chkSelected = itemView.findViewById(R.id.adapterChkSelected);
            this.txtBody = itemView.findViewById(R.id.adapterTxtBody);
            this.txtId = itemView.findViewById(R.id.adapterTxtId);
            this.lytBackground = itemView.findViewById(R.id.adapterLytBackground);
        }
    }
}