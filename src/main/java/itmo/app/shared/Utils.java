package itmo.app.shared;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Utils {

    public static Object readObjectFromChannel(SocketChannel channel)
        throws IOException, ClassNotFoundException {
        var objectSizeBuffer = ByteBuffer.allocate(Integer.BYTES);
        channel.read(objectSizeBuffer);
        objectSizeBuffer.flip();
        int objectSize = objectSizeBuffer.getInt();
        var objectBuffer = ByteBuffer.allocate(Integer.BYTES + objectSize);
        int bytesRead = 0;
        while (bytesRead < objectSize) {
            bytesRead += channel.read(objectBuffer);
        }
        try (
            var byteArrayStream = new ByteArrayInputStream(
                objectBuffer.slice(Integer.BYTES, objectSize).array()
            );
            var objectInputStream = new ObjectInputStream(byteArrayStream);
        ) {
            return objectInputStream.readObject();
        }
    }

    public static ByteBuffer objectToBuffer(Serializable object) throws IOException {
        try (
            var byteOut = new ByteArrayOutputStream();
            var objectStream = new ObjectOutputStream(byteOut);
        ) {
            objectStream.writeObject(object);
            byte[] objectBytes = byteOut.toByteArray();

            int objectSize = objectBytes.length;
            var buffer = ByteBuffer.allocate(Integer.BYTES + objectSize);
            buffer.putInt(objectSize);
            buffer.put(objectBytes);
            return buffer;
        }
    }

    public static void writeObjectToCahnnel(SocketChannel channel, Serializable object)
        throws IOException {
        try (var selector = Selector.open();) {
            ByteBuffer buffer = Utils.objectToBuffer(object);
            buffer.flip();
            channel.register(selector, SelectionKey.OP_WRITE);
            while (true) {
                selector.select();
                var keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey selectedKey = keys.next();
                    ((SocketChannel) selectedKey.channel()).write(buffer);
                    return;
                }
            }
        }
    }

    public static Object readObjectFromInputStream(InputStream input)
        throws IOException, ClassNotFoundException {
        byte[] sizebuff = new byte[Integer.BYTES];
        input.read(sizebuff);
        int size = ByteBuffer.wrap(sizebuff).getInt();
        byte[] objectbuff = new byte[size];
        input.read(objectbuff);
        try (
            var byteStream = new ByteArrayInputStream(objectbuff);
            var objectStream = new ObjectInputStream(byteStream);
        ) {
            return objectStream.readObject();
        }
    }

    public static void writeObjectToOutputStream(
        Serializable object,
        OutputStream output
    ) throws IOException {
        ByteBuffer buffer = Utils.objectToBuffer(object);
        output.write(buffer.array());
    }
}
