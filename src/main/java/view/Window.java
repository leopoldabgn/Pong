package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import model.Pad;

public class Window extends JFrame
{
	private static final long serialVersionUID = 981946199136785452L;
	public static final Color BACKGROUND_COLOR = new Color(60, 60, 60);

	private Ground ground;
	private Pad leftPad, rightPad;
	private boolean upArrow, botArrow, upZ, downS;

	public Window(int w, int h)
	{
		this.setTitle("Pong");
		this.setSize(w, h);
		this.setMinimumSize(getSize());
		this.setBackground(BACKGROUND_COLOR);
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setDefaultLookAndFeelDecorated(true);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		ground = new Ground(1200, 650);
		leftPad = ground.getLeftPad();
		rightPad = ground.getRightPad();
		
		setupKeyboard();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Code à exécuter lorsque la fenêtre est redimensionnée
				ground.refreshSizes(getSize());
            }
        });

		this.getContentPane().add(ground, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	public void setupKeyboard() {
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
	
				int key = e.getKeyCode();
				switch (key) {
					case KeyEvent.VK_Z:
						upZ = true;
						leftPad.setVelY(-Pad.SPEED);
						break;
					case KeyEvent.VK_S:
						downS = true;
						leftPad.setVelY(Pad.SPEED);
						break;
					case KeyEvent.VK_UP:
						upArrow = true;
						rightPad.setVelY(-Pad.SPEED);
						break;
					case KeyEvent.VK_DOWN:
						botArrow = true;
						rightPad.setVelY(Pad.SPEED);
						break;
					case KeyEvent.VK_ESCAPE:
						Window.this.dispose();
						System.exit(0);
					default:
						break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
	
				int key = e.getKeyCode();
				switch (key) {
					case KeyEvent.VK_Z:
						upZ = false;
						leftPad.setVelY(0);
						break;
					case KeyEvent.VK_S:
						downS = false;
						leftPad.setVelY(0);
						break;
					case KeyEvent.VK_UP:
						upArrow = false;
						rightPad.setVelY(0);
						break;
					case KeyEvent.VK_DOWN:
						botArrow = false;
						rightPad.setVelY(0);
						break;
					default:
						break;
				}


				if(upZ && !downS) {
					leftPad.setVelY(-Pad.SPEED);
				}
				else if(!upZ && downS) {
					leftPad.setVelY(Pad.SPEED);
				}
				else if(!upZ && !downS) {
					leftPad.setVelY(0);
				}

				if(upArrow && !botArrow) {
					rightPad.setVelY(-Pad.SPEED);
				}
				else if(!upArrow && botArrow) {
					rightPad.setVelY(Pad.SPEED);
				}
				else if(!upArrow && !botArrow) {
					rightPad.setVelY(0);
				}

			}

		});
	}
	

}
