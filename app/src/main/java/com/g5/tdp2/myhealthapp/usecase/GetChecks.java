package com.g5.tdp2.myhealthapp.usecase;

import com.g5.tdp2.myhealthapp.entity.Check;

import java.util.List;
import java.util.function.Consumer;

public interface GetChecks {
    void getChecks(int affiliateId, Consumer<List<Check>> succCallback, Consumer<Exception> errCallback);
}
