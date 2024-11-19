package com.example.thoitiet;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class AboutActivity extends AppCompatActivity {

    private static final int REQUEST_CALL_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView appDescriptionTextView = findViewById(R.id.appDescriptionTextView);
        TextView authorTextView = findViewById(R.id.authorTextView);
        TextView thankYouTextView = findViewById(R.id.thankYouTextView);
        Button closeButton = findViewById(R.id.closeButton);
        ImageView thankYouImageView = findViewById(R.id.thankYouImageView);
        Button callButton = findViewById(R.id.callButton);

        titleTextView.setText("Giới thiệu về ứng dụng thời tiết");
        appDescriptionTextView.setText("Ứng dụng thời tiết cung cấp thông tin thời tiết chính xác và kịp thời, giúp bạn lên kế hoạch cho các hoạt động hàng ngày một cách dễ dàng.");
        authorTextView.setText("Tác giả: Lê sơn tùng, Huỳnh đức hà việt");
        thankYouTextView.setText("Cảm ơn bạn đã sử dụng ứng dụng của chúng tôi!");

        closeButton.setOnClickListener(v -> finish());

        // Thêm xử lý cho nút Gọi điện thoại
        callButton.setOnClickListener(v -> makePhoneCall());
    }

    private void makePhoneCall() {
        String phoneNumber = "0965589805";
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(dialIntent);
    }


    private void startCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            }
        }
    }
}
