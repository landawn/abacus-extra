package com.landawn.abacus.xyz.remoteExecution;

import com.landawn.abacus.remote.RemoteTask;
import com.landawn.abacus.util.N;

public class PublicRemoteTask<T, R> implements RemoteTask<T, R> {
    @Override
    public R run(T t) {
        N.println("========================$$$$$$$$$$$$$$$$$$$$$%%%%%%%%%%%%%%%%%%%%%%" + t);
        return null;
    }
}
