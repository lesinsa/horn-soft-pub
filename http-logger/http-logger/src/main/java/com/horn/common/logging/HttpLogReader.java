package com.horn.common.logging;

import com.horn.common.logging.domain.LogHttpData;
import com.horn.common.logging.domain.LogHttpRequest;

import java.util.List;

/**
 * @author by lesinsa on 19.10.2015.
 */
public interface HttpLogReader {

    List<LogHttpRequest> getAllRequests();

    List<LogHttpData> getAllHttpDatas();
}
