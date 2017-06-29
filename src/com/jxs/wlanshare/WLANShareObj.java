package com.jxs.wlanshare;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class WLANShareObj {
	public static final int MAX=4096;

	private InetAddress ip;
	private MulticastSocket socket;
	private Shareable obj;
	private String mark;
	private int port;
	private boolean isShare;
	private OnErrorListener listener=new OnErrorListener() {
		@Override
		public void onError(Exception e) {
			e.printStackTrace();
		}
	};

	public WLANShareObj(Shareable obj, String ipe, int port, String mark, OnErrorListener listen) {
		try {
			if (listen != null) listener = listen;
			this.mark = mark;
			this.obj = obj;
			this.port = port;
			ip = InetAddress.getByName(ipe);
			socket = new MulticastSocket(port);
		} catch (Exception e) {
			err(e);
		}
	}

	public void share() {
		try {
			socket.setTimeToLive(1);
			socket.joinGroup(ip);
			isShare = true;
			new Thread(new Runnable() {
					@Override
					public void run() {
						while (isShare) {
							try {
								byte[] a=new byte[MAX];
								DatagramPacket packet=new DatagramPacket(a, a.length);
								socket.receive(packet);
								String msgg=new String(packet.getData(), 0, packet.getLength());
								System.out.println(msgg);
								String[] msg=msgg.split(":", 2);
								if (msg[0].equals(mark)) {
									WLANShare.fromJson(msg[1], obj);
									obj.onUpdate();
								}
							} catch (Exception e) {
								err(e);
							}
						}
					}
				}).start();
			update();
		} catch (Exception e) {
			err(e);
		}
	}

	public void update() {
		if (!isShare) return;
		new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						byte[] data=(mark + ":" + WLANShare.toJson(obj)).getBytes();
						DatagramPacket packet=new DatagramPacket(data, data.length, ip, port);
						socket.send(packet);
					} catch (Exception e) {
						err(e);
					}
				}
			}).start();
	}

	public void stop() {
		try {
			isShare = false;
			socket.leaveGroup(ip);
			ip = null; socket = null;
		} catch (Exception e) {
			err(e);
		}
	}

	public boolean isShare() {
		return isShare;
	}

	public void setShare(boolean isShare) {
		this.isShare = isShare;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public InetAddress getIp() {
		return ip;
	}

	public void setIp(InetAddress ip) {
		this.ip = ip;
	}

	public MulticastSocket getSocket() {
		return socket;
	}

	public void setSocket(MulticastSocket socket) {
		this.socket = socket;
	}

	public Shareable getObj() {
		return obj;
	}

	public void setObj(Shareable obj) {
		this.obj = obj;
	}

	private void err(Exception e) {
		if (listener != null) listener.onError(e);
	}
}
