package com.g5.tdp2.myhealthapp.service;

import com.g5.tdp2.myhealthapp.entity.Check;
import com.g5.tdp2.myhealthapp.usecase.GetChecks;
import com.g5.tdp2.myhealthapp.util.DateFormatter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class GetChecksStub implements GetChecks {
    @Override
    public void getChecks(int affiliateId, Consumer<List<Check>> succCallback, Consumer<Exception> errCallback) {
        List<Check> checks = Arrays.asList(
                new Check(1,
                        "https://firebasestorage.googleapis.com/v0/b/lustrous-bay-252022.appspot.com/o/myhealthapp%2Fchecks%2Fpending%2F962f87c9-3ea7-4770-bd89-b0e0817c32fe.jpg?alt=media&token=a451095b-212a-4be3-80ef-04ac9c178b1f",
                        "myhealthapp/checks/pending/962f87c9-3ea7-4770-bd89-b0e0817c32fe.jpg",
                        "PENDING",
                        1,
                        affiliateId,
                        DateFormatter.YYYY_MM_DD.deserialize("2019-10-01"),
                        DateFormatter.YYYY_MM_DD.deserialize("2019-10-17")),
                new Check(2,
                        "https://firebasestorage.googleapis.com/v0/b/lustrous-bay-252022.appspot.com/o/myhealthapp%2Fchecks%2Fpending%2F962f87c9-3ea7-4770-bd89-b0e0817c32fe.jpg?alt=media&token=a451095b-212a-4be3-80ef-04ac9c178b1f",
                        "myhealthapp/checks/pending/962f87c9-3ea7-4770-bd89-b0e0817c32fe.jpg",
                        "ACCEPTED",
                        2,
                        affiliateId,
                        DateFormatter.YYYY_MM_DD.deserialize("2019-08-11"),
                        DateFormatter.YYYY_MM_DD.deserialize("2019-08-22"))
        );
        succCallback.accept(checks);
    }
}
