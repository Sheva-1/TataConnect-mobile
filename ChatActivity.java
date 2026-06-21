package com.example.tataconnect;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private LinearLayout layoutMessages;
    private EditText etMessage;
    private ScrollView chatScrollView;
    private TextView tvRecipientName;
    private TextView tvRecipientInitial;
    
    private String currentUserId;
    private String currentUserName = "User"; 
    private String otherUserId;
    private String chatId;
    
    private FirebaseFirestore db;
    private ListenerRegistration chatListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getUid();
        
        otherUserId = getIntent().getStringExtra("OTHER_USER_ID");
        String otherUserName = getIntent().getStringExtra("OTHER_USER_NAME");
        
        if (currentUserId == null || otherUserId == null) {
            Toast.makeText(this, "Error: Chat could not be initialized", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Generate consistent chatId (sorted UIDs)
        if (currentUserId.compareTo(otherUserId) < 0) {
            chatId = currentUserId + "_" + otherUserId;
        } else {
            chatId = otherUserId + "_" + currentUserId;
        }

        setupUI(otherUserName);
        fetchCurrentUserName();
        listenForMessages();
    }

    private void setupUI(String otherUserName) {
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        layoutMessages = findViewById(R.id.layoutMessages);
        etMessage = findViewById(R.id.etMessage);
        chatScrollView = findViewById(R.id.chatScrollView);
        tvRecipientName = findViewById(R.id.tvRecipientName);
        tvRecipientInitial = findViewById(R.id.tvRecipientInitial);
        
        layoutMessages.removeAllViews(); 

        if (otherUserName != null && !otherUserName.isEmpty()) {
            tvRecipientName.setText(otherUserName);
            tvRecipientInitial.setText(otherUserName.substring(0, 1).toUpperCase());
        } else {
            fetchOtherUserName();
        }

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        MaterialButton btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(v -> sendMessage());
    }

    private void fetchCurrentUserName() {
        db.collection("users").document(currentUserId).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                currentUserName = doc.getString("fullName");
                if (currentUserName == null) currentUserName = "User";
            }
        });
    }

    private void fetchOtherUserName() {
        db.collection("users").document(otherUserId).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                String name = doc.getString("fullName");
                if (name != null && !name.isEmpty()) {
                    tvRecipientName.setText(name);
                    tvRecipientInitial.setText(name.substring(0, 1).toUpperCase());
                }
            }
        });
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();
        if (TextUtils.isEmpty(messageText)) return;

        Message message = new Message(currentUserId, otherUserId, messageText);
        
        // Use a temporary local timestamp if needed, but Firestore handles it well if we don't filter it out
        db.collection("chats").document(chatId).collection("messages")
                .add(message)
                .addOnFailureListener(e -> Log.e("Chat", "Send failed", e));
        
        // Update conversation summary for Inbox
        String otherName = tvRecipientName.getText().toString();
        List<String> participants;
        List<String> names;
        
        if (currentUserId.compareTo(otherUserId) < 0) {
            participants = Arrays.asList(currentUserId, otherUserId);
            names = Arrays.asList(currentUserName, otherName);
        } else {
            participants = Arrays.asList(otherUserId, currentUserId);
            names = Arrays.asList(otherName, currentUserName);
        }

        Map<String, Object> conversation = new HashMap<>();
        conversation.put("lastMessage", messageText);
        conversation.put("timestamp", FieldValue.serverTimestamp());
        conversation.put("participants", participants);
        conversation.put("participantNames", names);

        db.collection("conversations").document(chatId)
                .set(conversation, SetOptions.merge())
                .addOnFailureListener(e -> Log.e("Chat", "Conv update failed", e));

        etMessage.setText("");
    }

    private void listenForMessages() {
        chatListener = db.collection("chats").document(chatId).collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("ChatActivity", "Listener error", error);
                        return;
                    }
                    
                    if (value != null) {
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Message msg = dc.getDocument().toObject(Message.class);
                                if (msg != null) {
                                    addMessageToLayout(msg.getMessage(), msg.getSenderId().equals(currentUserId), msg.getTimestamp());
                                }
                            }
                        }
                        scrollToBottom();
                    }
                });
    }

    private void addMessageToLayout(String text, boolean isSent, Date date) {
        if (text == null) return;

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = isSent ? Gravity.END : Gravity.START;
        params.setMargins(0, 0, 0, 24);
        container.setLayoutParams(params);

        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setPadding(32, 20, 32, 20);
        tv.setMaxWidth(700);
        tv.setTextColor(ContextCompat.getColor(this, isSent ? R.color.white : R.color.primary_dark));
        tv.setBackgroundResource(isSent ? R.drawable.bg_chat_bubble_sent : R.drawable.bg_chat_bubble_received);
        container.addView(tv);

        TextView time = new TextView(this);
        String timeStr = (date != null) ? new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date) : "Sending...";
        time.setText(timeStr);
        time.setTextSize(10);
        time.setAlpha(0.6f);
        time.setPadding(isSent ? 0 : 8, 4, isSent ? 8 : 0, 0);
        
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        timeParams.gravity = isSent ? Gravity.END : Gravity.START;
        time.setLayoutParams(timeParams);
        
        container.addView(time);

        layoutMessages.addView(container);
    }

    private void scrollToBottom() {
        chatScrollView.post(() -> chatScrollView.fullScroll(View.FOCUS_DOWN));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (chatListener != null) {
            chatListener.remove();
        }
    }
}
