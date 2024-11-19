package com.example.thoitiet;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FeedbackActivity extends AppCompatActivity {
    private EditText userNameInput, userEmailInput, feedbackInput;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctivity_feedback); // Đảm bảo tên layout là chính xác

        // Liên kết các view từ layout
        userNameInput = findViewById(R.id.user_name);
        userEmailInput = findViewById(R.id.user_email);
        feedbackInput = findViewById(R.id.feedback_input);
        submitButton = findViewById(R.id.btn_submit_feedback);

        // Tạo hiệu ứng di chuyển dọc cho nút Gửi
        ObjectAnimator animatorYButton = ObjectAnimator.ofFloat(submitButton, "translationY", 0f, 200f);
        animatorYButton.setDuration(1000); // Thời gian di chuyển
        animatorYButton.setRepeatCount(1);
        animatorYButton.setRepeatMode(ObjectAnimator.REVERSE);
        animatorYButton.start();

        // Xử lý sự kiện khi nhấn nút Gửi
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameInput.getText().toString().trim();
                String userEmail = userEmailInput.getText().toString().trim();
                String feedback = feedbackInput.getText().toString().trim();

                if (feedback.isEmpty()) {
                    Toast.makeText(FeedbackActivity.this, "Vui lòng nhập phản hồi trước khi gửi.", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý phản hồi (có thể gửi lên server hoặc lưu trữ cục bộ)
                    Toast.makeText(FeedbackActivity.this, "Cảm ơn bạn, " + userName + ", đã gửi phản hồi!", Toast.LENGTH_SHORT).show();

                    // Xóa nội dung sau khi gửi
                    userNameInput.setText("");
                    userEmailInput.setText("");
                    feedbackInput.setText("");
                    finish(); // Đóng activity sau khi gửi
                }
            }
        });
    }
}
