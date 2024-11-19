package com.example.thoitiet;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private List<WeatherData> weatherDataList;

    public WeatherAdapter(List<WeatherData> weatherDataList) {
        this.weatherDataList = weatherDataList;
    }


    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        WeatherData data = weatherDataList.get(position);
        holder.txtTime.setText(data.getTime());
        holder.txtTemperature.setText("Nhiệt độ: " + data.getTemperature() + "°C");
        holder.txtHumidity.setText("Độ ẩm: " + data.getHumidity() + "%");
        holder.txtWindSpeed.setText("Tốc độ gió: " + data.getWindSpeed() + " m/s");
        holder.txtWeatherCondition.setText("Trạng thái: " + data.getWeatherCondition());
    }

    @Override
    public int getItemCount() {
        return weatherDataList.size();
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView txtTime, txtTemperature, txtHumidity, txtWindSpeed, txtWeatherCondition;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtTemperature = itemView.findViewById(R.id.txtTemperature);
            txtHumidity = itemView.findViewById(R.id.txtHumidity);
            txtWindSpeed = itemView.findViewById(R.id.txtWindSpeed);
            txtWeatherCondition = itemView.findViewById(R.id.txtWeatherCondition);
        }
    }
}

