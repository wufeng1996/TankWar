package com.tank;
import java.awt.*;
 /**
  * ǽ��
  * ǽ�ܵ�ס�ӵ��� ������̹�˴�Խ
  * @author ���
  *
  */

public class Wall {
	/**
	 * ǽ������(x,y),ǽ��Ŀ�ȣ��߶�
	 */
	private int x,y,width,height;
	/**
	 * ����TankClient������
	 */
	private TankClient tc;
    /**
     * ���췽��
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
	 * �ṩǽ�����ʾ����ɫ���û�ɫ
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
	 * @return ����ǽ��Ӧ�ľ���
	 */
	public Rectangle getRect(){
		return new Rectangle(x,y,width,height);
	}
	
	
	
	
}
