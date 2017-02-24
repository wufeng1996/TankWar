package com.tank;
import java.awt.*;
 /**
  * 墙类
  * 墙能挡住子弹， 不允许坦克穿越
  * @author 吴锋
  *
  */

public class Wall {
	/**
	 * 墙的坐标(x,y),墙体的宽度，高度
	 */
	private int x,y,width,height;
	/**
	 * 持有TankClient的引用
	 */
	private TankClient tc;
    /**
     * 构造方法
     * @param x
     * @param y
     * @param width
     * @param height
     * @param tc
     */
	public Wall(int x, int y, int width, int height, TankClient tc) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.tc = tc;
	}
	/**
	 * 提供墙体的显示：颜色设置灰色
	 * @param g
	 */
	public void draw(Graphics g){
		Color c=g.getColor();
		g.setColor(Color.GRAY);
		g.fillRect(x, y, width, height);
		g.setColor(c);
	}
	/**
	 * 
	 * @return 返回墙对应的矩形
	 */
	public Rectangle getRect(){
		return new Rectangle(x,y,width,height);
	}
	
	
	
	
}
