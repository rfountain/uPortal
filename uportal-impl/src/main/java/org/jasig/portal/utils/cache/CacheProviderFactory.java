/**
 * Copyright (c) 2000-2009, Jasig, Inc.
 * See license distributed with this file and available online at
 * https://www.ja-sig.org/svn/jasig-parent/tags/rel-10/license-header.txt
 */
package org.jasig.portal.utils.cache;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.collections.map.ReferenceMap;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portal.utils.threading.MapCachingDoubleCheckedCreator;
import org.springframework.beans.factory.annotation.Required;
import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.provider.CacheProviderFacade;

/**
 * CacheFactory impl that provides Map instances that wrap a configured {@link CacheProviderFacade}. This uses the
 * {@link MapCacheProvider} to perform the wrapping. Refer to that class for which operations on the {@link Map}
 * interface are supported.
 * 
 * @author Eric Dalquist
 * @version $Revision$
 */
public class CacheProviderFactory implements CacheFactory {
    protected final Log logger = LogFactory.getLog(this.getClass());

    // Stores caches that are created with a soft reference to the cache Map to avoid re-creating wrapper objects without need
    private final MapCacheCreator mapCacheCreator = new MapCacheCreator();

    private CacheProviderFacade cacheProviderFacade;
    private ICacheModelFactory cacheModelFactory;
    
    /**
     * @return the cacheProviderFacade
     */
    public CacheProviderFacade getCacheProviderFacade() {
        return cacheProviderFacade;
    }
    /**
     * @param cacheProviderFacade the cacheProviderFacade to set
     */
    @Required
    public void setCacheProviderFacade(CacheProviderFacade cacheProviderFacade) {
        Validate.notNull(cacheProviderFacade);
        this.cacheProviderFacade = cacheProviderFacade;
    }

    /**
     * @return the cacheModelFactory
     */
    public ICacheModelFactory getCacheModelFactory() {
        return cacheModelFactory;
    }
    /**
     * @param cacheModelFactory the cacheModelFactory to set
     */
    @Required
    public void setCacheModelFactory(ICacheModelFactory cacheModelFactory) {
        Validate.notNull(cacheModelFactory);
        this.cacheModelFactory = cacheModelFactory;
    }

    /* (non-Javadoc)
     * @see org.jasig.portal.utils.cache.CacheFactory#getCache()
     */
    public <K extends Serializable, V> Map<K, V> getCache() {
        return this.getCache(DEFAULT);
    }

    /* (non-Javadoc)
     * @see org.jasig.portal.utils.cache.CacheFactory#getCache(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public <K extends Serializable, V> Map<K, V> getCache(String cacheName) throws IllegalArgumentException {
        return (Map<K, V>) this.mapCacheCreator.get(cacheName);
    }

    private class MapCacheCreator extends MapCachingDoubleCheckedCreator<String, Map<?, ?>> {
        public MapCacheCreator() {
            super(new ReferenceMap(ReferenceMap.HARD, ReferenceMap.SOFT));
        }
        
        /* (non-Javadoc)
         * @see org.jasig.portal.utils.threading.MapCachingDoubleCheckedCreator#getKey(java.lang.Object[])
         */
        @Override
        protected String getKey(Object... args) {
            return (String) args[0];
        }

        
        /* (non-Javadoc)
         * @see org.jasig.portal.utils.threading.MapCachingDoubleCheckedCreator#createInternal(java.lang.Object, java.lang.Object[])
         */
        @SuppressWarnings("unchecked")
        @Override
        protected Map<?, ?> createInternal(String cacheName, Object... args) {
            final FlushingModel flushingModel = CacheProviderFactory.this.cacheModelFactory.getFlushingModel(cacheName);
            final CachingModel cachingModel = CacheProviderFactory.this.cacheModelFactory.getCachingModel(cacheName);

            return new MapCacheProvider(CacheProviderFactory.this.cacheProviderFacade, cachingModel, flushingModel);
        }
    }
}