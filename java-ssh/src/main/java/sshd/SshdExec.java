package sshd;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelExec;
import org.apache.sshd.client.channel.ChannelShell;
import org.apache.sshd.client.channel.ChannelSubsystem;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.ChannelPipedInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static java.lang.Thread.sleep;

public class SshdExec {

    public static void main(String[] args) throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        String cmd = "tail -f install.log";
        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        try(ClientSession session = client
                .connect("root", "127.0.0.1", 2222)
                .verify(3000).getSession()) {
            session.addPasswordIdentity("root");
            session.auth().verify(3000);
            //ChannelExec channel = session.createExecChannel(cmd);
            ChannelShell channel = session.createShellChannel();
            channel.setIn(System.in);
            channel.setOut(System.out);
            channel.open();
            channel.waitFor(Collections.singleton(ClientChannelEvent.CLOSED), 0);
//            OutputStream out = channel.getInvertedIn();
//            String str = "cd /var;ls\n";
//            out.write(str.getBytes());
//            out.flush();
//            ChannelPipedInputStream in = (ChannelPipedInputStream)channel.getInvertedOut();
//            byte[] buf = new byte[1024];
//            int length;
//            String result;
//            ByteArrayOutputStream output;
//            while((length = in.read(buf)) != -1) {
//                //清空output
//                output = new ByteArrayOutputStream();
//                output.write(buf, 0, length);
//                result = output.toString(StandardCharsets.UTF_8.name());
//                System.out.println(result);
//                //清空buf
//                buf = new byte[1024];
//                if(!in.isOpen()) break;
//            }
//            sleep(10000);
//            channel.close();
//            channel.waitFor(Collections.singleton(ClientChannelEvent.CLOSED), 0);
            System.out.println("开始");
            channel.close();
        }
        System.out.println(System.currentTimeMillis() - start);
        client.close();

    }
}
