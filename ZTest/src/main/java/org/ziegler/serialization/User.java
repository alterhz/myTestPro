package org.ziegler.serialization;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * 序列化的代理模式
 * <p>序列化时，调用 {@link User#writeReplace} 方法，创建并序列化的代理对象。不是序列化的{@link User}对象。</p>
 * <p>反序列化时，实际是用的{@link User.SerializationProxy} 进行反序列化，调用 {@link User.SerializationProxy#readResolve}
 * 方法，这时会创建 {@link User}对象，如果反序列化{@link User.SerializationProxy}有问题，那么就不会创建{@link User}对象了。</p>
 * <p>总结，这种模式序列化和反序列化的不是{@link User}对象，而是它的代理{@link User.SerializationProxy}对象。</p>
 *
 * @author Ziegler
 * date 2021/4/22
 */
public class User implements Serializable {
    private final int id;
    private final String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .toString();
    }

    private static class SerializationProxy implements Serializable {
        private final int id;
        private final String name;
        private static final long serialVersionUID = 2039208493824098L;

        SerializationProxy(User user) {
            this.id = user.getId();
            this.name = user.getName();
        }

        /**
         * 反序列化调用此方法
         * @return 返回User对象
         */
        private Object readResolve() {
            return new User(id, name);
        }
    }


    /**
     * 序列化调用此方法
     * @return 返回代理对象
     */
    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    /**
     * 此方法不应该调用到，所以直接抛出异常
     */
    private void ReadObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
}
