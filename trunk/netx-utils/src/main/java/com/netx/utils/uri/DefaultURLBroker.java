package com.netx.utils.uri;

/**
 * 缺省的实现
 *
 * @author Tiger
 * @since 2011-4-22 下午05:31:10
 * @version 1.0
 * @update runshine 2014-10-22
 */
public class DefaultURLBroker extends URLBroker {

    public DefaultURLBroker(URLBroker urlBroker) {
        super(urlBroker);
    }

    public DefaultURLBroker() {

    }

    @Override
    public URLBroker getInstance() {
        return new DefaultURLBroker(this);
    }
    
/**
 * 说明:按URLBrokerFactory与URLBroker语义来看是想缓存URLBroker实例的，实例内部分动作也已经渲染好被缓存
 * 但这里却使用了生成新实例，导致每次从缓存Map取出一个URLBroker，然后又由它生成了一个新实例，使得URLBrokerFactory失去了缓存的意义
 * 所以更改了语义，使用getInstance。目前此实现使用的getInstance依旧生成新实例,因为原实现是线程不安全的，以此来解决线程不安全
    public URLBroker newInstance() {
        return new DefaultURLBroker(this);
    }
*/
    
}
