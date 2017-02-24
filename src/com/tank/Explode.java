package com.tank;
import java.awt.*;
/**
 * ����ͨ�����ɸ���С���ȵ�԰ģ�ⱬը���̣�ÿ��̹�˱�����ʱ����һ����ը�����
 * @author ���
 *
 */

public class Explode {
	/**
	 * ��ը����(x,y)
	 */
	private int x,y;
	/**
	 * �жϱ�ը�Ƿ񱻼���Ĳ�����
	 */
	private boolean live=true;
	/**
	 * ����TankClient������ã�(��ܼ�)
	 */
	private TankClient tc;
	/**
	 * �������ʾģ�ⱬըЧ��������Բ��ֱ��
	 */
	private int[] diameter={6,12,18,24,30,48,25,15,8};
	/**
	 * ������������������diameter
	 */
	private int step=0;
	/**
	 * ���췽��
	 * @param x  ������
	 * @param y  ������
	 * @param tc TankClient ������
	 */
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	/**
	 * ����ը�����ڱ�����״̬��live==false��ʱ�����������Ƴ������ڽ�����ʾ
	 * ��step���������Բֱ�������飬ÿ��ʾ��һ�α�ը��step����Ϊ0
	 * @param g
	 */
	public void draw(Graphics g){
		if(!live) {
			tc.explodes.remove(this);
			return;
		}	
		if(step >= diameter.length){ // step==diameter.lengthʱ�������Ѿ�����һ�α�ը��step����Ϊ0
			live=false;
			step=0;
			return;
		}
		Color c=g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(x, y, diameter[step], diameter[step++]);
		g.setColor(c);			
	}
	
	
	
}
