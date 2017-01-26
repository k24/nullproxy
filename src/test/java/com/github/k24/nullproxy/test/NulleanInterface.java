package com.github.k24.nullproxy.test;

import com.github.k24.nullproxy.NullProxy;

/**
 * Created by k24 on 2017/01/12.
 */
public interface NulleanInterface {
    NulleanInterface NULL = NullProxy.newProxyInstance(NulleanInterface.class);

    int intValue();
}
