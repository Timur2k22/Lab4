package ru.timur.lab4;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import ru.timur.lab4.manipulationWithData.DataManipulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GUI extends JFrame {
    private DataManipulation dm = null;
    private final JPanel panelTop = new JPanel();
    private final JButton importButton = new JButton("Import");
    private final JButton exportButton = new JButton("export");
    private final JButton exit = new JButton("exit");
    private final JTextField fileName = new JTextField();

    public GUI() {
        setUpWindow();
        setUpButtons();
        setUpFileName();
        setUpPanel();
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.setVisible(true);
    }

    private void doExport(ActionEvent event) {
        String pathForSave = fileName.getText();
        if (dm == null) {
            JOptionPane.showMessageDialog(null, "вы еще не загрузили данные", "Ok", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        File file = new File(pathForSave);
        if (!file.isAbsolute()) {
            JOptionPane.showMessageDialog(null, "Вы указали некорректный путь", "Ok", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            dm.exportData(pathForSave);
            System.out.println("все сохранилось");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Вы неверно указали путь для сохранения, либо файл открыт", "Ok", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Произошла ошибка при записи", "Ok", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void doImport(ActionEvent event) {
        try {
            dm = new DataManipulation();
            JFileChooser fileChooser =
                    new JFileChooser();
            int ret = fileChooser.showDialog(null, "Choose file");
            if (ret != JFileChooser.APPROVE_OPTION) {
                return;
            }
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            dm.loadData(path);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Произошла ошибка при записи файла", "Ok", JOptionPane.INFORMATION_MESSAGE);
        } catch (InvalidFormatException e) {
            JOptionPane.showMessageDialog(null, "Указан неверный формат файла", "Ok", JOptionPane.INFORMATION_MESSAGE);
        } catch (InvalidOperationException e) {
            JOptionPane.showMessageDialog(null, "Системе не удается найти указанный путь", "Ok", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void setUpButtons() {
        importButton.setAlignmentX(CENTER_ALIGNMENT);
        exportButton.setAlignmentX(CENTER_ALIGNMENT);
        exit.setAlignmentX(CENTER_ALIGNMENT);
        importButton.addActionListener(this::doImport);
        exportButton.addActionListener(this::doExport);
        exit.addActionListener(e -> System.exit(0));
    }

    private void setUpPanel() {
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
        panelTop.add(fileName);
        panelTop.add(importButton);
        panelTop.add(exportButton);
        panelTop.add(exit);
    }

    private void setUpWindow() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 200);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.add(panelTop, BorderLayout.NORTH);
    }

    private void setUpFileName() {
        fileName.setText("введите путь для сохранения данных");
        fileName.setForeground(Color.GRAY);
        fileName.setBackground(Color.WHITE);
        fileName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // при получении фокуса удаляем текст на фоне
                if ("введите путь для сохранения данных".equals(fileName.getText())) {
                    fileName.setText("");
                    fileName.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // при потере фокуса возвращаем текст на фон
                if ("".equals(fileName.getText())) {
                    fileName.setText("введите путь для сохранения данных");
                    fileName.setForeground(Color.GRAY);
                }
            }
        });
    }
}
