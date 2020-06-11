package com.shoes.shoeslaundry.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.model.Message;
import com.shoes.shoeslaundry.ui.user.ChatActivity;
import com.shoes.shoeslaundry.ui.user.MainActivity;
import com.shoes.shoeslaundry.utils.notifications.APIService;
import com.shoes.shoeslaundry.utils.notifications.Client;
import com.shoes.shoeslaundry.utils.notifications.Data;
import com.shoes.shoeslaundry.utils.notifications.Response;
import com.shoes.shoeslaundry.utils.notifications.Sender;
import com.shoes.shoeslaundry.utils.notifications.Token;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class ChatAdminActivity extends AppCompatActivity {

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView messengerTextView;

        public MessageViewHolder(View v) {
            super(v);
            messengerTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messageTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
        }
    }

    private static final String TAG = "MainActivity";
    public static final String MESSAGES_CHILD = "Message";
    private static final int REQUEST_INVITE = 1;
    private static final int REQUEST_IMAGE = 2;
    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 10;
    public static final String ANONYMOUS = "anonymous";
    private static final String MESSAGE_SENT_EVENT = "message_sent";
    private String mUsername;
    private String uidpelanggan;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;
    private static final String MESSAGE_URL = "http://friendlychat.firebase.google.com/message/";

    private Button mSendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    private ImageView mAddMessageImageView;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder>
            mFirebaseAdapter;
    APIService apiService;

    private static final int ID_SENDER = 1;
    private static final int ID_RECEIVER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_admin);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Set default username is anonymous.
        mUsername = ANONYMOUS;
        mUsername = "ADMIN";
        uidpelanggan = getIntent().getStringExtra("uidpelanggan");
//    uidpelanggan = "5ot3imPaULRzKfNRglsESISgDY53";
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        // Initialize ProgressBar and RecyclerView.
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SnapshotParser<Message> parser = new SnapshotParser<Message>() {
            @Override
            public Message parseSnapshot(DataSnapshot dataSnapshot) {
                Message friendlyMessage = dataSnapshot.getValue(Message.class);
                if (friendlyMessage != null) {
                    friendlyMessage.setId(dataSnapshot.getKey());
                }
                return friendlyMessage;
            }
        };

        DatabaseReference messagesRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(uidpelanggan);
        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(messagesRef, parser)
                        .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {
            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View v1;
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                switch (i){
                    case ID_SENDER:
                        v1 = inflater.inflate(R.layout.item_message_sender,viewGroup,false);
                        break;
                    case ID_RECEIVER:
                        v1 = inflater.inflate(R.layout.item_message,viewGroup,false);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + i);
                }
                return new MessageViewHolder(v1);
            }

            @Override
            protected void onBindViewHolder(final MessageViewHolder viewHolder,
                                            int position,
                                            Message friendlyMessage) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                if (friendlyMessage.getText() != null) {
                    viewHolder.messageTextView.setText(friendlyMessage.getText());
                    viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                }
                viewHolder.messengerTextView.setText(friendlyMessage.getName());
            }

            private boolean isSender(int position) {
                String uidsender = mFirebaseAuth.getUid();
                return uidsender.equals(getItem(position).getIduser());
            }

            @Override
            public int getItemViewType(int position) {
                if (isSender(position)) {
                    return ID_SENDER;
                } else {
                    return ID_RECEIVER;
                }
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setAdapter(mFirebaseAdapter);

        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mSendButton = (Button) findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message friendlyMessage = new
                        Message(mFirebaseAuth.getUid(),mMessageEditText.getText().toString(),
                        mUsername,false);
                mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(uidpelanggan)
                        .push().setValue(friendlyMessage);
                mMessageEditText.setText("");

                sendNotification(uidpelanggan, friendlyMessage.getName(), friendlyMessage.getText(), "new_message");

            }
        });

        seenMessage();
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

    }

    private void sendNotification(final String hisUID, final String name, final String text, String title) {
        if (title.equals("new_message")) {
            title = "New Message";
        } else if (title.equals("new_status")) {
            title = "New Status";
        }
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(hisUID);
        String finalTitle = title;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Token token = dt.getValue(Token.class);
                    Data data = new Data(mFirebaseAuth.getUid(), name + " : " + text, finalTitle, hisUID, R.drawable.wash);
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
//                                    Toast.makeText(ChatAdminActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void seenMessage() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Message").child(uidpelanggan);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dt: dataSnapshot.getChildren()){
                    Message message = dt.getValue(Message.class);
                    if(message.getIduser().equals(uidpelanggan) && !message.isRead()){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("read",true);
                        dt.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in.
        // TODO: Add code to check if user is signed in.
    }

    @Override
    public void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
