package com.kermi.base.service;

import util.SessionAttributes;


public interface SessionService {

    /**
     * 将Session中存的值写到缓存中
     *
     * @param attributes SessionAttributes自定义类
     */
    void writeSessionAttributesToRedis(SessionAttributes attributes);

    /**
     * 从Redis中获取Session值
     *
     * @param sessionID
     * @return
     */
    SessionAttributes getSessionAttributesFromRedis(String sessionID);
}

