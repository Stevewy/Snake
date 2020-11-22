import javax.swing.*;

/**
 * @author WangYao
 * @date 2019/12/28
 * @function 运行
 */
public class Snake {


    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setBounds(10, 10, 900, 720);//设置画布
        frame.setResizable(false);//是否可以改变窗口大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//X的作用
        frame.add(new Panel());
        frame.setVisible(true);//显示出来
    }
}
