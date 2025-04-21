package com.rebels.quasar.service;

import com.rebels.quasar.exception.CommunicationException;
import java.util.List;

/**
 *
 * @author FDip
 */
public interface MessageService {
    String decodeMessage(List<List<String>> messages) throws CommunicationException;
}
