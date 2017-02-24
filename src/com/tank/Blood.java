package com.tank;
import java.awt.*;
/**
 * Blood 类为血块类，提供一个按预定轨迹运动的血块，可使主战坦克恢复满血
 * @author wufeng
 *
 */

public class Blood {
	/**
	 * 血块的宽度
	 */
	private static final int BLOOD_WIDTH=20;
	/**
	 * 血块的高度
	 */
	private static final int BLOOD_HEIGHT=20;
	/**
	 * 血块坐标(x,y)
	 */
	private int x,y;
	/**
	 * 表示血块是否存活，这决定了它能否在界面显示，能否被主战坦克使用
	 */
	private boolean live=true;
	/**
	 * 血块按预定轨迹运动的辅助变量
	 */
	private int step=0;
	/**
	 * 该数组提供血块运动的轨迹上的定点
	 */
	private int[][] position={{100,100},{120,100},{140,100},{160,100},{180,100},{200,100},{200,120},
			{200,140},{200,160},{180,160},{160,160},{140,160},{120,160},{100,160},{100,140},{100,120},
			{100,100}};
	/**
	 * 此类的构造方法
	 */
	public Blood() {
		this.x = position[0][0];
		this.y = position[0][1];
	}
	/**
	 * 用红色矩形代表血块，显示在界面上
	 * @param g Graphics类 “画笔”
	 */
	public void draw(Graphics g){
		if(!live)  return;
		Color c=g.getColor();
		g.setColor(Color.RED);
		g.fillRect(x, y, BLOOD_WIDTH, BLOOD_HEIGHT);
		g.setColor(c);
		
		move();
	}
	/**
	 * 血块的运动
	 */
	public void move(){
		step++;
		if(step==position.length){	
			step=0;
		}
		x=position[step][0];
		y=position[step][1];
	}
	/**
	 * 获得血块的矩形，Rectangle类的intersects方法能判断两矩形是否碰撞
	 * @return
	 */
	public Rectangle getRect(){
		return new Rectangle(x,y,BLOOD_WIDTH,BLOOD_HEIGHT);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
	
}
