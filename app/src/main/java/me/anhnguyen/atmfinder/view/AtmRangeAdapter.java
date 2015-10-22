package me.anhnguyen.atmfinder.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.anhnguyen.atmfinder.R;
import me.anhnguyen.atmfinder.model.ui.AtmRange;

/**
 * Created by nguyenhoanganh on 10/21/15.
 */
public class AtmRangeAdapter extends ArrayAdapter<AtmRange> {
    private List<AtmRange> ranges = new ArrayList<>();

    public AtmRangeAdapter(Context context) {
        super(context, 0);
        ranges.add(new AtmRange("2 km", 2000));
        ranges.add(new AtmRange("5 km", 5000));
        ranges.add(new AtmRange("10 km", 10000));
        ranges.add(new AtmRange("15 km", 15000));
        ranges.add(new AtmRange("20 km", 20000));
    }

    @Override
    public AtmRange getItem(int position) {
        return ranges.get(position);
    }

    @Override
    public int getCount() {
        return ranges.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_range, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.bind(ranges.get(position));

        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.range_name)
        TextView textViewRangeName;
        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }

        public void bind(AtmRange range) {
            textViewRangeName.setText(range.getName());
        }
    }
}
