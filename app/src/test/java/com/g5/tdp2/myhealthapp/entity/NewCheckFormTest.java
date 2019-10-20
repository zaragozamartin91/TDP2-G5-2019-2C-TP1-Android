package com.g5.tdp2.myhealthapp.entity;

import com.g5.tdp2.myhealthapp.util.JsonParser;

import org.junit.Test;

import static org.junit.Assert.*;

public class NewCheckFormTest {
    @Test
    public void toJson() {
        String url = "https://firebasestorage.googleapis.com/v0/b/lustrous-bay-252022.appspot.com/o/myhealthapp%2Fchecks%2Fpending%2F962f87c9-3ea7-4770-bd89-b0e0817c32fe.jpg?alt=media&token=a451095b-212a-4be3-80ef-04ac9c178b1f";
        NewCheckForm newCheckForm = new NewCheckForm(
                url,
                "myhealthapp/checks/pending/962f87c9-3ea7-4770-bd89-b0e0817c32fe.jpg",
                14,
                123
        );

        String json = JsonParser.INSTANCE.writeValueAsString(newCheckForm);
        System.out.println(json);

        NewCheckForm parsed = JsonParser.INSTANCE.readValue(json, NewCheckForm.class);
        assertEquals(newCheckForm, parsed);
    }
}