package com.example.chat.adaptrs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.databinding.ItemContainerReceivedImageBinding;
import com.example.chat.databinding.ItemContainerReceivedMessageBinding;
import com.example.chat.databinding.ItemContainerReceivedMessageChatroomBinding;
import com.example.chat.databinding.ItemContainerSentImageBinding;
import com.example.chat.databinding.ItemContainerSentMessageBinding;
import com.example.chat.models.ChatMessage;
import com.example.chat.models.User;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoomAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<ChatMessage> chatMessages;
    private final String senderId;
    private final Map<String, Bitmap> userImages = new HashMap<>();
    private static final Map<String,String> idtoName=new HashMap<>();

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;
    public static final int VIEW_TYPE_SENT_IMAGE = 3;
    public static final int VIEW_TYPE_RECEIVED_IMAGE = 4;

    public ChatRoomAdapter(List<ChatMessage> chatMessages, List<User> users, String senderId) {
        this.chatMessages = chatMessages;
        this.senderId = senderId;

        for (User user : users) {
            userImages.put(user.id, decodeImage(user.image));
            idtoName.put(user.id,user.name);
        }
    }

    public void adduser(User user){
        userImages.put(user.id,decodeImage(user.image));
        idtoName.put(user.id,user.name);
    }

    public static String getName(String userid){
        return idtoName.get(userid);
    }

    private Bitmap decodeImage(String encodedImage) {
        if (encodedImage != null && !encodedImage.isEmpty()) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder0(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent, false
                    )
            );
        }
        else if(viewType == VIEW_TYPE_RECEIVED){
            return new ReceivedMessageViewHolder0(
                    ItemContainerReceivedMessageChatroomBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent, false
                    )
            );
        }
        else if(viewType == VIEW_TYPE_SENT_IMAGE){
            return new SentMessageViewHolderimg(
                    ItemContainerSentImageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,false
                    )
            );
        }
        else {
            return new ReceivedMessageViewHolderimg(
                    ItemContainerReceivedImageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder0) holder).setData(chatMessage);
        } else if(getItemViewType(position) == VIEW_TYPE_RECEIVED){
            ((ReceivedMessageViewHolder0) holder).setData(chatMessage, userImages.get(chatMessage.senderId));
        } else if(getItemViewType(position) == VIEW_TYPE_SENT_IMAGE){
            if (chatMessage.message != null && !chatMessage.message.isEmpty()) {
                ((SentMessageViewHolderimg) holder).setData(chatMessage);
            }
        } else {
            if (chatMessage.message != null && !chatMessage.message.isEmpty()) {
                ((ReceivedMessageViewHolderimg) holder).setData(chatMessage, userImages.get(chatMessage.senderId));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).isImage){
            if (chatMessages.get(position).senderId.equals(senderId)) {
                return VIEW_TYPE_SENT_IMAGE;
            } else {
                return VIEW_TYPE_RECEIVED_IMAGE;
            }
        }
        else {
            if (chatMessages.get(position).senderId.equals(senderId)) {
                return VIEW_TYPE_SENT;
            } else {
                return VIEW_TYPE_RECEIVED;
            }
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class SentMessageViewHolder0 extends RecyclerView.ViewHolder {

        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder0(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(ChatMessage chatMessage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    static class ReceivedMessageViewHolder0 extends RecyclerView.ViewHolder {

        private final ItemContainerReceivedMessageChatroomBinding binding;

        ReceivedMessageViewHolder0(ItemContainerReceivedMessageChatroomBinding itemContainerReceivedMessageChatroomBinding) {
            super(itemContainerReceivedMessageChatroomBinding.getRoot());
            binding = itemContainerReceivedMessageChatroomBinding;
        }

        void setData(ChatMessage chatMessage, Bitmap senderImage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
            binding.imageProfile.setImageBitmap(senderImage);
            binding.textUserName.setText(getName(chatMessage.senderId));
        }
    }

    static class SentMessageViewHolderimg extends RecyclerView.ViewHolder {

        private final ItemContainerSentImageBinding binding;

        SentMessageViewHolderimg(ItemContainerSentImageBinding itemContainerSentImageBinding) {
            super(itemContainerSentImageBinding.getRoot());
            binding = itemContainerSentImageBinding;
        }

        void setData(ChatMessage chatMessage) {
            byte[] bytes = Base64.decode(chatMessage.message, Base64.DEFAULT);
            binding.imageMessage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    static class ReceivedMessageViewHolderimg extends RecyclerView.ViewHolder {

        private final ItemContainerReceivedImageBinding binding;

        ReceivedMessageViewHolderimg(ItemContainerReceivedImageBinding itemContainerReceivedImageBinding) {
            super(itemContainerReceivedImageBinding.getRoot());
            binding = itemContainerReceivedImageBinding;
        }

        void setData(ChatMessage chatMessage, Bitmap senderImage) {
            byte[] bytes = Base64.decode(chatMessage.message, Base64.DEFAULT);
            binding.imageMessage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            binding.textDateTime.setText(chatMessage.dateTime);
            binding.imageProfile.setImageBitmap(senderImage);
            binding.textUserName.setText(getName(chatMessage.senderId));
        }
    }
}
