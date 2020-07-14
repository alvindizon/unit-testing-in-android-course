package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.example11.cart.CartItem;
import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;

public class FetchContactsUseCase {
    private final List<Listener> listeners = new ArrayList<>();
    private final GetContactsHttpEndpoint getContactsHttpEndpoint;

    public FetchContactsUseCase(GetContactsHttpEndpoint getContactsHttpEndpoint) {

        this.getContactsHttpEndpoint = getContactsHttpEndpoint;
    }

    public void fetchContactsAndNotify(String filterTerm) {
        getContactsHttpEndpoint.getContacts(filterTerm, new GetContactsHttpEndpoint.Callback() {
            @Override
            public void onGetContactsSucceeded(List<ContactSchema> contactResponse) {
                for (Listener listener : listeners) {
                    listener.onContactsFetched(mapResponseToContacts(contactResponse));
                }
            }

            @Override
            public void onGetContactsFailed(GetContactsHttpEndpoint.FailReason failReason) {
                switch (failReason) {
                    case GENERAL_ERROR:
                        for(Listener listener : listeners) {
                            listener.onFetchContactsFailed();
                        }
                        break;
                    case NETWORK_ERROR:
                        for(Listener listener : listeners) {
                            listener.onNetworkError();
                        }
                        break;
                    default:
                        throw new RuntimeException("Invalid failReason: " + failReason.toString());

                }
            }
        });
    }

    private List<Contact> mapResponseToContacts(List<ContactSchema> contactResponse) {
        List<Contact> contacts = new ArrayList<>();
        for(ContactSchema response : contactResponse) {
            contacts.add(new Contact(
                    response.getId(),
                    response.getFullName(),
                    response.getImageUrl())
            );
        }
        return contacts;
    }

    public void registerListener(Listener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(Listener listener) {
        listeners.remove(listener);
    }

    public interface Listener {
        void onContactsFetched(List<Contact> capture);

        void onFetchContactsFailed();

        void onNetworkError();
    }
}
