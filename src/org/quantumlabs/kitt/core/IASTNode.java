package org.quantumlabs.kitt.core;


public interface IASTNode {
    int getOffset();

    int getLength();

    boolean hashChildren();

    IASTNode[] getChildren();
}
