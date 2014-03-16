package org.quantumlabs.kitt.core.serivce;

import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.resources.IResource;
import org.quantumlabs.kitt.ui.view.model.TreeNode;

public class TransactionCache
{
    private enum SYSTEM_CACHE_ITEM
    {
        NAVIGATOR_TREE_ROOT
    }

    private static TransactionCache instance;
    private ConcurrentHashMap<SYSTEM_CACHE_ITEM, Object> systemCache;

    public static TransactionCache instance()
    {
        if( instance == null )
        {
            synchronized( TransactionCache.class )
            {
                if( instance == null )
                {
                    instance = new TransactionCache();
                }
            }
        }
        return instance;
    }

    public TransactionCache()
    {
    }

    public boolean cache( TreeNode<?> tree )
    {
        if( !systemCache.containsKey( SYSTEM_CACHE_ITEM.NAVIGATOR_TREE_ROOT ) )
        {
            systemCache.putIfAbsent( SYSTEM_CACHE_ITEM.NAVIGATOR_TREE_ROOT, tree );
            return true;
        }
        return false;
    }

    public TreeNode<?> getTree()
    {
        return TreeNode.class.cast( systemCache.get( SYSTEM_CACHE_ITEM.NAVIGATOR_TREE_ROOT ) );
    }

    public void initialize()
    {
        systemCache = new ConcurrentHashMap<SYSTEM_CACHE_ITEM, Object>();
    }
}
