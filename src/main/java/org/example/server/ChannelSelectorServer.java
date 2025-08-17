package org.example.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChannelSelectorServer {

    private static final Map<SocketChannel, StringBuilder> clientBuffers = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.bind(new InetSocketAddress(5000));
            serverChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isAcceptable()) {
                        SocketChannel clientChannel = serverChannel.accept();
                        System.out.println("Client connected: " + clientChannel.getRemoteAddress());
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);

                        clientBuffers.put(clientChannel, new StringBuilder());
                    } else if (key.isReadable()) {
                        Path sentfile = Path.of("sen0.txt");
                        if (!Files.exists(sentfile)) {
                            Files.createFile(sentfile);
                        }
                        echoData(key, sentfile);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void echoData(SelectionKey key, Path sent) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        int bytesRead = clientChannel.read(buffer);

        if (bytesRead > 0) {
            buffer.flip();
            String chunk = StandardCharsets.UTF_8.decode(buffer).toString();

            System.out.println(chunk);
            // Get or create buffer for this client
            StringBuilder clientBuffer = clientBuffers.get(clientChannel);
            clientBuffer.append(chunk);

            // Process complete lines
            String bufferedData = clientBuffer.toString();
            String[] parts = bufferedData.split("\n", -1);

            // Write all complete lines (all but the last part)
            for (int i = 0; i < parts.length - 1; i++) {
                String line = parts[i] + "\n";
                Files.writeString(sent, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                System.out.println("Received line: " + parts[i]); // Real-time feedback
            }

            // Keep the incomplete line (last part) in buffer
            clientBuffer.setLength(0);
            clientBuffer.append(parts[parts.length - 1]);

        } else if (bytesRead == -1) {
            // Client disconnected - write any remaining data
            StringBuilder clientBuffer = clientBuffers.get(clientChannel);
            if (clientBuffer != null && clientBuffer.length() > 0) {
                Files.writeString(sent, clientBuffer.toString(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }

            System.out.println("Client disconnected: " + clientChannel.getRemoteAddress());
            clientBuffers.remove(clientChannel); // Clean up
            key.cancel();
            clientChannel.close();
        }
    }
}