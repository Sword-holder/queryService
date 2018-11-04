package quaryService;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class SocketServer {
	static ServerSocket server;
	
	public static void response(String response, Socket client) throws Exception {
		
	    String ip = client.getLocalAddress().getHostAddress();
	    int client_port = 55533;
	    Socket socket = new Socket(ip, client_port);
	    OutputStream outputStream = socket.getOutputStream();
	    client.getOutputStream().write(response.getBytes("UTF-8"));
	    outputStream.close();
	    socket.close();
	}
	
	
	public static void listen() throws Exception {
		// ����ָ���Ķ˿�
	    int port = 55533;
	    server = new ServerSocket(port);
	    
	    // server��һֱ�ȴ����ӵĵ���
	    System.out.println("server��һֱ�ȴ����ӵĵ���");
	    Socket socket = server.accept();
	    // ���������Ӻ󣬴�socket�л�ȡ�����������������������ж�ȡ
	    InputStream inputStream = socket.getInputStream();
	    byte[] bytes = new byte[65536];
	    int len;
	    StringBuilder sb = new StringBuilder();
	    len = inputStream.read(bytes);
	    //����ʹ��utf-8
	    sb.append(new String(bytes, 0, len,"UTF-8"));
	    System.out.println("get message from client: " + sb);
	    
	    String response = Searcher.getAnswer(sb+"");
	    System.out.println(response);
	    
	    OutputStream out = socket.getOutputStream();
	    byte[] b = response.getBytes();
	    out.write(b);
	    
	    inputStream.close();
	    out.close();
	    socket.close();
	    server.close();
	} 
	
	public void finalize() throws Exception {
	    server.close();
	}
	    
	    
	public static void main(String[] args) throws Exception {
		while(true) {
			listen();
		}
	}
}
