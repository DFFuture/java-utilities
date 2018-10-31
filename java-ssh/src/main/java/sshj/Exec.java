package sshj;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Exec {
    public static void main(String[] args) throws IOException {
        final SSHClient sshClient = new SSHClient();
        // 加载 ~/.ssh/known_hosts
        //sshClient.loadKnownHosts();
        sshClient.connect("192.168.0.111", 22);
        Session session = null;
        try {
            sshClient.authPassword("root", "123456");
            session = sshClient.startSession();
            final Session.Command cmd = session.exec("ping -c 1 baidu.com");
            System.out.println(cmd.getInputStream().toString());
            cmd.join(5, TimeUnit.SECONDS);
            System.out.println(cmd.getExitStatus());
        } finally {

            if (session != null) {
                session.close();
            }
            sshClient.disconnect();
        }


    }
}
