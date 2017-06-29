package com.jxs.wlanshare;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import org.json.JSONObject;
import java.lang.reflect.Field;

public class WLANShareObject {
	private InetAddress ip;
	private MulticastSocket socket;
	private boolean isShare;

	public void share(String mip, int port) throws Exception {
		this.ip = InetAddress.getByName(mip);
		socket = new MulticastSocket(port);
		socket.setTimeToLive(1);
		socket.joinGroup(ip);
		new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							byte[] a=new byte[4096];
							DatagramPacket packet=new DatagramPacket(a, a.length);
							socket.receive(packet);
							String msg=new String(packet.getData(), 0, packet.getLength());
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
	}

	public boolean isShare() {
		return isShare;
	}
	
	public static String toJson(Object obj) {
		JSONObject d=new JSONObject();
		try {
			Field[] f=obj.getClass().getDeclaredFields();
			for (Field one : f) {
				one.setAccessible(true);
				Class h=one.getType();
				if (h.equals(Object.class)) {
					d.put(one.getName(), toJson(one.get(obj)));
				} else if (h.equals(String.class)) {
					d.put(one.getName(), one.get(obj));
				} else if (h.equals(int.class)) {
					d.put(one.getName(), one.getInt(obj));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
