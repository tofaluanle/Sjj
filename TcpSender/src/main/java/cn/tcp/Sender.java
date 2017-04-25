package cn.tcp;

import java.awt.Button;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Sender extends Frame implements ActionListener {
    TextArea text;
    Panel p;
    TextArea tf;
    TextField tf_ip;
    TextField tf_port;
    Button send, exit;
    Button connect;
    Panel p2;
    Panel p3;

    Socket socket = null;
    DataOutputStream out = null;
    DataInputStream in = null;

    public Sender(String title) {
        super(title);
        p2 = new Panel();
        p3 = new Panel();
        text = new TextArea(18, 60);
        text.setEditable(false);
        p2.add(text);
        TextArea sampleText = new TextArea(15, 20);
        fillSample(sampleText);
        p2.add(sampleText);
        p = new Panel();
        tf = new TextArea(5, 40);
        tf_ip = new TextField(15);
        tf_port = new TextField(6);
        connect = new Button("连接");
        send = new Button("发送");
        exit = new Button("退出");
        tf_ip.setText("172.168.220.114");
        tf_port.setText("4757");
        p.add(new Label("输入 信息"));
        p.add(tf);
        p.add(send);
        p.add(exit);
        p3.add(new Label("收到信息", Label.CENTER));
        p3.add(tf_ip);
        p3.add(tf_port);
        p3.add(connect);
        add(p3, "North");
        add(p2);
        add(p, "South");
        setSize(840, 480);
        send.addActionListener(this);
        exit.addActionListener(this);
        connect.addActionListener(this);
        setLocationRelativeTo(null);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
    }

    private void fillSample(TextArea sampleText) {
        sampleText.setFont(new Font("黑体", Font.PLAIN, 15));
        String[] s = new String[]{"SHINE_USER", "EXEC_CMD|", "MOVE_FILE||", "172.168.66.79", "4666", "CREATE_MEETING||", "JOIN_MEETING||", "CHANGE_COLOR", "CHANGE_COLOR"};
        StringBuilder sb = new StringBuilder();
        for (String s1 : s) {
            sb.append(s1);
            sb.append("\n");
        }
        sampleText.setText(sb.toString());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exit)
            System.exit(0);
        else if (e.getSource() == send) {
            try {
                out.writeUTF(tf.getText());
                text.append("发送：" + tf.getText() + "\n");
            } catch (IOException ex) {
                ex.printStackTrace();
                text.append("数据流通道没有建立\n");
            }
        } else {
            try {
                socket = new Socket(tf_ip.getText(), Integer.parseInt(tf_port.getText()));
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                talk();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public void talk() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (in != null) {
                        try {
                            text.append("接收：" + in.readUTF() + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                            text.append("数据流通道没有建立\n");
                            break;
                        }
                    }
                }
            }
        }.start();

    }

    public static void main(String[] args) {
        new Sender("CS001店办公室");
    }
}