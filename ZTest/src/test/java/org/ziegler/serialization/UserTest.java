package org.ziegler.serialization;

import org.junit.jupiter.api.Test;

import java.io.*;

class UserTest {

    @Test
    public void test() throws IOException, ClassNotFoundException {

        final User jack = new User(1, "jack");

        final ByteArrayOutputStream bytesOS = new ByteArrayOutputStream();
        final ObjectOutputStream outputStream = new ObjectOutputStream(bytesOS);
        outputStream.writeObject(jack);
        final byte[] bytes = bytesOS.toByteArray();

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        final User user = (User) objectInputStream.readObject();
        System.out.println("user = " + user);


    }
}