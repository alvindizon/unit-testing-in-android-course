package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class FetchContactsUseCaseTest {

    // region constants
    private static final String FILTER_TERM = "filter_term";
    public static final String ID = "id";
    public static final String FULL_NAME = "Full N. Ame";
    public static final String PHONE_NUMBER = "+639161234567";
    public static final String IMAGE_URL = "https://img.io/ab214";
    public static final double AGE = 69.0;
    // endregion constants

    // region helper fields
    @Mock
    GetContactsHttpEndpoint getContactsHttpEndpointMock;
    @Mock FetchContactsUseCase.Listener listenerMock1;
    @Mock FetchContactsUseCase.Listener listenerMock2;

    @Captor ArgumentCaptor<List<Contact>> acListContact;
    // endregion helper fields

    FetchContactsUseCase SUT;

    @Before
    public void setup() throws Exception {
        SUT = new FetchContactsUseCase(getContactsHttpEndpointMock);
        success();
    }

    // correct filterTerm passed to endpoint
    @Test
    public void fetchContacts_success_filterTermPassedToEndpoint() throws Exception {
        // Arrange
        ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.fetchContactsAndNotify(FILTER_TERM);
        // Assert
        verify(getContactsHttpEndpointMock).getContacts(acString.capture(), any(GetContactsHttpEndpoint.Callback.class));
        assertThat(acString.getValue(), is(FILTER_TERM));
    }

    // all observers notified with correct data
    @Test
    public void fetchContacts_success_observersNotifiedWithCorrectData() throws Exception {
        // Arrange
        // Act
        SUT.registerListener(listenerMock1);
        SUT.registerListener(listenerMock2);
        SUT.fetchContactsAndNotify(FILTER_TERM);
        // Assert
        verify(listenerMock1).onContactsFetched(acListContact.capture());
        verify(listenerMock1).onContactsFetched(acListContact.capture());
        List<List<Contact>> captures = acListContact.getAllValues();
        List<Contact> capture1 = captures.get(0);
        List<Contact> capture2 = captures.get(1);
        assertThat(capture1, is(getContacts()));
        assertThat(capture2, is(getContacts()));
    }

    // unsubscribed observers are not notified
    @Test
    public void fetchContacts_success_unsubscribedObserversNotNotified() throws Exception {
        // Arrange
        // Act
        SUT.registerListener(listenerMock1);
        SUT.registerListener(listenerMock2);
        SUT.unregisterListener(listenerMock2);
        SUT.fetchContactsAndNotify(FILTER_TERM);
        // Assert
        verify(listenerMock1).onContactsFetched(any(List.class));
        verifyNoMoreInteractions(listenerMock2);
    }

    // all observers notified with general error
    @Test
    public void fetchContacts_generalError_observersNotifiedOfFailure() throws Exception {
        // Arrange
        generalError();
        // Act
        SUT.registerListener(listenerMock1);
        SUT.registerListener(listenerMock2);
        SUT.fetchContactsAndNotify(FILTER_TERM);
        // Assert
        verify(listenerMock1).onFetchContactsFailed();
        verify(listenerMock2).onFetchContactsFailed();
    }

    // all observers notified with network error
    @Test
    public void fetchContacts_networkError_observersNotifiedOfFailure() throws Exception {
        // Arrange
        networkError();
        // Act
        SUT.registerListener(listenerMock1);
        SUT.registerListener(listenerMock2);
        SUT.fetchContactsAndNotify(FILTER_TERM);
        // Assert
        verify(listenerMock1).onNetworkError();
        verify(listenerMock2).onNetworkError();
    }

    // region helper methods
    private void success() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) args[1];
                callback.onGetContactsSucceeded(getContactsResponse());
                return null;
            }
        }).when(getContactsHttpEndpointMock).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
    }

    private List<ContactSchema> getContactsResponse() {
        List<ContactSchema> response = new ArrayList<>();
        response.add(new ContactSchema(ID, FULL_NAME, PHONE_NUMBER, IMAGE_URL, AGE));
        return response;
    }

    private List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(ID, FULL_NAME, IMAGE_URL));
        return contacts;
    }

    private void generalError() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) args[1];
                callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.GENERAL_ERROR);
                return null;
            }
        }).when(getContactsHttpEndpointMock).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
    }


    private void networkError() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) args[1];
                callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.NETWORK_ERROR);
                return null;
            }
        }).when(getContactsHttpEndpointMock).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
    }
    // endregion helper methods

    // region helper classes

    // endregion helper classes

}