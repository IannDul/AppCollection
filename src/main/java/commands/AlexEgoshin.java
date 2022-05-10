package commands;

import commands.dependencies.Instances;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Класс, предназначенный для <i>секретной команды</i> от Дуль Я. С. и Коротина А. М.
 */
public final class AlexEgoshin extends Command {
    public AlexEgoshin(List<String> args) {
        super(args, 0);
    }

    @Override
    public int execute(Instances instances) {
        class AlexEgoshinPostavMaxBall extends JFrame {

            public AlexEgoshinPostavMaxBall() {

                this.getContentPane().setLayout(new FlowLayout());
                try {
                    JLabel label1 = new JLabel("Поставь максималочку,пожалуйста");
                    ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("Privet_Alex.png"));

                    JLabel label2 = new JLabel(icon);

                    add(label1);
                    add(label2);
                }
                catch (NullPointerException e){
                    instances.outPutter.output("Неверно указан путь до файла");
                }
            }

            public void createAndShowGUI() {
                JFrame frame = new AlexEgoshinPostavMaxBall();
                frame.pack();
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            }
        }
        AlexEgoshinPostavMaxBall alexEgoshinPostavMaxBall = new AlexEgoshinPostavMaxBall();
        alexEgoshinPostavMaxBall.createAndShowGUI();
        instances.outPutter.output("Команда успешно выполнена -> надо максимальный балл для Алексея Коротина и Дуль Яна");
        return 0;
    }

}