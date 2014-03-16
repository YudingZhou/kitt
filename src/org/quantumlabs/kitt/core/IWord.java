package org.quantumlabs.kitt.core;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.rules.IToken;

/**
 * Each token which are parsed by partition scanner could be a word. Try to
 * create IWord by position and token
 * */

public interface IWord {

    int DISCARD = -1;

    int getType();

    Position getPosition();

    IToken getToken();

    int getOffset();
}
