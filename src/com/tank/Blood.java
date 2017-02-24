package com.tank;
import java.awt.*;
/**
 * Blood ��ΪѪ���࣬�ṩһ����Ԥ���켣�˶���Ѫ�飬��ʹ��ս̹�˻ָ���Ѫ
 * @author wufeng
 *
 */

public class Blood {
	/**
	 * Ѫ��Ŀ��
	 */
	private static final int BLOOD_WIDTH=20;
	/**
	 * Ѫ��ĸ߶�
	 */
	private static final int BLOOD_HEIGHT=20;
	/**
	 * Ѫ������(x,y)
	 */
	private int x,y;
	/**
	 * ��ʾѪ���Ƿ������������ܷ��ڽ�����ʾ���ܷ���ս̹��ʹ��
	 */
	private boolean live=true;
	/**
	 * Ѫ�鰴Ԥ���켣�˶��ĸ�������
	 */
	private int step=0;
	/**
	 * �������ṩѪ���˶��Ĺ켣�ϵĶ���
	 */
	private int[][] position={{100,100},{120,100},{140,100},{160,100},{180,100},{200,100},{200,120},
			{200,140},{200,160},{180,160},{160,160},{140,160},{120,160},{100,160},{100,140},{100,120},
			{100,100}};
	/**
	 * ����Ĺ��췽��
	 */
	public Blood() {
		this.x = position[0][0];
		this.y = position[0][1];
	}
	/**
	 * �ú�ɫ���δ���Ѫ�飬��ʾ�ڽ�����
	 * @param g Graphics�� �����ʡ�
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
	 * Ѫ����˶�
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
	 * ���Ѫ��ľ��Σ�Rectangle���intersects�������ж��������Ƿ���ײ
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
