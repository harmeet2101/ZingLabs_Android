package com.zing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zing.R;
import com.zing.fragment.VerifyOtpFragment;
import com.zing.util.AppTypeface;

public class OtpAdapter extends BaseAdapter {
    private Context context;
    private int[] KeyId;
    LayoutInflater inflater;
    private VerifyOtpFragment verifyOtpFragment;

    public OtpAdapter(Context context, int[] KeyId, VerifyOtpFragment verifyOtpFragment) {
        this.context = context;
        this.KeyId = KeyId;
        this.verifyOtpFragment = verifyOtpFragment;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AppTypeface.getTypeFace(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return KeyId.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView tvKey;
        ImageView ivKey;
        View view;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();

        View rowView;
        rowView = inflater.inflate(R.layout.otp_row, null);
        holder.tvKey = rowView.findViewById(R.id.tvKey);
        holder.ivKey = rowView.findViewById(R.id.ivKey);
        holder.view = rowView.findViewById(R.id.view);

        holder.tvKey.setTypeface(AppTypeface.avenieNext_regular);

        if ((position + 1) % 3 == 0) {
            holder.view.setVisibility(View.GONE);
        }

        if (position == 9 || position == 11) {
            holder.ivKey.setVisibility(View.VISIBLE);
            if (position == 9) {
                if (verifyOtpFragment.getMin()) {

                    holder.ivKey.setImageResource(R.drawable.delete_selected);
                } else {
                    holder.ivKey.setImageResource(R.drawable.delete);
                }
            } else {
                if (verifyOtpFragment.getMax()) {
                    holder.ivKey.setImageResource(R.drawable.next_selected);
                } else {
                    holder.ivKey.setImageResource(R.drawable.next);
                }
            }

        } else {
            holder.ivKey.setVisibility(View.GONE);
            holder.tvKey.setText("" + KeyId[position]);
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (verifyOtpFragment).setValue(position);
                notifyDataSetChanged();
            }
        });

        return rowView;
    }
}
