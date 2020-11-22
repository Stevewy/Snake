import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * @author WangYao
 * @date 2019/12/28
 * @function 画蛇
 */
public class Panel extends JPanel implements KeyListener, ActionListener {
    //先导入图片
    private ImageIcon title;
    private ImageIcon body;
    private ImageIcon up;
    private ImageIcon down;
    private ImageIcon left;
    private ImageIcon right;
    private ImageIcon food;

    //蛇的一些数据
    private int len = 4;//初始为3,加一个确保可以吃食物
    private int[] snakex = new int[817];//最多816个
    private int[] snakey = new int[817];
    private String fx = "R";//U D L R 上下左右
    private boolean isStarted = false;
    private boolean isFailed = false;
    private int delay = 200;
    private Timer timer = new Timer(delay, this);//闹钟,定时叫自己
    private int score;
    //食物
    private int foodx;
    private int foody;
    private Random rand = new Random();

    private void init(){
        len = 4;
        score = 0;
        fx = "R";
        delay = 200;
        snakex[0] = 100;
        snakey[0] = 100;
        snakex[1] = 75;
        snakey[1] = 100;
        snakex[2] = 50;
        snakey[2] = 100;
        foodx = 25 + 25 * rand.nextInt(34);
        foody = 75 + 25 * rand.nextInt(24);
    }

    public Panel(){
        loadBGM();
        loadImageIcon();
        init();
        this.setFocusable(true);
        this.addKeyListener(this);//获取键盘信息
        timer.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        title.paintIcon(this, g, 25, 11);//g是Graphics
        g.fillRect(25, 75, 850, 600);//黑布
        g.setColor(Color.WHITE);
        g.drawString("len:  " + (len - 1), 750, 35);
        g.drawString("Score:  " + score, 750, 50);

        if(!isStarted){
            g.setColor(Color.WHITE);
            g.setFont(new Font("arial", Font.BOLD, 40));//类型,粗体,大小
            g.drawString("Press Space to Start", 300, 300);
        }
        if(isFailed){
            g.setColor(Color.RED);
            g.setFont(new Font("arial", Font.BOLD, 40));//类型,粗体,大小
            g.drawString("Failed: Press Space to Restart", 200, 300);
        }

        switch (fx){
            case "R":
                right.paintIcon(this, g, snakex[0], snakey[0]);
                break;
            case "L":
                left.paintIcon(this, g, snakex[0], snakey[0]);
                break;
            case "U":
                up.paintIcon(this, g, snakex[0], snakey[0]);
                break;
            case "D":
                down.paintIcon(this, g, snakex[0], snakey[0]);
                break;
        }

        for(int i = 1; i < len - 1; i++){
            body.paintIcon(this, g, snakex[i], snakey[i]);
        }
        food.paintIcon(this, g, foodx, foody);
    }

    @Override
    public void keyTyped(KeyEvent e){}

    @Override
    public void keyPressed(KeyEvent e){
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_SPACE){
            if(isFailed){
                isFailed = false;
                init();
            }
            else
                isStarted = !isStarted;
            if(isStarted)
                playBGM();
            else
                stopBGM();
            repaint();
        }else if (keyCode == KeyEvent.VK_LEFT){
            if(snakex[1] != snakex[0] - 25 && isStarted)
                fx = "L";
        }else if (keyCode == KeyEvent.VK_RIGHT){
            if(snakex[1] != snakex[0] + 25 && isStarted)
                fx = "R";
        }else if (keyCode == KeyEvent.VK_UP){
            if(snakey[1] != snakey[0] - 25 && isStarted)
                fx = "U";
        }else if (keyCode == KeyEvent.VK_DOWN){
            if(snakey[1] != snakey[0] + 25 && isStarted)
                fx = "D";
        }
    }
    @Override
    public void keyReleased(KeyEvent e){}
    @Override
    public void actionPerformed(ActionEvent e) {
        if(isStarted && !isFailed){
            //从后向前处理
            for(int i = len - 1; i > 0; i--){
                snakex[i] = snakex[i-1];
                snakey[i] = snakey[i-1];
            }
            //处理头
            switch (fx){
                case "R":
                    snakex[0] = snakex[0] + 25;
                    if(snakex[0] > 850){
                        snakex[0] = 850;
                        isFailed = true;
                    }
                    break;
                case "L":
                    snakex[0] = snakex[0] - 25;
                    if(snakex[0] < 25){
                        snakex[0] = 25;
                        isFailed = true;
                    }
                    break;
                case "U":
                    snakey[0] = snakey[0] - 25;
                    if(snakey[0] < 75){
                        snakey[0] = 75;
                        isFailed = true;
                    }
                    break;
                case "D":
                    snakey[0] = snakey[0] + 25;
                    if(snakey[0] > 650){
                        snakey[0] = 650;
                        isFailed = true;
                    }
                    break;
            }

            if(snakex[0] == foodx && snakey[0] == foody){
                len++;
                if(delay > 50)
                    delay -= 10;
                score += len;
                foodx = 25 + 25 * rand.nextInt(34);
                foody = 75 + 25 * rand.nextInt(24);
                for(int i = 0; i < len; i++)
                    if(snakex[i] == foodx && snakey[i] == foody){
                        foodx = 25 + 25 * rand.nextInt(34);
                        foody = 75 + 25 * rand.nextInt(24);
                        i = -1;
                    }
            }

            for(int i = 1; i < len - 1; i++)
                if(snakex[i] == snakex[0] && snakey[i] == snakey[0])
                    isFailed = true;
            repaint();
        }
        timer.start();
    }

    private Clip bgm;
    private void loadBGM(){//载入BGM
        try {
            bgm = AudioSystem.getClip();//打开音乐
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("music/snake.wav");//得到流
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(in));
            bgm.open(ais);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }catch (IOException e) {
            System.out.println("音乐出了问题");
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    private void playBGM() {
        bgm.loop(Clip.LOOP_CONTINUOUSLY);
    }

    private void stopBGM() {
        bgm.stop();
    }

    private void loadImageIcon() {//载入图片
        InputStream in;
        try {
            in = this.getClass().getClassLoader().getResourceAsStream("title.jpg");
            title = new ImageIcon(ImageIO.read(in));
            in = this.getClass().getClassLoader().getResourceAsStream("body.png");
            body = new ImageIcon(ImageIO.read(in));
            in = this.getClass().getClassLoader().getResourceAsStream("up.png");
            up = new ImageIcon(ImageIO.read(in));
            in = this.getClass().getClassLoader().getResourceAsStream("down.png");
            down = new ImageIcon(ImageIO.read(in));
            in = this.getClass().getClassLoader().getResourceAsStream("left.png");
            left = new ImageIcon(ImageIO.read(in));
            in = this.getClass().getClassLoader().getResourceAsStream("right.png");
            right = new ImageIcon(ImageIO.read(in));
            in = this.getClass().getClassLoader().getResourceAsStream("food.png");
            food = new ImageIcon(ImageIO.read(in));
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}