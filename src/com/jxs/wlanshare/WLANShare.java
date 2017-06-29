package com.jxs.wlanshare;

import java.lang.reflect.Field;

import org.json.JSONObject;

public class WLANShare {
	public static Object fromJson(String json, Object obj) throws Exception {
		if (json.equalsIgnoreCase("null")) return null;
		JSONObject d=new JSONObject(json);
		try {
			Field[] f=obj.getClass().getDeclaredFields();
			for (Field one : f) {
				one.setAccessible(true);
				Class<?> h=one.getType();
				String n=one.getName();
				if (h.equals(Object.class)) {
					one.set(obj, d.optString(n));
				} else if (h.equals(String.class)) {
					one.set(obj, d.optString(n));
				} else if (h.equals(int.class)) {
					one.setInt(obj, d.optInt(n));
				} else if (h.equals(long.class)) {
					one.setLong(obj, d.optLong(n));
				} else if (h.equals(short.class)) {
					one.setShort(obj, (short) d.optInt(n));
				} else if (h.equals(byte.class)) {
					one.setByte(obj, (byte) d.optInt(n));
				} else if (h.equals(boolean.class)) {
					one.setBoolean(obj, d.optBoolean(n));
				} else if (h.equals(float.class)) {
					one.setFloat(obj, d.optFloat(n));
				} else if (h.equals(double.class)) {
					one.setDouble(obj, d.optDouble(n));
				} else if (h.equals(char.class)) {
					one.setChar(obj, (char) d.opt(n));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static String toJson(Object obj) {
		if (obj == null) return "{\"null\"}";
		JSONObject d=new JSONObject();
		try {
			Field[] f=obj.getClass().getDeclaredFields();
			for (Field one : f) {
				one.setAccessible(true);
				Class<?> h=one.getType();
				String n=one.getName();
				if (h.equals(String.class)) {
					d.put(n, one.get(obj));
				} else if (Object.class.isAssignableFrom(h)) {
					d.put(n, toJson(one.get(obj)));
				} else if (h.equals(int.class)) {
					d.put(n, one.getInt(obj));
				} else if (h.equals(long.class)) {
					d.put(n, one.getLong(obj));
				} else if (h.equals(short.class)) {
					d.put(n, one.getShort(obj));
				} else if (h.equals(byte.class)) {
					d.put(n, one.getByte(obj));
				} else if (h.equals(boolean.class)) {
					d.put(n, one.getBoolean(obj));
				} else if (h.equals(float.class)) {
					d.put(n, one.getFloat(obj));
				} else if (h.equals(double.class)) {
					d.put(n, one.getDouble(obj));
				} else if (h.equals(char.class)) {
					d.put(n, one.getChar(obj));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d.toString();
	}
	
	public static WLANShareObj share(Shareable obj, String ip, int port, String mark, OnErrorListener listener) {
		WLANShareObj o=new WLANShareObj(obj, ip, port, mark, listener);
		o.share();
		return o;
	}
}
