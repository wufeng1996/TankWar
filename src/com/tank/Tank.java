package com.tank;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.List;

/**
 * 本项目最基础，最重要的类
 * 最好将机器人坦克重新封装成另一个类
 * @author Administrator
 *
 */
public class Tank {
	/**
	 * 坦克在x方向的运行速度，与y方向上的必须一致，否则无法提供径直轨迹
	 */
	private static final int xspeed=5;
	/**
	 * 坦克在y方向的运行速度，与x方向上的必须一致，否则无法提供径直轨迹
	 */
	private static final int yspeed=5;
	/**
	 * 坦克的宽度
	 */
	private static final int TANK_WIDTH=30;
	/**
	 * 坦克的高度
	 */
	private static final int TANK_HEIGHT=30;
	/**
	 * 坦克的总血量
	 */
    private static final int TANK_LIFE=100;
	/**
	 * 坦克的坐标(x,y)
	 */
	private int x,y;
	/**
	 * 坦克前一刻的坐标(x,y),允许坦克与坦克碰撞或与墙体碰撞时能返回前一刻位置
	 */
	private int oldX,oldY;
	/**
	 * 坦克的血量，被子弹击中会减少
	 */
	private int life=TANK_LIFE;
	/**
	 * 坦克的血条：Tank的内部类 BloodBar
	 */
	private BloodBar bb=new BloodBar();
	/**
	 * 代表坦克好坏的布尔量
	 */
	private boolean good;
	/**
	 * 代表坦克存活的布尔量
	 */
	private boolean live=true;
	/**
	 * 随机数种子:java util.Random
	 */
	private static Random r=new Random();
	/**
	 * 以随机数种子生成的在[5,16]的随机整数
	 */
	private int step=r.nextInt(12)+5; 
    /**
     * 持有TankClient的引用
     */
	private TankClient tc=null;
	/**
	 * 代表方向盘按键的布尔值，true表示按键状态
	 */
	private boolean bLeft=false,bRight=false,bUp=false,bDown=false;
	/**
	 * 内部类：枚举类型，定义了9个方向
	 * @author 
	 *
	 */
	enum Direction {Left,Up,Right,Down,LeftUp,RightUp,LeftDown,RightDown,Stop}
	/**
	 * 坦克的运动方向，初始化为Stop
	 */
	private Direction dir=Direction.Stop;
	/**
	 * 炮筒方向，初始化为Stop
	 */
	private Direction barrel_dir=Direction.Stop;
	
	public Tank(int x, int y,boolean good) {
		this.x = x;
		this.y = y;
		this.good=good;
	}
	
	public Tank(int x,int y,boolean good,Direction dir,TankClient tc){
		this(x,y,good);
		this.oldX=x;
		this.oldY=y;
		this.dir=dir;
		this.tc=tc;
	}
	/**
	 * 显示坦克，坦克血条，炮筒。
	 * 主站坦克 淡蓝色，友方坦克蓝色，敌方坦克红色
	 * 坦克炮筒方向默认为Direction.Down
	 * 当坦克消亡时，即live==false,从容器中移除。
	 * @param g
	 */
	
