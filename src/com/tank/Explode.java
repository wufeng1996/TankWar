package com.tank;
import java.awt.*;
/**
 * 此类通过若干个大小不等的园模拟爆炸过程，每次坦克被击毙时产生一个爆炸类对象
 * @author 吴锋
 *
 */

public class Explode {
	/**
	 * 爆炸坐标(x,y)
	 */
	private int x,y;
	/**
	 * 判断爆炸是否被激活的布尔量
	 */
	private boolean live=true;
	/**
	 * 持有TankClient类的引用，(大管家)
	 */
	private TankClient tc;
	/**
	 * 该数组表示模拟爆炸效果的若干圆的直径
	 */
	private int[] diameter={6,12,18,24,30,48,25,15,8};
	/**
	 * 辅助变量：遍历数组diameter
	 */
	private int step=0;
	/**
	 * 构造方法
	 * @param x  横坐标
	 * @param y  纵坐标
	 * @param tc TankClient 的引用
	 */
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	/**
	 * 当爆炸不处于被激活状态（live==false）时，从容器中移除，不在界面显示
	 * 用step遍历存放了圆直径的数组，每显示完一次爆炸，step重置为0
	 * @param g
	 */
	public void draw(Graphics g){
		if(!live) {
			tc.explodes.remove(this);
			return;
		}	
		if(step >= diameter.length){ // step==diameter.length时，表面已经画完一次爆炸，step重置为0
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
