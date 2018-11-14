package sshd;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelExec;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;

import java.io.IOException;
import java.util.Collections;

public class SshdExec {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        String cmd = "tail -f /root/apache-flume-1.8.0-bin/logs/flume.log";
        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        try(ClientSession session = client
                .connect("root", "192.168.0.111", 22)
                .verify(3000).getSession()) {
            session.addPasswordIdentity("123456");
            session.auth().verify(3000);
            ChannelExec channel = session.createExecChannel(cmd);
            channel.setOut(System.out);
            channel.open();
            //channel.waitFor(Collections.singleton(ClientChannelEvent.CLOSED), 0);
            channel.close();
        }
        System.out.println(System.currentTimeMillis() - start);
        client.close();

    }
}
