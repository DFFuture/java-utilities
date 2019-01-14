package jsch;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;

public class JschExec {
    public static void main(String[] args) throws JSchException, IOException, InterruptedException {
        JSch client = new JSch();
        Session session = client.getSession("root","127.0.0.1", 2222);
        session.setPassword("root");
        session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("PerferredAuthentications", "publickey,keybord-interactive, password");
        session.connect(3000);

        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setCommand("tail -f install.log");
        channelExec.setErrStream(System.err);
        channelExec.setInputStream(null);
        channelExec.setOutputStream(System.out);

        InputStream in = channelExec.getInputStream();
        channelExec.connect();

        StringBuilder executeResultString = new StringBuilder();
        byte[] tmp = new byte[1024];
        while (true) {
            while(in.available() > 0) {
                int i = in.read(tmp, 0 , 1024);
                if(i < 0) break;
                executeResultString.append(new String(tmp, 0, i));
                System.out.println(executeResultString);
            }
            if(channelExec.isClosed()) {
                System.out.println("exit code:" + channelExec.getExitStatus());
                break;
            }
            Thread.sleep(1000);
        }
        channelExec.disconnect();
        session.disconnect();
        System.out.println("返回值是：" + executeResultString);
    }
}
