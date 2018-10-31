package sshd;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;

import java.io.IOException;

public class SshdExec {

    public static void main(String[] args) throws IOException {
        String cmd = "hostname";
        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        try(ClientSession session = client
                .connect("root", "192.168.0.111", 22)
                .verify(3000).getSession()) {
            session.addPasswordIdentity("12345");
            session.auth().verify(3000);
        }
        client.close();
    }
}
