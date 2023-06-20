package com.example.staratlas;

import com.example.staratlas.dao.RGB;
import com.example.staratlas.dao.Star;
import com.example.staratlas.dao.ISS;
import com.example.staratlas.repo.StarService;
import com.example.staratlas.ui.Camera;
import com.example.staratlas.ui.StarMapRenderer;
import com.example.staratlas.Config;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main extends JFrame implements MouseMotionListener, MouseWheelListener, MouseListener {
    private static int WIDTH = Config.WIDTH;
    private static int HEIGHT = Config.HEIGHT;
    public Star focus;
    public boolean focused, reading;
    public long time, dt;
    public double full_dist;
    public static final String LONG_TEXT = "";
    public StarService starService = new StarService();
    public final Camera camera;
    public JLabel label;
    public JScrollPane scroll;
    public int lastX;
    public int lastY;
    public RGB rgb = new RGB();
    private final ISS iss = new ISS(0, 0);
    public Main() {
        Config.load("data\\config.txt");
        WIDTH = Config.WIDTH;
        HEIGHT = Config.HEIGHT;
        lastX = lastY = 0;

        // Create stars
        Star[] stars = createStars();

        // Create the StarRenderer and Camera
        camera = new Camera();


        // Set up the JPanel for rendering the stars
        StarMapRenderer panel = new StarMapRenderer(camera, stars, this);
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setBackground(Color.BLACK);
        panel.setLayout(new BorderLayout());
        label = new JLabel(LONG_TEXT, JLabel.LEFT);
        label.setVerticalAlignment(JLabel.TOP);
        scroll = new JScrollPane(label, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.createVerticalScrollBar();
        scroll.setPreferredSize(new Dimension(250, HEIGHT - 50));
        label.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(scroll, BorderLayout.EAST);

        // Add the panel to the JFrame
        getContentPane().add(panel);


        ActionListener updateClockAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                long t = System.currentTimeMillis();
                double v = 0.002 * full_dist;
                dt = t - time;
                time = t;
                if (reading) {
                    double pitch_dec = focus.getDeclination() + camera.getPitch();
                    double yaw_dec = (-focus.getRightAscension() - camera.getYaw()) % 360;
                    double fov_dec = (60 - camera.getFieldOfView());
                    if (yaw_dec > 180) {
                        yaw_dec -= 360;
                    }
                    if (yaw_dec < -180) {
                        yaw_dec += 360;
                    }
                    double full_dec = Math.hypot(Math.hypot(pitch_dec, yaw_dec), fov_dec);
                    double pitch_mul = Math.abs(pitch_dec / full_dec);
                    double yaw_mul = Math.abs(yaw_dec / full_dec);
                    double fov_mul = Math.abs(fov_dec / full_dec);
                    if (Math.abs(pitch_dec) > dt * v * pitch_mul) {
                        pitch_dec = dt * v * pitch_mul * Math.signum(pitch_dec);
                    }
                    if (Math.abs(fov_dec) > dt * v * fov_mul) {
                        fov_dec = dt * v * fov_mul * Math.signum(fov_dec);
                    }
                    if (Math.abs(yaw_dec) > dt * v * yaw_mul) {
                        yaw_dec = dt * v * yaw_mul * Math.signum(yaw_dec);
                    }
                    int max = Math.max(getHeight(), getWidth());
                    camera.setFieldOfView(camera.getFieldOfView() + fov_dec);
                    camera.rotate(yaw_dec, pitch_dec, camera.getFieldOfView());
                }
                panel.repaint();
            }
        };


        // Add mouse motion listener to the panel to rotate the camera
        panel.addMouseMotionListener(this);
        panel.addMouseWheelListener(this);
        panel.addMouseListener(this);
        Timer t = new Timer(1000 / 60, updateClockAction);
        t.start();
        // Set up the JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("StarAtlas");
        //setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        this.scroll.setVisible(false);

        // Создание таймера для обновления карты каждую секунду
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Получение данных о текущей позиции МКС из API или другого источника данных
                // Например, можно использовать API OpenNotify (http://open-notify.org/Open-Notify-API/ISS-Location-Now/)
                // для получения текущих координат МКС в формате JSON
                try {
                    URL url = new URL("http://api.open-notify.org/iss-now.json");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line;
                        StringBuilder response = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // Разбор ответа JSON и извлечение координат МКС без использования библиотеки JSON
                        String responseString = response.toString();
                        int latIndex = responseString.indexOf("\"latitude\": \"") + "\"latitude\": \"".length();
                        int latEndIndex = responseString.indexOf("\"", latIndex);
                        double latitude = Double.parseDouble(responseString.substring(latIndex, latEndIndex).replace("\"",""));

                        int lonIndex = responseString.indexOf("\"longitude\": \"") + "\"longitude\": \"".length();
                        int lonEndIndex = responseString.indexOf("\"", lonIndex);
                        double longitude = Double.parseDouble(responseString.substring(lonIndex, lonEndIndex).replace("\"",""));

                        // Обновление позиции МКС на карте
                        double gmst = ISS.GMST();
                        System.out.println(longitude+" "+gmst);
                        iss.setRightAscension(-longitude-gmst);
                        iss.setDeclination(latitude);
                        System.out.println(iss.getRightAscension()+" "+iss.getDeclination());
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                // Перерисовка карты для отображения обновленных позиций
                panel.repaint();
            }
        });

        // Запуск таймера
        timer.start();


    };



    // Create an array of num random stars
    private Star[] createStars() {
        Star[] stars = new Star[starService.getNumberOfStars() + 1];
        for (int i = 0; i < stars.length - 1; i++) {
            Color color = rgb.calc_color(starService, i);
            stars[i] = starService.getAllStars().get(i);
            stars[i].setColor(color);
        }
        // Добавление МКС в массив звезд
        stars[stars.length - 1] = iss;
        return stars;
    }
    // MouseMotionListener interface implementation
    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = e.getX();
        int dy = e.getY();
        int max = Math.max(getHeight(), getWidth());
        if(!reading)
            camera.rotate(dx-lastX, dy-lastY, max);
        lastX=dx;
        lastY=dy;
        //e.getComponent().repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int dx = e.getX();
        int dy = e.getY();
        lastX=dx;
        lastY=dy;

        //e.getComponent().repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!reading) {
            if (focused) {
                reading=true;
                scroll.setVisible(true);
                label.setText("<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><table><tr><td width='225'>" + starService.getStarInfo(focus) + "</td></tr></table></meta></html>");
                double pitch_dec = focus.getDeclination()+camera.getPitch();
                double yaw_dec = (-focus.getRightAscension()-camera.getYaw())%360;
                double fov_dec = (60-camera.getFieldOfView());
                if(yaw_dec>180){
                    yaw_dec-=360;
                }
                if(yaw_dec<-180){
                    yaw_dec+=360;
                }
                full_dist=Math.hypot(Math.hypot(pitch_dec, yaw_dec), fov_dec);
            }
        }
        else {
            reading=false;
            scroll.setVisible(false);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    // Main method to run the program
    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int dx = e.getWheelRotation();
        if (!reading)
            camera.setFieldOfView(camera.getFieldOfView()+10*dx);
    }
}