package ru.prbb.common.biz;

import java.util.List;

/**
 * @author lesinsa on 10.07.2015.
 */
public interface ParamProvider {
    List<String> getParams(P2PParams input);
}
