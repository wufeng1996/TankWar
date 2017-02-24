package com.tank;
import java.awt.*;
import java.util.List;
/**
 * 子弹类  此类的两个布尔变量boolean live,boolean good 至关重要
 * 提供了子弹的移动move(),hitTank(),hitTanks(),hitWall()
 * @author 吴锋
 *
 */

public class Missile {
	/**
	 * 子弹在x方向上的运行速度，与y方向上的必须一致，否则无法提供径直轨迹
	 */
	private static final int xspeed=10;
	/**
	 * 子弹在y方向上的运行速度，与x方向上的必须一致，否则无法提供径直轨迹
	 */
	private static final int yspeed=10;
	/**
	 * 用圆表示一颗子弹，表示子弹的宽度
	 */
	private static final int MISSILE_WIDTH=8;
	/**
	 * 表示子弹的高度
	 */
	private static final int MISSILE_HEIGHT=8;
	/**
	 * 子弹的坐标(x,y)
	 */
	private int x,y;
	/**
	 * 子弹的方向，为Tank类的Direction 枚举类型
	 */
	Tank.Direction dir;
	/**
	 * 表示子弹存活的布尔值
	 */
	private boolean live=true;
	/**
	 * 表示子弹好坏的布尔量，与对应的坦克好坏布尔值保持一致
	 */
	private boolean good;
	/**
	 * 持有TankClient的引用
	 */
	private TankClient tc;
	/**
	 * 子弹类的构造方法
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
	 * 子弹类的构造方法
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
	 * 当子弹的live==false时，从容器移除，不显示在界面
	 * 友方坦克子弹显示“黑色”，敌方坦克子弹显示“白色”
	 * 内部调用了move()，每显示完一次子弹的位置，让其重新运动，以形成动态效果
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
     *  根据子弹的方向(dir:Tank.Direction),改变子弹的坐标
     *  对子弹越界进行处理，越界的子弹无效
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
	 * @return 子弹对应的矩形
	 */
	public Rectangle getRect(){
		return new Rectangle(x,y,MISSILE_WIDTH,MISSILE_HEIGHT);
	}
	/**
	 * 对一辆坦克进行攻击
	 * 通过Rectangle的intersects方法，判断子弹所在的矩形与坦克的矩形是否相碰撞
	 * 只有当子弹与坦克同时存活，碰撞才有效，并且规定，己方坦克的子弹不攻击己方坦克
	 * 在坦克被击毙时，产生一次爆炸效果
	 * @param t 可能被子弹击中的坦克
	 * @return  子弹击中，返回true，否则false
	 */
	public boolean hitTank(Tank t){
		if(this.getRect().intersects(t.getRect()) && t.isLive() && good!=t.isGood()){
			if(t.getLife()>10 && t==tc.myTank){ 
				t.setLife(t.getLife()-10);  //主战坦克一次扣10点血
				this.live=false; //子弹定义为死亡状态，否则对坦克造成穿透伤害
				return true;
			}
			else if(t.getLife()>20 && t!=tc.myTank){ //机器人坦克一次扣20点血
				t.setLife(t.getLife()-20);
				this.live=false;
				return true;
			}
			else{   //对主战坦克和机器人坦克作最后一击
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
	 * 对一群坦克进行攻击
	 * 循环调用hitTank(Tank t)
	 * @param tanks 存放坦克的容器
	 * @return  一旦击中任意一辆坦克，返回true，否则返回false
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
       * 子弹击中墙时，子弹消亡
       * @param w  有可能被子弹击中的墙
       * @return  击中返回true，否则返回false
       */
	public boolean hitWall(Wall w){
		if(live && this.getRect().intersects(w.getRect())){
			live=false;
			return true;
		}
		return false;
	}
	
	
}
