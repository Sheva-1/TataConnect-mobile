package com.example.tataconnect;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InboxActivity extends AppCompatActivity {

    private ConversationAdapter adapter;
    private List<Map<String, Object>> conversationList;
    private List<Map<String, Object>> filteredList;
    private FirebaseFirestore db;
    private String currentUserId;
    private ProgressBar progressBar;
    private TextView tvEmptyInbox;
    private ListenerRegistration inboxListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inbox);

        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getUid();
        conversationList = new ArrayList<>();
        filteredList = new ArrayList<>();

        if (currentUserId == null) {
            Toast.makeText(this, "Session expired", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupUI();
        loadConversations();
    }

    private void setupUI() {
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        progressBar = findViewById(R.id.progressBar);
        tvEmptyInbox = findViewById(R.id.tvEmptyInbox);
        
        EditText etSearchInbox = findViewById(R.id.etSearchInbox);
        if (etSearchInbox != null) {
            etSearchInbox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filter(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        RecyclerView rvConversations = findViewById(R.id.rvConversations);
        if (rvConversations != null) {
            rvConversations.setLayoutManager(new LinearLayoutManager(this));
            adapter = new ConversationAdapter(filteredList, currentUserId, conversation -> {
                @SuppressWarnings("unchecked")
                List<String> participants = (List<String>) conversation.get("participants");
                @SuppressWarnings("unchecked")
                List<String> names = (List<String>) conversation.get("participantNames");

                String otherUserId = "";
                String otherUserName = "";

                if (participants != null && names != null && participants.size() >= 2 && names.size() >= 2) {
                    int otherIndex = participants.get(0).equals(currentUserId) ? 1 : 0;
                    otherUserId = participants.get(otherIndex);
                    otherUserName = names.get(otherIndex);
                }

                Intent intent = new Intent(InboxActivity.this, ChatActivity.class);
                intent.putExtra("OTHER_USER_ID", otherUserId);
                intent.putExtra("OTHER_USER_NAME", otherUserName);
                startActivity(intent);
            });
            rvConversations.setAdapter(adapter);
        }
    }

    private void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(conversationList);
        } else {
            text = text.toLowerCase();
            for (Map<String, Object> item : conversationList) {
                @SuppressWarnings("unchecked")
                List<String> names = (List<String>) item.get("participantNames");
                @SuppressWarnings("unchecked")
                List<String> ids = (List<String>) item.get("participants");
                
                String otherName = "";
                if (names != null && ids != null && ids.size() >= 2 && names.size() >= 2) {
                    int otherIndex = ids.get(0).equals(currentUserId) ? 1 : 0;
                    otherName = names.get(otherIndex);
                }

                if (otherName.toLowerCase().contains(text)) {
                    filteredList.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
        if (tvEmptyInbox != null) {
            tvEmptyInbox.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    private void loadConversations() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        
        inboxListener = db.collection("conversations")
                .whereArrayContains("participants", currentUserId)
                .addSnapshotListener((value, error) -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    if (error != null) {
                        Log.e("Inbox", "Error: " + error.getMessage());
                        return;
                    }

                    if (value != null) {
                        conversationList.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            conversationList.add(doc.getData());
                        }
                        
                        Collections.sort(conversationList, (a, b) -> {
                            try {
                                com.google.firebase.Timestamp tsA = (com.google.firebase.Timestamp) a.get("timestamp");
                                com.google.firebase.Timestamp tsB = (com.google.firebase.Timestamp) b.get("timestamp");
                                if (tsA == null || tsB == null) return 0;
                                return tsB.compareTo(tsA);
                            } catch (Exception e) { return 0; }
                        });

                        // Re-apply filter or update filtered list
                        filter(""); // This will update filteredList with all items if search is empty
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (inboxListener != null) {
            inboxListener.remove();
        }
    }

    private interface OnConversationClickListener {
        void onConversationClick(Map<String, Object> conversation);
    }

    private static class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {
        private final List<Map<String, Object>> conversations;
        private final OnConversationClickListener listener;
        private final String currentUserId;

        ConversationAdapter(List<Map<String, Object>> conversations, String currentUserId, OnConversationClickListener listener) {
            this.conversations = conversations;
            this.currentUserId = currentUserId;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Map<String, Object> conv = conversations.get(position);
            @SuppressWarnings("unchecked")
            List<String> names = (List<String>) conv.get("participantNames");
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) conv.get("participants");

            String otherName = "User";
            if (names != null && ids != null && ids.size() >= 2 && names.size() >= 2) {
                int otherIndex = ids.get(0).equals(currentUserId) ? 1 : 0;
                otherName = names.get(otherIndex);
            }

            holder.tvName.setText(otherName);
            holder.tvLastMessage.setText((String) conv.get("lastMessage"));
            
            if (otherName != null && !otherName.isEmpty()) {
                holder.tvInitial.setText(otherName.substring(0, 1).toUpperCase());
            } else {
                holder.tvInitial.setText("U");
            }

            Object ts = conv.get("timestamp");
            if (ts instanceof com.google.firebase.Timestamp) {
                Date date = ((com.google.firebase.Timestamp) ts).toDate();
                holder.tvTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date));
            } else {
                holder.tvTime.setText("Now");
            }

            holder.itemView.setOnClickListener(v -> listener.onConversationClick(conv));
        }

        @Override
        public int getItemCount() {
            return conversations.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView tvName, tvLastMessage, tvTime, tvInitial;
            ViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
                tvTime = itemView.findViewById(R.id.tvTime);
                tvInitial = itemView.findViewById(R.id.tvInitial);
            }
        }
    }
}
