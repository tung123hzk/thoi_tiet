package com.example.thoitiet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class VideoPlayerActivity extends AppCompatActivity {
    private VideoView videoView;
    private Button buttonPlay, buttonPause, buttonNext, buttonBackToHome;
    private TextView videoTitle, videoDescription;

    // Danh sách các video từ thư mục raw
    private int[] videoResources = {R.raw.hoanghon, R.raw.locxoay, R.raw.dong};

    private String[] videoNames = {"Hoàng Hôn", "Lốc Xoáy", "Cơn Mưa Dông"};
    private String[] videoDescriptions = {
            // Giải thích cho hiện tượng Hoàng Hôn
            "**Hoàng Hôn**: Là hiện tượng xảy ra khi mặt trời dần khuất sau đường chân trời vào buổi chiều, tạo ra màu sắc rực rỡ trên bầu trời. Ánh sáng mặt trời bị khúc xạ và tán xạ qua các tầng khí quyển, khiến bầu trời chuyển từ màu vàng, cam đến đỏ rực. Đây là thời điểm mà tia nắng cuối cùng chiếu rọi trước khi trời tối.",

            // Giải thích cho hiện tượng Lốc Xoáy
            "**Lốc Xoáy**: Là hiện tượng khí quyển khi luồng không khí quay rất nhanh theo hình xoắn ốc, hình thành khi có sự chênh lệch lớn về nhiệt độ và áp suất giữa các vùng khí quyển. Lốc xoáy thường xảy ra ở những khu vực có khí hậu ẩm và nóng, gây ra gió mạnh và có thể kèm theo sấm sét, mưa đá. Sức tàn phá của lốc xoáy có thể gây thiệt hại nặng nề cho các khu vực nó đi qua.",

            // Giải thích cho hiện tượng Cơn Mưa Dông
            "**Cơn Mưa Dông**: Là hiện tượng thời tiết khi mây dông hình thành, mang theo mưa lớn và sấm sét. Mưa dông thường xuất hiện vào mùa hè, khi không khí nóng ẩm gặp khối không khí lạnh, tạo ra sự đối lưu mạnh. Điều này gây ra sấm sét và mưa lớn. Cơn mưa dông có thể đi kèm với gió giật mạnh và đôi khi có cả mưa đá."
    };

    private int currentVideoIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        // Khởi tạo các view
        videoView = findViewById(R.id.videoView);
        buttonPlay = findViewById(R.id.buttonPlay);
        buttonPause = findViewById(R.id.buttonPause);
        buttonNext = findViewById(R.id.buttonNext);
        videoTitle = findViewById(R.id.videoTitle);
        buttonBackToHome = findViewById(R.id.buttonBackToHome);
        videoDescription = findViewById(R.id.videoDescription);

        // Thiết lập video ban đầu
        updateVideoTitle();
        playVideoFromRaw(videoResources[currentVideoIndex]);

        // Xử lý sự kiện nút Phát
        buttonPlay.setOnClickListener(v -> {
            if (!videoView.isPlaying()) {
                videoView.start();
            }
        });

        // Xử lý sự kiện nút Tạm dừng
        buttonPause.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.pause();
            }
        });

        // Xử lý sự kiện nút Tiếp theo
        buttonNext.setOnClickListener(v -> nextVideo());
        // Xử lý sự kiện nút Quay lại trang chủ
        buttonBackToHome.setOnClickListener(v -> goBackToHome());
    }

    // Phương thức phát video từ thư mục raw
    private void playVideoFromRaw(int videoResId) {
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResId);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.start();
    }

    // Phương thức chuyển sang video tiếp theo
    private void nextVideo() {
        currentVideoIndex++;
        if (currentVideoIndex >= videoResources.length) {
            currentVideoIndex = 0; // Quay lại video đầu tiên nếu đến cuối danh sách
            Toast.makeText(this, "Đã hết danh sách, quay lại video đầu tiên", Toast.LENGTH_SHORT).show();
        }
        updateVideoTitle();
        playVideoFromRaw(videoResources[currentVideoIndex]);
    }

    // Cập nhật tiêu đề video
    private void updateVideoTitle() {
        videoTitle.setText(videoNames[currentVideoIndex]);
        videoDescription.setText(videoDescriptions[currentVideoIndex]);
    }

    // Phương thức quay lại trang chủ
    private void goBackToHome() {
        Intent intent = new Intent(VideoPlayerActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}