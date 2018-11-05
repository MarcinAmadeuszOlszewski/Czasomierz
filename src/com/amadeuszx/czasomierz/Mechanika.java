package com.amadeuszx.czasomierz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

class Mechanika extends JFrame
{
	private Dimension pulpit = Toolkit.getDefaultToolkit().getScreenSize();
	private Dimension okienko = new Dimension(110, 15);
	private JCheckBox wlacznik;
	private JRadioButton pauza;
	private JLabel wyswietlacz;
	private MojPanel wyswietlaczIkona;
	private BufferedImage play, pausa, poczatek;
	private boolean on = false;
	private long czas = 0;
	private long czasT = 0;
	private Timer aktualizacja;

	Mechanika()
	{

		try
		{
			play = ImageIO.read(getClass().getResource("/play.png"));
			pausa = ImageIO.read(getClass().getResource("/pauza.png"));
			poczatek = ImageIO.read(getClass().getResource("/poczatek.png"));
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

		aktualizacja = new Timer();
		aktualizacja.schedule(akt, 0, 1_000);

		setLayout(null);
		wlacznik = new JCheckBox();
		wlacznik.setBackground(Color.GRAY);
		wlacznik.setSelected(false);
		wlacznik.setBounds(okienko.width - 20, 0, 20, okienko.height);
		wlacznik.setToolTipText("Start/Stop");
		wlacznik.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (!wlacznik.isSelected())
				{

					on = false;
					if (pauza.isSelected())
					{
						wyswietlacz.setText(wyswietlacz.getText());
						wyswietlaczIkona.aktualizacja(poczatek);
						pauza.setSelected(false);
					}
					else
					{
						wyswietl(poczatek);
					}
				}
				else
				{
					czas = System.currentTimeMillis();
					czasT = 0;
					on = true;
				}

			}
		});
		add(wlacznik);

		pauza = new JRadioButton();
		pauza.setToolTipText("Pauza");
		pauza.setBounds(0, 0, 20, okienko.height);
		pauza.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (wlacznik.isSelected())
				{

					if (pauza.isSelected())
					{
						wyswietl(pausa);
						czasT += System.currentTimeMillis() - czas;
						on = false;
					}
					else
					{
						czas = System.currentTimeMillis();
						on = true;
					}
				}
				else
				{
					pauza.setSelected(false);
				}
			}
		});
		add(pauza);

		wyswietlaczIkona = new MojPanel();
		wyswietlaczIkona.aktualizacja(poczatek);
		wyswietlaczIkona.setBounds(19, 0, 8, okienko.height);
		add(wyswietlaczIkona);

		wyswietlacz = new JLabel("00:00");
		wyswietlacz.setToolTipText("Czas");
		wyswietlacz.setBounds(37, 0, okienko.width - 42, okienko.height);
		add(wyswietlacz);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Czas");
		setLocation(pulpit.width - okienko.width, pulpit.height - okienko.height - 30);
		setSize(okienko);
		setAlwaysOnTop(true);
		setUndecorated(true);
		setVisible(true);
	}

	TimerTask akt = new TimerTask()
	{
		@Override
		public void run()
		{
			if (on)
			{
				wyswietl(play);
			}
		}
	};

	void wyswietl(BufferedImage play2)
	{
		long pomiar = ((System.currentTimeMillis() - czas) + czasT);
		wyswietlaczIkona.aktualizacja(play2);
		wyswietlacz.setText(String.format("%02d:%02d", pomiar / 60_000, (pomiar % 60_000) / 1_000));
		// String.format("%d min, %d sec",
		// TimeUnit.MILLISECONDS.toSeconds(length)/60,
		// TimeUnit.MILLISECONDS.toSeconds(length) % 60 ); //z internetu
	}

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				new Mechanika();
			}
		});

	}

	class MojPanel extends JPanel
	{
		private BufferedImage img;

		public void aktualizacja(BufferedImage img)
		{
			this.img = img;
			repaint();
		}

		@Override
		protected void paintComponent(Graphics arg0)
		{
			arg0.clearRect(0, 0, getWidth(), getHeight());
			arg0.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		}

	}
}
