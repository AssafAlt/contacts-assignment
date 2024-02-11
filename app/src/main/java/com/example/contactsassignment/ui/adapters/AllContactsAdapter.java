package com.example.contactsassignment.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsassignment.R;
import com.example.contactsassignment.data.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsAdapter.AllContactViewHolder> {

    private List<Contact> contacts;
    private List<Contact> filteredContacts; // New list to hold filtered contacts
    private OnContactClickListener listener;

    public AllContactsAdapter(List<Contact> contacts, OnContactClickListener listener) {
        this.contacts = contacts;
        this.filteredContacts = new ArrayList<>(contacts); // Initialize filteredContacts with all contacts
        this.listener = listener;
    }

    @NonNull
    @Override
    public AllContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.contact_list_item, parent, false);
        return new AllContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllContactViewHolder holder, int position) {
        Contact contact = filteredContacts.get(position); // Use filtered contacts list
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return filteredContacts.size(); // Use filtered contacts list size
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        filterContacts(""); // Reset filter when contacts change
    }

    // Filter contacts based on prefix
    public void filterContacts(String prefix) {
        filteredContacts.clear();
        for (Contact contact : contacts) {
            if (contact.getName().toLowerCase().startsWith(prefix.toLowerCase())) {
                filteredContacts.add(contact);
            }

        }
        notifyDataSetChanged();
    }

    public class AllContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView nameTextView;
        private final TextView phoneTextView;

        public AllContactViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            itemView.setOnClickListener(this);
        }

        public void bind(Contact contact) {
            nameTextView.setText(contact.getName());
            if (contact.getPhone() != null && !contact.getPhone().isEmpty()) {
                phoneTextView.setVisibility(View.VISIBLE);
                phoneTextView.setText(contact.getPhone());
            } else {
                phoneTextView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onContactClick(filteredContacts.get(position)); // Use filtered contacts list
                }
            }
        }
    }


    public interface OnContactClickListener {
        void onContactClick(Contact contact);
    }
}
