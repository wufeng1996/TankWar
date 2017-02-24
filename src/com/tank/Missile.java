package com.tank;
import java.awt.*;
import java.util.List;
/**
 * �ӵ���  �����������������boolean live,boolean good ������Ҫ
 * �ṩ���ӵ����ƶ�move(),hitTank(),hitTanks(),hitWall()
 * @author ���
 *
 */

public class Missile {
	/**
	 * �ӵ���x�����ϵ������ٶȣ���y�����ϵı���һ�£������޷��ṩ��ֱ�켣
	 */
	private static final int xspeed=10;
	/**
	 * �ӵ���y�����ϵ������ٶȣ���x�����ϵı���һ�£������޷��ṩ��ֱ�켣
	 */
	private static final int yspeed=10;
	/**
	 * ��Բ��ʾһ���ӵ�����ʾ�ӵ��Ŀ��
	 */
	private static final int MISSILE_WIDTH=8;
	/**
	 * ��ʾ�ӵ��ĸ߶�
	 */
	private static final int MISSILE_HEIGHT=8;
	/**
	 * �ӵ�������(x,y)
	 */
	private int x,y;
	/**
	 * �ӵ��ķ���ΪTank���Direction ö������
	 */
	Tank.Direction dir;
	/**
	 * ��ʾ�ӵ����Ĳ���ֵ
	 */
	private boolean live=true;
	/**
	 * ��ʾ�ӵ��û��Ĳ����������Ӧ��̹�˺û�����ֵ����һ��
	 */
	private boolean good;
	/**
	 * ����TankClient������
	 */
	private TankClient tc;
	/**
	 * �ӵ���Ĺ��췽��
	 * @param x
	 * @param y
	 * @param dir
	 */
	public Missile(int x, int y, Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	/**
	 * �ӵ���Ĺ��췽��
	 * @param x
	 * @param y
	 * @param good
	 * @param dir
	 * @param tc
	 */
	public Missile(int x,int y,boolean good,Tank.Direction dir,TankClient tc){
		this(x,y,dir);
		this.good=good;
		this.tc=tc;
	}
	/**
	 * ���ӵ���live==falseʱ���������Ƴ�������ʾ�ڽ���
	 * �ѷ�̹���ӵ���ʾ����ɫ�����з�̹���ӵ���ʾ����ɫ��
	 * �ڲ�������move()��ÿ��ʾ��һ���ӵ���λ�ã����������˶������γɶ�̬Ч��
	 * @param g
	 */
	public void draw(Graphics g){
		if(!live){
			tc.missiles.remove(this);
			return;
		}		
		Color c=g.getColor();
		if(good) g.setColor(Color.BLACK);
		else g.setColor(Color.white);
		
		g.fillOval(x, y, MISSILE_WIDTH, MISSILE_HEIGHT);
		g.setColor(c);
		move();
	}
    /**
     *  �����ӵ��ķ���(dir:Tank.Direction),�ı��ӵ�������
     *  ���ӵ�Խ����д���Խ����ӵ���Ч
     */
	private void move() {
		switch(dir){
		case Left:  x-=xspeed;break;
		case Right: x+=xspeed;break;
		case Up: y-=xspeed;break;
		case Down: y+=xspeed;break;
		case LeftUp: x-=xspeed;y-=yspeed;break;
		case LeftDown: x-=xspeed;y+=yspeed;break;
		case RightUp: x+=xspeed;y-=yspeed;break;
		case RightDown: x+=xspeed;y+=yspeed;break;
		case Stop: y+=yspeed;break;
		}
		if(x<0 ||y<0|| x>TankClient.GAME_WIDTH ||y>TankClient.GAME_HEIGHT){
			live=false;
		}
	}
	/**
	 * 
	 * @return �ӵ���Ӧ�ľ���
	 */
	public Rectangle getRect(){
		return new Rectangle(x,y,MISSILE_WIDTH,MISSILE_HEIGHT);
	}
	/**
	 * ��һ��̹�˽��й���
	 * ͨ��Rectangle��intersects�������ж��ӵ����ڵľ�����̹�˵ľ����Ƿ�����ײ
	 * ֻ�е��ӵ���̹��ͬʱ����ײ����Ч�����ҹ涨������̹�˵��ӵ�����������̹��
	 * ��̹�˱�����ʱ������һ�α�ըЧ��
	 * @param t ���ܱ��ӵ����е�̹��
	 * @return  �ӵ����У�����true������false
	 */
	public boolean hitTank(Tank t){
		if(this.getRect().intersects(t.getRect()) && t.isLive() && good!=t.isGood()){
			if(t.getLife()>10 && t==tc.myTank){ 
				t.setLife(t.getLife()-10);  //��ս̹��һ�ο�10��Ѫ
				this.live=false; //�ӵ�����Ϊ����״̬�������̹����ɴ�͸�˺�
				return true;
			}
			else if(t.getLife()>20 && t!=tc.myTank){ //������̹��һ�ο�20��Ѫ
				t.setLife(t.getLife()-20);
				this.live=false;
				return true;
			}
			else{   //����ս̹�˺ͻ�����̹�������һ��
				t.setLive(false);  
				t.setLife(0);
				this.live=false;			
				tc.explodes.add(new Explode(x,y,this.tc));			
				return true;
			}		
		}
		return false;
	}
	/**
	 * ��һȺ̹�˽��й���
	 * ѭ������hitTank(Tank t)
	 * @param tanks ���̹�˵�����
	 * @return  һ����������һ��̹�ˣ�����true�����򷵻�false
	 */
	public boolean hitTanks(List<Tank> tanks){
		for(int i=0;i<tanks.size();i++){
			if(this.hitTank(tanks.get(i))){
				return true;
			}
		}
		return false;
	}
      /**
       * �ӵ�����ǽʱ���ӵ�����
       * @param w  �п��ܱ��ӵ����е�ǽ
       * @return  ���з���true�����򷵻�false
       */
	public boolean hitWall(Wall w){
		if(live && this.getRect().intersects(w.getRect())){
			live=false;
			return true;
		}
		return false;
	}
	
	
}
