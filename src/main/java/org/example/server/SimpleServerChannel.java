package org.example.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SimpleServerChannel {

    public static void main(String[]args){
     try(ServerSocketChannel serverChannel=ServerSocketChannel.open()){
         serverChannel.socket().bind(new InetSocketAddress(5000));
         serverChannel.configureBlocking(false);

         System.out.println("Server is listening on port "+serverChannel.socket().getLocalPort());

         List<SocketChannel> clientChannels=new ArrayList<SocketChannel>();

         while (true) {
             SocketChannel clientChannel = serverChannel.accept();

             if(clientChannel!=null) {
                 clientChannel.configureBlocking(false);
                 System.out.printf("Client %s connected%n", clientChannel.socket().getRemoteSocketAddress());
                 clientChannels.add(clientChannel);
             }

             ByteBuffer buffer = ByteBuffer.allocate(1024);

             for (int i = 0; i < clientChannels.size(); i++) {


                 SocketChannel channel = clientChannels.get(i);
                 int readBytes = channel.read(buffer);

                 if (readBytes > 0) {
                     buffer.flip();
                     channel.write(ByteBuffer.wrap("Echo from server: ".getBytes()));
                     while (buffer.hasRemaining()) {
                         channel.write(buffer);
                     }
                     buffer.clear();
                 } else if (readBytes == -1) {
                     System.out.printf("Connection to %s lost%n", channel.socket().getRemoteSocketAddress());
                     channel.close();
                     clientChannels.remove(i);
                 }
             }
         }
     }
    catch (IOException ex){
        System.out.println("Server sxception "+ex.getMessage());
    }
    }
}
