package com.tank;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
/**
 * 此类是程序运行起点
 * @author 吴锋
 *
 */


public class TankClient extends Frame {
	/**
	 * 游戏界面宽度
	 */
	public static final int GAME_WIDTH=800;
	/**
	 * 游戏界面高度
	 */
	public static final int GAME_HEIGHT=600;
    /**
     * 墙体
     */
	Wall w1=new Wall(150,150,400,20,this);
	Wall w2=new Wall(350,200,15,200,this);
	/**
	 * 主战坦克
	 */
	Tank myTank=new Tank(400,400,true,Tank.Direction.Stop,this);
	/**
	 * 血块
	 */
	Blood b=new Blood();
	/**
	 * 负责刷新界面的线程
	 */
	private PaintThread t=new PaintThread();
	/**
	 * 线程存活的布尔量
	 */
	private boolean thread_live=true;
	/**
	 * 存放敌我所有炮弹的容器
	 */
	List<Missile> missiles=new ArrayList<Missile>();
	/**
	 * 存放所有爆炸对象的容器
	 */
	List<Explode> explodes=new ArrayList<Explode>();
	/**
	 * 存放所有机器人坦克(包含敌我)的容器
	 */
	List<Tank> tanks=new ArrayList<Tank>();
	
	Image offScreenImage =null;
	/**
	 * 综合其他类的对象，产生界面最终的显示效果
	 */
	public void paint (Graphics g){
		g.drawString("屏幕炮弹数量:"+missiles.size(), 10, 50);
		g.drawString("屏幕爆炸次数:"+explodes.size(), 10, 70);
		g.drawString("机器人坦克数量:"+tanks.size(), 10, 90);
		g.drawString("我的坦克血量:"+myTank.getLife(), 10, 110);
		g.drawString("增加敌人坦克：Q 增加友方坦克：W  增加血块：R 坦克复活：E", 10, 580);
		if(winner()!=null)   //显示游戏结果
			g.drawString(winner(),350,500);
		
		for(int i=0;i<tanks.size();i++){
			Tank t=tanks.get(i);
			t.hitWall(w1);
			t.hitWall(w2);
		//	t.hitTanks(tanks);  // 当参战坦克数目偏多时，此方法调用将会影响界面
			t.draw(g);
		}	
		for(int i=0;i<missiles.size();i++){
			Missile m=missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}
		for(int i=0;i<explodes.size();i++){
			Explode e=explodes.get(i);
			e.draw(g);
		}
			
		b.draw(g);
		myTank.draw(g);
		myTank.hitWall(w1);
		myTank.hitWall(w2);
	//	myTank.hitTanks(tanks);
		myTank.eat(b);
		
		w1.draw(g);
		w2.draw(g);
	}
	
	public void update(Graphics g){
		if(offScreenImage ==null){
			offScreenImage =this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		Graphics gOffScreen=offScreenImage.getGraphics();
		Color c=gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	/**
	 * 产生游戏的主窗口，设置各类监听器，并负责启动 定时刷新界面 的线程
	 */
	public void launchFrame(){
		addEnemy();  
		addFriend();
		
		this.setLocation(100,100);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("坦克大战");
		this.addWindowListener(new WindowAdapter(){  //匿名类
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		setVisible(true);
		
		this.addKeyListener(new KeyMonitor());
		
		new Thread(t).start();
	}
	
	public static void main(String[] args) {
		TankClient tc=new TankClient();
		tc.launchFrame(); 
	}
	/**
	 * 私有内部类，线程类
	 * @author Administrator
	 *
	 */
	private class PaintThread implements Runnable{
		
		public void run() { 
			while(thread_live){
				repaint();  //设置每0.05s调用一次该方法，会调用相关的paint,update方法
				try {
					Thread.sleep(50);		//如果没写update方法，时间太短要闪，太长不连贯
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				}
			}
			
		}
	/**
	 * 私有内部类，重写了KeyAdapter中的 keyReleased,keyPressed方法
	 * @author Administrator
	 *
	 */
	private class KeyMonitor extends KeyAdapter{
		
		public void keyReleased(KeyEvent e) {
			myTank.keyRealeased(e);
		}

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}		
	}
	/**
	 * 增加12辆敌方坦克，坦克运动方向默认为Down
	 */
	public void addEnemy(){
		for(int i=0;i<12;i++){
			tanks.add(new Tank(50+50*(i+1),50,false,Tank.Direction.Down,this));
		}
	}
	/**
	 * 增加4辆友方坦克，坦克运动方向默认为Up
	 */
	public void addFriend(){
		for(int i=0;i<4;i++){
			tanks.add(new Tank(50+100*(i+1),550,true,Tank.Direction.Up,this));
		}
	}
	/**
	 * 定义游戏规则
	 * @return 以字符串形式返回游戏结果
	 */
	public String winner(){
		int m=0,n=0;
		for(int i=0;i<tanks.size();i++){
			Tank t=tanks.get(i);
			if(t.isGood()) m++;
			else n++;
		}
		if(m==tanks.size() && myTank.isLive())  return "我方胜利！(按Q增加敌方坦克)";
		else if(n==tanks.size() && !myTank.isLive())  return "敌方胜利！(按W增加友方坦克)";
		else if(!myTank.isLive()) return "你的坦克挂了，按E复活！";
		else return null;
	}
	
	
}
	
	