	public void draw(Graphics g){
		if(!live){
			tc.tanks.remove(this);
			return;
		}
		
		this.move(); //放在draw方法前后的无所谓。 
		Color c=g.getColor();
		
		if(this==tc.myTank) g.setColor(Color.CYAN);
		else if(good==true)  g.setColor(Color.BLUE);
		else  g.setColor(Color.RED);
		
		g.fillOval(x, y, TANK_WIDTH,TANK_HEIGHT);			
		g.setColor(c);
		//画出坦克血条
		bb.draw(g); 
		//画出炮筒
		switch(barrel_dir){
		case Left:
			g.drawLine(x, y+TANK_HEIGHT/2, x+TANK_WIDTH/2, y+TANK_HEIGHT/2);
			break;
		case Right:
			g.drawLine(x+TANK_WIDTH, y+TANK_HEIGHT/2, x+TANK_WIDTH/2, y+TANK_HEIGHT/2);
			break;
		case Up: 
			g.drawLine(x+TANK_WIDTH/2, y, x+TANK_WIDTH/2, y+TANK_HEIGHT/2);
			break;
		case Down:
		case Stop:
			g.drawLine(x+TANK_WIDTH/2, y+TANK_HEIGHT, x+TANK_WIDTH/2, y+TANK_HEIGHT/2);
			break;
		case LeftUp:
			g.drawLine(x, y, x+TANK_WIDTH/2, y+TANK_HEIGHT/2);
			break;
		case LeftDown:
			g.drawLine(x, y+TANK_HEIGHT, x+TANK_WIDTH/2, y+TANK_HEIGHT/2);
			break;
		case RightUp:
			g.drawLine(x+TANK_WIDTH, y, x+TANK_WIDTH/2, y+TANK_HEIGHT/2);
			break;
		case RightDown:
			g.drawLine(x+TANK_WIDTH, y+TANK_HEIGHT, x+TANK_WIDTH/2, y+TANK_HEIGHT/2);
			break;
		}
	}
	/**
	 * 对方向盘的按键处理
	 * @param e KeyEvent类型，调用getKeyCode()获取键值
	 */
	public void keyPressed(KeyEvent e){
		int key=e.getKeyCode();
		switch(key){
		case KeyEvent.VK_LEFT :
			bLeft=true;
			break;
		case KeyEvent.VK_RIGHT :
			bRight=true;
			break;
		case KeyEvent.VK_UP :
			bUp=true;
			break;
		case KeyEvent.VK_DOWN:
			bDown=true;
			break;
		}
		
		
	}
    /**
     * 对“松开键”事件处理
     * @param e KeyEvent类型，调用getKeyCode()获取键值
     */
	public void keyRealeased(KeyEvent e) {
		int key=e.getKeyCode();
		switch(key){
		case KeyEvent.VK_Q:
			tc.addEnemy();
			break;
		case KeyEvent.VK_W:
			tc.addFriend();
			break;
		case KeyEvent.VK_R:
			tc.b.setLive(true);
			break;
		case KeyEvent.VK_E:
			tc.myTank.setLive(true);
			tc.myTank.setLife(TANK_LIFE);
			break;
		case KeyEvent.VK_Z :
			fire();
			break;
		case KeyEvent.VK_A :
			superFire();
			break;
		case KeyEvent.VK_LEFT :
			bLeft=false;
			break;
		case KeyEvent.VK_RIGHT :
			bRight=false;
			break;
		case KeyEvent.VK_UP :
			bUp=false;
			break;
		case KeyEvent.VK_DOWN:
			bDown=false;
			break;
		}	
	}
	/**
	 * 每调用一次该方法，以当前坦克对象的好坏，此时的炮筒方向，生成一个子弹类的对象，并将生成的子弹对象加进容器
	 * 坦克消亡时，fire()调用无效
	 * @return
	 */
	public Missile fire() {
		if(!live) return null;
		
		int x=this.x + TANK_WIDTH/2;
		int y=this.y + TANK_HEIGHT/2;
		
		Missile m=new Missile(x,y,this.good,barrel_dir,this.tc);
		tc.missiles.add(m);
		return m;
	} 
	/**
	 * 人为提供坦克开火后，产生的子弹的方向
	 * 坦克消亡后，fire()调用无效
	 * @param dir 子弹的方向
	 * @return
	 */
	public Missile fire(Direction dir){
		if(!live) return null;
		
		int x=this.x + TANK_WIDTH/2;
		int y=this.y + TANK_HEIGHT/2;
		
		Missile m=new Missile(x,y,this.good,dir,this.tc);
		tc.missiles.add(m);
		return m;
	}
    /**
     * 依据方向盘的按键状态，确定坦克的运动方向
     * 默认状态下：主站坦克Stop,机器人坦克利用随机数种子(Random r) 随机时段后随机改变方向
     */
	public void setDirection(){
		if(bLeft && !bRight && !bUp && !bDown)
			dir=Direction.Left;
		else if(!bLeft && bRight && !bUp && !bDown)
			dir=Direction.Right;
		else if(!bLeft && !bRight && bUp && !bDown)
			dir=Direction.Up;
		else if(!bLeft && !bRight && !bUp && bDown)
			dir=Direction.Down;
		else if(bLeft && !bRight && bUp && !bDown)
			dir=Direction.LeftUp;
		else if(bLeft && !bRight && !bUp && bDown)
			dir=Direction.LeftDown;
		else if(!bLeft && bRight && bUp && !bDown)
			dir=Direction.RightUp;
		else if(!bLeft && bRight && !bUp && bDown)
			dir=Direction.RightDown;
		else{
			if(this==tc.myTank)
				dir=Direction.Stop;	
			else{
				Direction[] dirs=Direction.values();
				if(step==0){
					step=r.nextInt(12)+5;
					int num=r.nextInt(dirs.length);
					dir=dirs[num];
				}
				step--;
			}			
		} 	
	}
	/**
	 * 根据坦克的方向，进行相关的移动
	 * 解决坦克的越界问题，机器人坦克随机开火
	 */
	public void move(){
		this.oldX=x;  //每次移动前，记录坦克的位置坐标，作为移动后的前一刻位置
		this.oldY=y;
		
		this.setDirection();
		if(dir!=Direction.Stop) barrel_dir=dir; //当坦克方向不是Stop时，才把炮筒方向改为坦克的运动方向
		switch(dir){
		case Left:  x-=xspeed;break;
		case Right: x+=xspeed;break;
		case Up: y-=yspeed;break;
		case Down: y+=yspeed;break;
		case LeftUp: x-=xspeed;y-=yspeed;break;
		case LeftDown: x-=xspeed;y+=yspeed;break;
		case RightUp: x+=xspeed;y-=yspeed;break;
		case RightDown: x+=xspeed;y+=yspeed;break;
		case Stop: break;	
		}
		//解决坦克出界问题
		if(x<0) x=0;
		if(y<30) y=30;
		if(x+Tank.TANK_WIDTH>TankClient.GAME_WIDTH) x=TankClient.GAME_WIDTH-Tank.TANK_WIDTH;
		if(y+Tank.TANK_HEIGHT>TankClient.GAME_HEIGHT) y=TankClient.GAME_HEIGHT-Tank.TANK_HEIGHT;
		//机器人坦克随机发炮弹
		if(this!=tc.myTank){
			if(r.nextInt(40)>35)
				this.fire();
		}		
	}
	/**
	 * 
	 * @return 坦克对应的矩形
	 */
	public Rectangle getRect(){
		return new Rectangle(x,y,TANK_WIDTH,TANK_HEIGHT);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isGood() {
		return good;
	}
	/**
	 * 允许坦克返回前一刻位置
	 */
	public void stay() {
		x=oldX;
		y=oldY;
	}
	/**
	 * 若坦克撞墙，则返回前一刻状态
	 * @param w Wall类
	 * @return 撞上返回true，否则false
	 */
	public boolean hitWall(Wall w){
		if(live && this.getRect().intersects(w.getRect())){
			stay();
			return true;
		}
		return false;
	}
	/**
	 * 解决坦克覆盖的问题，当两辆坦克碰撞时，各自回到前一刻位置，禁止互相穿越
	 * @param tanks 存放一堆坦克的容器
	 * @return
	 */
	public boolean hitTanks(List<Tank> tanks){
		for(int i=0;i<tanks.size();i++){
			Tank t=tanks.get(i);
			if(this!=t){
				if(this.getRect().intersects(t.getRect()) && live && t.live ){
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}	
	/**
	 * 坦克吃血块，血量补满
	 * @param b
	 * @return
	 */
	public boolean eat(Blood b){
		if(live && b.isLive() && this.getRect().intersects(b.getRect())){
			life=TANK_LIFE;
			b.setLive(false);
			return true;
		}
		return false;
	}
	
	/**
	 * 超级开火，朝8个方向各发射一颗子弹
	 */
	public void superFire(){
		Direction[] dirs=Direction.values();
		for(int i=0;i<8;i++){
			fire(dirs[i]);
		}
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	/**
	 * 血条，内部类 ，含draw()
	 * @author Administrator
	 */
	private class BloodBar{
		
		public void draw(Graphics g){  //一个空心矩形与一个根据坦克血量改变宽度的实心矩形覆盖显示，来表示血条
			Color c=g.getColor();
			g.setColor(Color.GRAY);
			g.drawRect(x-2,y-6,TANK_WIDTH+4,4);
			int blood_width=(TANK_WIDTH+4)*life/TANK_LIFE;
			g.fillRect(x-2, y-6, blood_width, 4);
			g.setColor(c);
		}
	}
	
	
	
	
}