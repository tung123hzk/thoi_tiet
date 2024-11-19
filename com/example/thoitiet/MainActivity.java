package com.example.thoitiet;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String CHANNEL_ID = "weather_notifications";
    private FusedLocationProviderClient fusedLocationClient;
    private EditText edtTimKiem;
    private TextView txtTenTP, txtTenQG, txtNhietDo, txtTrangThai, txtDoAm ,txtThoiGianCapNhat ;
    private Button btnTim, btnNgayTiepTheo;
    private TextView txtTrangThaiMay, txtTocDoGio;
    private Button btnCanhBao;
    //  private RecyclerView recyclerView;
    private WeatherAdapter weatherAdapter;
    private List<WeatherData> weatherDataList;
    private TextView txtChatLuongKhongKhi, txtUVIndex;
    private TextToSpeech textToSpeech;
    private Button BtnChiaSe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtTimKiem = findViewById(R.id.edtTimKiem);
        txtTenTP = findViewById(R.id.txtTenTP);
        txtTenQG = findViewById(R.id.txtTenQG);
        txtNhietDo = findViewById(R.id.txtNhietDo);
        txtTrangThai = findViewById(R.id.txtTrangThai);
        txtDoAm = findViewById(R.id.txtDoAm);
        txtTrangThaiMay = findViewById(R.id.txtTrangThaiMay);
        txtTocDoGio = findViewById(R.id.txtTocDoGio);
        txtThoiGianCapNhat = findViewById(R.id.txtThoiGianCapNhat);
        txtChatLuongKhongKhi= findViewById(R.id.txtChatLuongKhongKhi);
        txtUVIndex = findViewById(R.id.txtUVIndex);
        btnTim = findViewById(R.id.btnTim);
        btnNgayTiepTheo = findViewById(R.id.btnNgayTiepTheo);
        btnCanhBao = findViewById(R.id.btnCanhBao);
        //recyclerView = findViewById(R.id.recyclerView);
        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        weatherDataList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(weatherDataList);
        //  recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setAdapter(weatherAdapter);
        btnCanhBao.setOnClickListener(v -> {
            hienThiCanhBaoThoiTiet();
        });

        // Khởi tạo TextToSpeech
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(new Locale("vi", "VN"));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "Ngôn ngữ không hỗ trợ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Tạo kênh thông báo
        createNotificationChannel();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Kiểm tra quyền truy cập vị trí
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocation();
        }

        btnTim.setOnClickListener(v -> {
            String city = edtTimKiem.getText().toString();
            if (!city.isEmpty()) {
                getWeatherData(city);
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng nhập thành phố", Toast.LENGTH_SHORT).show();
            }
        });

        btnNgayTiepTheo.setOnClickListener(v -> {
            String city = edtTimKiem.getText().toString();
            if (!city.isEmpty()) {
                getWeatherDataForNext5Days(city);
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng nhập thành phố", Toast.LENGTH_SHORT).show();
            }
        });
        Button btnChiaSe = findViewById(R.id.btnChiaSe);
        btnChiaSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin thời tiết từ các TextView
                String tenTP = ((TextView) findViewById(R.id.txtTenTP)).getText().toString();
                String nhietDo = ((TextView) findViewById(R.id.txtNhietDo)).getText().toString();
                String trangThai = ((TextView) findViewById(R.id.txtTrangThai)).getText().toString();
                String doAm = ((TextView) findViewById(R.id.txtDoAm)).getText().toString();
                String tocDoGio = ((TextView) findViewById(R.id.txtTocDoGio)).getText().toString();
                String chatLuongKhongKhi = ((TextView) findViewById(R.id.txtChatLuongKhongKhi)).getText().toString();
                String uvIndex = ((TextView) findViewById(R.id.txtUVIndex)).getText().toString();

                // Chuẩn bị thông tin để chia sẻ
                String shareText = "Thời tiết hiện tại: " + tenTP + ":\n" +
                        " " + nhietDo + "\n" +
                        " " + trangThai + "\n" +
                        " " + doAm + "\n" +
                        " " + tocDoGio + "\n" +
                        " " + chatLuongKhongKhi + "\n" +
                        " " + uvIndex;

                // Tạo intent chia sẻ
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

                // Hiển thị ứng dụng có thể chia sẻ
                startActivity(Intent.createChooser(shareIntent, "Chia sẻ thông tin thời tiết qua:"));
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_feedback) {
            // Mở FeedbackActivity
            startActivity(new Intent(this, FeedbackActivity.class));
            return true;
        }else if(itemId == R.id.about) {
            startActivity(new Intent(this,AboutActivity.class));
            return true;
        }else if(itemId == R.id.news) {
            startActivity(new Intent(this, NewsActivity.class));
            return true;
        }
        else if(itemId == R.id.video) {
            startActivity(new Intent(this,VideoPlayerActivity.class));
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Quyền đã được cấp, có thể sử dụng vị trí
        }
    }

    private void hienThiCanhBaoThoiTiet() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kiểm tra Cảnh báo Thời tiết")
                .setMessage("Hiện tại không có cảnh báo thời tiết bất thường.")
                .setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Weather Notifications";
            String description = "Channel for weather alerts";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        getWeatherDataByCoordinates(latitude, longitude);
                    }
                });
    }

    private void getWeatherData(String city) {
        String apiKey = "5ce51a565b52f1d769fbd279ae4ea864";
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric&lang=vi";

        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                parseWeatherData(response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    void getWeatherDataByCoordinates(double latitude, double longitude) {
        String apiKey = "5ce51a565b52f1d769fbd279ae4ea864";
        String urlString = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey + "&units=metric&lang=vi";

        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                parseWeatherData(response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void parseWeatherData(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String cityName = jsonObject.getString("name");
            String country = jsonObject.getJSONObject("sys").getString("country");
            double temperature = jsonObject.getJSONObject("main").getDouble("temp");
            String weather = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            int humidity = jsonObject.getJSONObject("main").getInt("humidity");
            long timeStamp = jsonObject.getLong("dt");
            // Lấy trạng thái mây và tốc độ gió
            int cloudiness = jsonObject.getJSONObject("clouds").getInt("all");
            double windSpeed = jsonObject.getJSONObject("wind").getDouble("speed");
            double latitude = jsonObject.getJSONObject("coord").getDouble("lat");
            double longitude = jsonObject.getJSONObject("coord").getDouble("lon");
            String updatedAt = formatDate(timeStamp);
            getAQIData(latitude, longitude); // Thêm hàm lấy chất lượng không khí
            getUVIndex(latitude, longitude);
            // Tạo một đối tượng WeatherData mới
            WeatherData weatherData = new WeatherData(updatedAt, temperature, humidity, windSpeed, weather);
            runOnUiThread(() -> {
                txtTenTP.setText("Tên thành phố: " + cityName);
                txtTenQG.setText("Tên quốc gia: " + country);
                txtNhietDo.setText("Nhiệt độ: " + temperature + "°C");
                txtTrangThai.setText("Trạng thái: " + weather);
                txtDoAm.setText("Độ ẩm: " + humidity + "%");
                txtThoiGianCapNhat.setText("Thời gian cập nhật: " + updatedAt);
                // Hiển thị trạng thái mây và tốc độ gió
                txtTrangThaiMay.setText(" Mây: " + cloudiness + "%");
                txtTocDoGio.setText("Tốc độ gió: " + windSpeed + " m/s");
                weatherDataList.clear(); // Xóa dữ liệu cũ
                weatherDataList.add(weatherData); // Thêm dữ liệu mới
                weatherAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                // Kiểm tra điều kiện thời tiết bất thường và thông báo
                checkAbnormalWeather(weather);
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getAQIData(double latitude, double longitude) {
        String apiKey = "5ce51a565b52f1d769fbd279ae4ea864";
        String urlString = "https://api.openweathermap.org/data/2.5/air_pollution?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey;

        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Phân tích dữ liệu AQI
                JSONObject jsonObject = new JSONObject(response.toString());
                int airQualityIndex = jsonObject.getJSONArray("list").getJSONObject(0).getJSONObject("main").getInt("aqi");

                // Hiển thị dữ liệu AQI lên giao diện
                runOnUiThread(() -> {
                    txtChatLuongKhongKhi.setText(" không khí (AQI): " + airQualityIndex);
                });

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void checkAbnormalWeather(String weather) {
        String[] abnormalConditions = {"mưa", "bão", "bão tuyết", "dông", "gió mạnh"};

        for (String condition : abnormalConditions) {
            if (weather.toLowerCase().contains(condition)) {
                sendNotification("Cảnh báo thời tiết!", "Thời tiết bất thường - " + condition);
                Toast.makeText(this, "Cảnh báo: Thời tiết bất thường - " + condition, Toast.LENGTH_LONG).show();
                break;
            }
        }
    }
    private void getUVIndex(double latitude, double longitude) {
        String apiKey = "5ce51a565b52f1d769fbd279ae4ea864";
        String urlString = "https://api.openweathermap.org/data/2.5/uvi?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey;

        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                JSONObject jsonObject = new JSONObject(response.toString());
                double uvIndex = jsonObject.getDouble("value");

                runOnUiThread(() -> {
                    txtUVIndex.setText("Chỉ số UV: " + uvIndex);
                });
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.tb) // Icon thông báo
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    private void getWeatherDataForNext5Days(String city) {
        String apiKey = "5ce51a565b52f1d769fbd279ae4ea864";
        String urlString = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + apiKey + "&units=metric&lang=vi";

        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                parseWeatherForecastData(response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void parseWeatherForecastData(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray list = jsonObject.getJSONArray("list");
            StringBuilder weatherForecast = new StringBuilder();
            String currentDay = "";

            for (int i = 0; i < list.length(); i++) {
                JSONObject intervalForecast = list.getJSONObject(i);
                long timeStamp = intervalForecast.getLong("dt");
                double temperature = intervalForecast.getJSONObject("main").getDouble("temp");
                String weather = intervalForecast.getJSONArray("weather").getJSONObject(0).getString("description");

                String date = formatDate(timeStamp); // Only date part
                String time = formatTime(timeStamp); // Only time part

                if (!currentDay.equals(date)) {
                    weatherForecast.append("\nNgày: ").append(date).append("\n"); // Start a new day
                    currentDay = date;
                }

                weatherForecast.append("Giờ: ").append(time)
                        .append(" - Nhiệt độ: ").append(temperature).append("°C")
                        .append(" - Trạng thái: ").append(weather)
                        .append("\n");
            }

            runOnUiThread(() -> {
                // Hiển thị thông tin thời tiết 3 giờ và 5 ngày tiếp theo
                showWeatherForecastDialog(weatherForecast.toString());
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showWeatherForecastDialog(String forecast) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Dự báo thời tiết 5 ngày tiếp theo")
                .setMessage(forecast)
                .setPositiveButton("Đóng", (dialog, which) -> {
                    dialog.dismiss();
                    // Đọc thông tin dự báo thời tiết
                    readWeatherForecast(forecast);
                })
                .create()
                .show();
    }

    private void readWeatherForecast(String forecast) {
        if (textToSpeech != null) {
            textToSpeech.speak(forecast, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }



    private String formatDate(long timeStamp) {
        Date date = new Date(timeStamp * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }
    private String formatTime(long timeStamp) {
        Date date = new Date(timeStamp * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(date);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Cần quyền truy cập vị trí để sử dụng chức năng này", Toast.LENGTH_SHORT).show();
            }
        }
    }
}