package com.tank;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
/**
 * �����ǳ����������
 * @author ���
 *
 */


public class TankClient extends Frame {
	/**
	 * ��Ϸ������
	 */
	public static final int GAME_WIDTH=800;
	/**
	 * ��Ϸ����߶�
	 */
	public static final int GAME_HEIGHT=600;
    /**
     * ǽ��
     */
	Wall w1=new Wall(150,150,400,20,this);
	Wall w2=new Wall(350,200,15,200,this);
	/**
	 * ��ս̹��
	 */
	Tank myTank=new Tank(400,400,true,Tank.Direction.Stop,this);
	/**
	 * Ѫ��
	 */
	Blood b=new Blood();
	/**
	 * ����ˢ�½�����߳�
	 */
	private PaintThread t=new PaintThread();
	/**
	 * �̴߳��Ĳ�����
	 */
	private boolean thread_live=true;
	/**
	 * ��ŵ��������ڵ�������
	 */
	List<Missile> missiles=new ArrayList<Missile>();
	/**
	 * ������б�ը���������
	 */
	List<Explode> explodes=new ArrayList<Explode>();
	/**
	 * ������л�����̹��(��������)������
	 */
	List<Tank> tanks=new ArrayList<Tank>();
	
	Image offScreenImage =null;
	/**
	 * �ۺ�������Ķ��󣬲����������յ���ʾЧ��
	 */
	public void paint (Graphics g){
		g.drawString("��Ļ�ڵ�����:"+missiles.size(), 10, 50);
		g.drawString("��Ļ��ը����:"+explodes.size(), 10, 70);
		g.drawString("������̹������:"+tanks.size(), 10, 90);
		g.drawString("�ҵ�̹��Ѫ��:"+myTank.getLife(), 10, 110);
		g.drawString("���ӵ���̹�ˣ�Q �����ѷ�̹�ˣ�W  ����Ѫ�飺R ̹�˸��E", 10, 580);
		if(winner()!=null)   //��ʾ��Ϸ���
			g.drawString(winner(),350,500);
		
		for(int i=0;i<tanks.size();i++){
			Tank t=tanks.get(i);
			t.hitWall(w1);
			t.hitWall(w2);
		//	t.hitTanks(tanks);  // ����ս̹����Ŀƫ��ʱ���˷������ý���Ӱ�����
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
	 * ������Ϸ�������ڣ����ø�������������������� ��ʱˢ�½��� ���߳�
	 */
	public void launchFrame(){
		addEnemy();  
		addFriend();
		
		this.setLocation(100,100);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("̹�˴�ս");
		this.addWindowListener(new WindowAdapter(){  //������
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
	 * ˽���ڲ��࣬�߳���
	 * @author Administrator
	 *
	 */
	private class PaintThread implements Runnable{
		
		public void run() { 
			while(thread_live){
				repaint();  //����ÿ0.05s����һ�θ÷������������ص�paint,update����
				try {
					Thread.sleep(50);		//���ûдupdate������ʱ��̫��Ҫ����̫��������
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				}
			}
			
		}
	/**
	 * ˽���ڲ��࣬��д��KeyAdapter�е� keyReleased,keyPressed����
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
	 * ����12���з�̹�ˣ�̹���˶�����Ĭ��ΪDown
	 */
	public void addEnemy(){
		for(int i=0;i<12;i++){
			tanks.add(new Tank(50+50*(i+1),50,false,Tank.Direction.Down,this));
		}
	}
	/**
	 * ����4���ѷ�̹�ˣ�̹���˶�����Ĭ��ΪUp
	 */
	public void addFriend(){
		for(int i=0;i<4;i++){
			tanks.add(new Tank(50+100*(i+1),550,true,Tank.Direction.Up,this));
		}
	}
	/**
	 * ������Ϸ����
	 * @return ���ַ�����ʽ������Ϸ���
	 */
	public String winner(){
		int m=0,n=0;
		for(int i=0;i<tanks.size();i++){
			Tank t=tanks.get(i);
			if(t.isGood()) m++;
			else n++;
		}
		if(m==tanks.size() && myTank.isLive())  return "�ҷ�ʤ����(��Q���ӵз�̹��)";
		else if(n==tanks.size() && !myTank.isLive())  return "�з�ʤ����(��W�����ѷ�̹��)";
		else if(!myTank.isLive()) return "���̹�˹��ˣ���E���";
		else return null;
	}
	
	
}
	
	

