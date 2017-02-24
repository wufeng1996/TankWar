package com.tank;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.List;

/**
 * ����Ŀ�����������Ҫ����
 * ��ý�������̹�����·�װ����һ����
 * @author Administrator
 *
 */
public class Tank {
	/**
	 * ̹����x����������ٶȣ���y�����ϵı���һ�£������޷��ṩ��ֱ�켣
	 */
	private static final int xspeed=5;
	/**
	 * ̹����y����������ٶȣ���x�����ϵı���һ�£������޷��ṩ��ֱ�켣
	 */
	private static final int yspeed=5;
	/**
	 * ̹�˵Ŀ��
	 */
	private static final int TANK_WIDTH=30;
	/**
	 * ̹�˵ĸ߶�
	 */
	private static final int TANK_HEIGHT=30;
	/**
	 * ̹�˵���Ѫ��
	 */
    private static final int TANK_LIFE=100;
	/**
	 * ̹�˵�����(x,y)
	 */
	private int x,y;
	/**
	 * ̹��ǰһ�̵�����(x,y),����̹����̹����ײ����ǽ����ײʱ�ܷ���ǰһ��λ��
	 */
	private int oldX,oldY;
	/**
	 * ̹�˵�Ѫ�������ӵ����л����
	 */
	private int life=TANK_LIFE;
	/**
	 * ̹�˵�Ѫ����Tank���ڲ��� BloodBar
	 */
	private BloodBar bb=new BloodBar();
	/**
	 * ����̹�˺û��Ĳ�����
	 */
	private boolean good;
	/**
	 * ����̹�˴��Ĳ�����
	 */
	private boolean live=true;
	/**
	 * ���������:java util.Random
	 */
	private static Random r=new Random();
	/**
	 * ��������������ɵ���[5,16]���������
	 */
	private int step=r.nextInt(12)+5; 
    /**
     * ����TankClient������
     */
	private TankClient tc=null;
	/**
	 * �������̰����Ĳ���ֵ��true��ʾ����״̬
	 */
	private boolean bLeft=false,bRight=false,bUp=false,bDown=false;
	/**
	 * �ڲ��ࣺö�����ͣ�������9������
	 * @author 
	 *
	 */
	enum Direction {Left,Up,Right,Down,LeftUp,RightUp,LeftDown,RightDown,Stop}
	/**
	 * ̹�˵��˶����򣬳�ʼ��ΪStop
	 */
	private Direction dir=Direction.Stop;
	/**
	 * ��Ͳ���򣬳�ʼ��ΪStop
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
	 * ��ʾ̹�ˣ�̹��Ѫ������Ͳ��
	 * ��վ̹�� ����ɫ���ѷ�̹����ɫ���з�̹�˺�ɫ
	 * ̹����Ͳ����Ĭ��ΪDirection.Down
	 * ��̹������ʱ����live==false,���������Ƴ���
	 * @param g
	 */
	
	public void draw(Graphics g){
		if(!live){
			tc.tanks.remove(this);
			return;
		}
		
		this.move(); //����draw����ǰ�������ν�� 
		Color c=g.getColor();
		
		if(this==tc.myTank) g.setColor(Color.CYAN);
		else if(good==true)  g.setColor(Color.BLUE);
		else  g.setColor(Color.RED);
		
		g.fillOval(x, y, TANK_WIDTH,TANK_HEIGHT);			
		g.setColor(c);
		//����̹��Ѫ��
		bb.draw(g); 
		//������Ͳ
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
	 * �Է����̵İ�������
	 * @param e KeyEvent���ͣ�����getKeyCode()��ȡ��ֵ
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
     * �ԡ��ɿ������¼�����
     * @param e KeyEvent���ͣ�����getKeyCode()��ȡ��ֵ
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
	 * ÿ����һ�θ÷������Ե�ǰ̹�˶���ĺû�����ʱ����Ͳ��������һ���ӵ���Ķ��󣬲������ɵ��ӵ�����ӽ�����
	 * ̹������ʱ��fire()������Ч
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
	 * ��Ϊ�ṩ̹�˿���󣬲������ӵ��ķ���
	 * ̹��������fire()������Ч
	 * @param dir �ӵ��ķ���
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
     * ���ݷ����̵İ���״̬��ȷ��̹�˵��˶�����
     * Ĭ��״̬�£���վ̹��Stop,������̹���������������(Random r) ���ʱ�κ�����ı䷽��
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
	 * ����̹�˵ķ��򣬽�����ص��ƶ�
	 * ���̹�˵�Խ�����⣬������̹���������
	 */
	public void move(){
		this.oldX=x;  //ÿ���ƶ�ǰ����¼̹�˵�λ�����꣬��Ϊ�ƶ����ǰһ��λ��
		this.oldY=y;
		
		this.setDirection();
		if(dir!=Direction.Stop) barrel_dir=dir; //��̹�˷�����Stopʱ���Ű���Ͳ�����Ϊ̹�˵��˶�����
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
		//���̹�˳�������
		if(x<0) x=0;
		if(y<30) y=30;
		if(x+Tank.TANK_WIDTH>TankClient.GAME_WIDTH) x=TankClient.GAME_WIDTH-Tank.TANK_WIDTH;
		if(y+Tank.TANK_HEIGHT>TankClient.GAME_HEIGHT) y=TankClient.GAME_HEIGHT-Tank.TANK_HEIGHT;
		//������̹��������ڵ�
		if(this!=tc.myTank){
			if(r.nextInt(40)>35)
				this.fire();
		}		
	}
	/**
	 * 
	 * @return ̹�˶�Ӧ�ľ���
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
	 * ����̹�˷���ǰһ��λ��
	 */
	public void stay() {
		x=oldX;
		y=oldY;
	}
	/**
	 * ��̹��ײǽ���򷵻�ǰһ��״̬
	 * @param w Wall��
	 * @return ײ�Ϸ���true������false
	 */
	public boolean hitWall(Wall w){
		if(live && this.getRect().intersects(w.getRect())){
			stay();
			return true;
		}
		return false;
	}
	/**
	 * ���̹�˸��ǵ����⣬������̹����ײʱ�����Իص�ǰһ��λ�ã���ֹ���ഩԽ
	 * @param tanks ���һ��̹�˵�����
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
	 * ̹�˳�Ѫ�飬Ѫ������
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
	 * �������𣬳�8�����������һ���ӵ�
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
	 * Ѫ�����ڲ��� ����draw()
	 * @author Administrator
	 */
	private class BloodBar{
		
		public void draw(Graphics g){  //һ�����ľ�����һ������̹��Ѫ���ı��ȵ�ʵ�ľ��θ�����ʾ������ʾѪ��
			Color c=g.getColor();
			g.setColor(Color.GRAY);
			g.drawRect(x-2,y-6,TANK_WIDTH+4,4);
			int blood_width=(TANK_WIDTH+4)*life/TANK_LIFE;
			g.fillRect(x-2, y-6, blood_width, 4);
			g.setColor(c);
		}
	}
	
	
	
	
}