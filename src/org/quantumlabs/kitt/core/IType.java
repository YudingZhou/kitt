package org.quantumlabs.kitt.core;

@Deprecated
public interface IType extends ITTCNElement {

    IType INTEGER = new Type("__integer");
    IType CHAR = new Type("__char");
    IType FLOAT = new Type("__float");
    IType BOOLEAN = new Type("__boolean");
    IType OBJID = new Type("__objid");
    IType VERDICTTYPE = new Type("__verdicttype");
    IType RECORD = new Type("__record");

    boolean isPrimitive();

    IType getSuper();
}
