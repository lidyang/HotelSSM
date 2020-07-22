package com.ldy.programmer.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 验证码生成器
 * 
 * @author llq
 */
public class DateUtil {
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	
	public static String dateToStr(Date date) {
		return sdf.format(date);
	}
	
	public static Date strToDate(String str) throws ParseException {
		return sdf.parse(str);
	}
}
