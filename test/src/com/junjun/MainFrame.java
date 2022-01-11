package com.junjun;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class MainFrame extends JFrame implements KeyListener,ActionListener {
    int[][] datas=new int[4][4];

    int loseFlag=1;

    int score=0;

    //图片资源的标识
    String theme="A-";

    //将item对象提取到成员变量位置，是为了便于其他方法使用
    JMenuItem item1=new JMenuItem("经典");
    JMenuItem item2=new JMenuItem("霓虹");
    JMenuItem item3=new JMenuItem("糖果");

    //用于初始化数据
    public void initData(){
        generatorNum();
        generatorNum();
    }

    //构造方法
    public MainFrame(){
        //初始化窗体
        initFrame();
        //初始化菜单
        initMenu();
        //初始化数据
        initData();
        //绘制界面
        paintView();
        //为窗口添加键盘监听
        this.addKeyListener(this);
        //调用方法，设置窗体可见
        setVisible(true);
    }

    //用于初始化菜单（换肤和关于我们）
    public void initMenu() {
        //1.创建JMenuBar
        JMenuBar menuBar=new JMenuBar();
        //2.创建栏目对象JMenu（换肤，关于我们）
        JMenu menu1=new JMenu("换肤");
        JMenu menu2=new JMenu("关于我们");


        menuBar.add(menu1);
        menuBar.add(menu2);

        menu1.add(item1);
        menu1.add(item2);
        menu1.add(item3);

        item1.addActionListener(this);
        item2.addActionListener(this);
        item3.addActionListener(this);
        //给窗体对象设置菜单
        setJMenuBar(menuBar);

    }

    //此方法用于初始化窗体，所有窗体的相关设置都在这个方法中完成
    public void initFrame() {

        //设置窗口尺寸
        setSize(514, 538);
        //设置窗体居中
        setLocationRelativeTo(null);
        //设置窗体置顶
        setAlwaysOnTop(true);
        //设置关闭模式
        setDefaultCloseOperation(3);
        //设置窗体标题
        setTitle("2048小游戏");
        // 取消默认布局
        setLayout(null);
    }

    //此方法用于绘制游戏界面
    public void paintView(){

        //移除掉界面中内容
        getContentPane().removeAll();

        //显示失败图片
        if (loseFlag==2){
            JLabel loseLabel=new JLabel(new ImageIcon("D:\\学习\\资料-ja vaSE零基础到开发游戏\\资料\\image\\"+theme+"lose.png"));
            loseLabel.setBounds(90,100,334,228);
            getContentPane().add(loseLabel);
        }


        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                JLabel imag=new JLabel(new ImageIcon("D:\\学习\\资料-ja vaSE零基础到开发游戏\\资料\\image\\"+theme+datas[j][i]+".png"));
                imag.setBounds(50+100*i,50+100*j,100,100);
                getContentPane().add(imag);
            }
        }

        JLabel background=new JLabel(new ImageIcon("D:\\学习\\资料-ja vaSE零基础到开发游戏\\资料\\image\\"+theme+"Background.jpg"));
        background.setBounds(40,40,420,420);
        getContentPane().add(background);

        JLabel scoreLabel=new JLabel("得分："+score);
        scoreLabel.setBounds(50,20,100,20);
        getContentPane().add(scoreLabel);

        //刷新界面
        getContentPane().repaint();
    }

    /*
     * keyTyped无法监听到上下左右按键等某些按键，无需关注
     * */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /*键盘被按下所触发的方法，在这个方法中区分出上下左右按键*/
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode=e.getKeyCode();
        if(keyCode==37){
            moveToLeft(1);
            generatorNum();
        }else if(keyCode==38){
            moveToTop(1);
            generatorNum();
        }else if(keyCode==39){
            moveToRight(1);
            generatorNum();
        }else if(keyCode==40){
            moveToBottom(1);
            generatorNum();
        }else {
            return;
        }

        //每次移动的逻辑进行完，都需要调用check方法，检查游戏是否失败的状态
        check();
        paintView();
    }

    //此方法用于数据的上移动
    public void moveToTop(int flag){
        anticlockwise();
        moveToLeft(flag);
        clockwise();
    }

    //此方法用于数据的下移动
    public void moveToBottom(int flag){
        clockwise();
        moveToLeft(flag);
        anticlockwise();
    }

    //此方法用于数据的右移动
    public void moveToRight(int flag) {
        horizontalSwap();
        moveToLeft(flag);
        horizontalSwap();
    }

    //此方法用于数据的左移动
    public void moveToLeft(int flag) {
        for (int i=0;i<datas.length;i++)
        {
            int [] newarr=new int[4];
            int index=0;
            for(int x=0;x<datas[i].length;x++)
            {
                if(datas[i][x]!=0)
                {
                    newarr[index]=datas[i][x];
                    index++;
                }
            }

            datas[i]=newarr;

            for(int x=0;x<3;x++)
            {
                if(datas[i][x]==datas[i][x+1])
                {
                    datas[i][x]*=2;

                    //计算得分
                    if(flag==1){
                        score+=datas[i][x];
                    }

                    for(int j=x+1;j<3;j++)
                    {
                        datas[i][j]=datas[i][j+1];
                    }

                    datas[i][3]=0;
                }
            }
        }

    }

    //用于整合四个移动的判断
    public void check(){
        if(checkLeft()==false&&checkRight()==false&&checkTop()==false&&checkBottom()==false){
            System.out.println("游戏失败");
            loseFlag=2;
        }
    }

    //用于二维数组的拷贝
    public void copyArray(int [][] src,int [][] dest){
        for (int i = 0; i < src.length; i++) {
            for (int j = 0; j < src[i].length; j++) {
                dest[i][j]=src[i][j];
            }
        }
    }

    //判断是否可以左移动
    public boolean checkLeft(){
        //1.创建新数组，用于备份原数组
        int [][] newarr=new int[4][4];
        //2.将原数组数据拷贝到新数组中
        copyArray(datas,newarr);
        //3.调用左移动方法，对原数组进行左移动
        moveToLeft(2);
        //4.使用移动后的数组，和备份数组逐个比较，并使用flag变量记录
        boolean flag=false;
        lo:
        for (int i = 0; i < datas.length; i++) {
            for (int j = 0; j < datas[i].length; j++) {
                if(datas[i][j]!=newarr[i][j]){
                    flag=true;
                    break lo;
                }
            }
        }
        //5.确定信息后，恢复原数组数据（再拷贝一次）
        copyArray(newarr,datas);
        //6.返回结果信息
        return flag;
    }

    //判断是否可以右移动
    public boolean checkRight(){
        int [][] newarr=new int[4][4];
        copyArray(datas,newarr);
        moveToRight(2);
        boolean flag=false;
        lo:
        for (int i = 0; i < datas.length; i++) {
            for (int j = 0; j < datas[i].length; j++) {
                if(datas[i][j]!=newarr[i][j]){
                    flag=true;
                    break lo;
                }
            }
        }
        copyArray(newarr,datas);
        return flag;
    }

    //判断是否可以上移动
    public boolean checkTop(){
        int [][] newarr=new int[4][4];
        copyArray(datas,newarr);
        moveToTop(2);
        boolean flag=false;
        lo:
        for (int i = 0; i < datas.length; i++) {
            for (int j = 0; j < datas[i].length; j++) {
                if(datas[i][j]!=newarr[i][j]){
                    flag=true;
                    break lo;
                }
            }
        }
        copyArray(newarr,datas);
        return flag;
    }

    //判断是否可以下移动
    public boolean checkBottom(){
        int [][] newarr=new int[4][4];
        copyArray(datas,newarr);
        moveToBottom(2);
        boolean flag=false;
        lo:
        for (int i = 0; i < datas.length; i++) {
            for (int j = 0; j < datas[i].length; j++) {
                if(datas[i][j]!=newarr[i][j]){
                    flag=true;
                    break lo;
                }
            }
        }
        copyArray(newarr,datas);
        return flag;
    }

    //此方法用于二维数组的翻转
    public void horizontalSwap(){
        for(int i=0;i<datas.length;i++){
            reverseArray(datas[i]);
        }
    }

    //此方法用于一维数组的翻转
    public void reverseArray(int[] arr){
        for(int start=0,end=arr.length-1;start<end;start++,end--){
            int temp=arr[start];
            arr[start]=arr[end];
            arr[end]=temp;
        }
    }

    //此方法用于顺时针旋转
    public void clockwise(){
        int [][] newarr=new int[4][4];

        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                newarr[j][3-i]=datas[i][j];
            }
        }

        datas=newarr;
    }

    //此方法用于逆时针旋转
    public void anticlockwise(){
        int [][] newarr=new int[4][4];

        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                newarr[3-j][i]=datas[i][j];
            }
        }

        datas=newarr;
    }

    /*键盘被松开时所触发的方法*/
    @Override
    public void keyReleased(KeyEvent e) {

    }

    //用于从空白位置随机产生2号数字块
    public void generatorNum(){
        //1.创建两个数组，准备记录二维数组中空白格子i和j的索引位置
        int [] arri={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        int [] arrj={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        int w=0;
        //2.遍历二维数组，取出每一个元素，判断当前元素是否为空白格式
        for (int i = 0; i < datas.length; i++) {
            for (int j = 0; j < datas[i].length; j++) {
                if(datas[i][j]==0){
                    //3.是0的话，将索引存入创建的两个数组中
                    arri[w]=i;
                    arrj[w]=j;
                    w++;//还具有统计效果
                }
            }
        }
        //如果w不是0，代表数组中还有空白位置，就可以产生新的数字块
        if(w!=0){
            Random r=new Random();
            int index=r.nextInt(w);
            int x=arri[index];
            int y=arrj[index];
            datas[x][y]=2;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==item1){
            System.out.println("换肤为经典");
            theme="A-";
        }else if (e.getSource()==item2){
            System.out.println("换肤为霓虹");
            theme="B-";
        }else if (e.getSource()==item2){
            System.out.println("换肤为糖果");
            theme="C-";
        }

        paintView();
    }
}

