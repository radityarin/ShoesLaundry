package com.shoes.shoeslaundry.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.shoes.shoeslaundry.utils.notifications.APIService;
import com.shoes.shoeslaundry.utils.notifications.Client;
import com.shoes.shoeslaundry.utils.notifications.Data;
import com.shoes.shoeslaundry.utils.notifications.Response;
import com.shoes.shoeslaundry.utils.notifications.Sender;
import com.shoes.shoeslaundry.utils.notifications.Token;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class ChatActivity extends AppCompatActivity {

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
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;
    private static final String MESSAGE_URL = "http://friendlychat.firebase.google.com/message/";

    private Button mSendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder>
            mFirebaseAdapter;
    private int counter = 0;

    private boolean isWorkTime = false;

    APIService apiService;
    boolean notify = false;

    private String adminUID;
    private String myUID;

    private static final int ID_SENDER = 1;
    private static final int ID_RECEIVER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        isWorkTime = checkWorkTime();
        adminUID = "obW9ciDSKlOkt3aWbmzptxM5w2v2";

        Button backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Set default username is anonymous.
        mUsername = ANONYMOUS;
        mUsername = getIntent().getStringExtra("name");

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        myUID = mFirebaseUser.getUid();


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

        DatabaseReference messagesRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(myUID);
        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(messagesRef, parser)
                        .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {

            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                RecyclerView.ViewHolder viewHolder;
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
                        Message(mFirebaseAuth.getUid(), mMessageEditText.getText().toString(),
                        mUsername, false);
                mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(myUID)
                        .push().setValue(friendlyMessage);
                mMessageEditText.setText("");

                sendNotification(adminUID, friendlyMessage.getName(), friendlyMessage.getText(), "new_message");

                if (isWorkTime) {
                    Log.d("cek", "onClick: ");
                    final Message message = new Message();
                    message.setIduser(adminUID);
                    message.setName("Admin Bot");
                    message.setRead(false);
                    if (counter == 0) {
                        message.setText("Terima kasih telah menghubungi Garage Shoes Clean Malang. Silakan beri tahu apa yang dapat kami bantu.");
                        counter++;
                    } else {
                        message.setText("Under maintenance");
                    }
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(myUID)
                                    .push().setValue(message);
                        }
                    }, 1500L);
                }
            }
        });

        seenMessage();


        //create api service
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

    }

    private void seenMessage() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Message").child(myUID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Message message = dt.getValue(Message.class);
                    if (message.getIduser().equals(adminUID) && !message.isRead()) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("read", true);
                        dt.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(final String adminUID, final String name, final String text, String title) {
        if (title.equals("new_message")) {
            title = "New Message";
        } else if (title.equals("new_status")) {
            title = "New Status";
        }
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(adminUID);
        String finalTitle = title;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Token token = dt.getValue(Token.class);
                    Data data = new Data(mFirebaseAuth.getUid(), name + " : " + text, finalTitle, adminUID, R.drawable.wash,"chat");
                    Sender sender = new Sender(data, token.getToken());
                    Gson gson = new Gson();
                    String json = gson.toJson(sender);
                    System.out.println(json);
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
//                                    Toast.makeText(ChatActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
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


    public boolean checkWorkTime() {
        Calendar now = Calendar.getInstance();

        int hour = now.get(Calendar.HOUR_OF_DAY); // Get hour in 24 hour format
        int minute = now.get(Calendar.MINUTE);

        Date date = parseDate(hour + ":" + minute);
        Date dateCompareOne = parseDate("09:00");
        Date dateCompareTwo = parseDate("18:00");

        if (dateCompareOne.before(date) && dateCompareTwo.after(date)) {
            return false;
        } else {
            return true;
        }
    }

    private Date parseDate(String date) {

        final String inputFormat = "HH:mm";
        SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
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
