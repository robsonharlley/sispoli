package br.com.sispoli.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class CadastroAlunos extends JFrame {

	// ── Paleta de cores da Aplicação
	// ──────────────────────────────────────────────────────
	private static final Color BG_APP = new Color(0xF7F6F3);
	private static final Color BG_CARD = Color.WHITE;
	private static final Color ACCENT = new Color(0x1D9E75); // teal-400
	private static final Color ACCENT_DARK = new Color(0x0F6E56); // teal-600
	private static final Color ACCENT_LIGHT = new Color(0xE1F5EE); // teal-50
	private static final Color DANGER = new Color(0xE24B4A);
	private static final Color WARN_BG = new Color(0xFAEEDA);
	private static final Color WARN_FG = new Color(0x854F0B);
	private static final Color TXT_PRIMARY = new Color(0x1A1A18);
	private static final Color TXT_MUTED = new Color(0x5F5E5A);
	private static final Color TXT_HINT = new Color(0x888780);
	private static final Color BORDER = new Color(0xD3D1C7);
	private static final Color BORDER_FOCUS = new Color(0x1D9E75);

	// ── Fontes ───────────────────────────────────────────────────────────────
	private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
	private static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 13);
	private static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 12);
	private static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 13);
	private static final Font FONT_BTN = new Font("Segoe UI", Font.BOLD, 13);
	private static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CadastroAlunos frame = new CadastroAlunos();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CadastroAlunos() {
		super("Cadastro de Alunos");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(820, 600));
		setPreferredSize(new Dimension(960, 760));

		JPanel root = new JPanel(new BorderLayout(0, 0));
		root.setBackground(BG_APP);
		root.add(buidHeader(), BorderLayout.NORTH);
		JScrollPane scrollMain = new JScrollPane(BuildBody());
		scrollMain.setBorder(BorderFactory.createEmptyBorder());
		scrollMain.getViewport().setBackground(BG_APP);
		scrollMain.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		root.add(scrollMain, BorderLayout.CENTER);
		root.add(buildFooter(), BorderLayout.SOUTH);
		setContentPane(root);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

	}

	// ----- Header --------------------------------------------------
	// -----------------------------------------------------------------

	private JPanel buidHeader() {
		JPanel hdr = new JPanel(new BorderLayout());
		hdr.setBackground(ACCENT_DARK);
		hdr.setBorder(new EmptyBorder(18, 28, 18, 28));

		JLabel title = new JLabel("Cadastro de Aluno");
		title.setFont(FONT_TITLE);
		title.setForeground(BG_CARD);

		JLabel sub = new JLabel("Preencha todos os campos obrigatórios(*)");
		sub.setFont(FONT_SMALL);
		sub.setForeground(new Color(0xA0D8C8));

		JPanel left = new JPanel(new GridLayout(2, 1, 0, 2));
		left.setOpaque(false);
		left.add(title);
		left.add(sub);
		hdr.add(left, BorderLayout.WEST);

		
	//----- badge status ---------------------------------
		JLabel bagde = new JLabel(" NOVO ");
		bagde.setFont(FONT_SMALL);
		bagde.setOpaque(true);
		bagde.setBackground(ACCENT_LIGHT);
		bagde.setForeground(ACCENT_DARK);
		bagde.setBorder(new CompoundBorder(new LineBorder(ACCENT, 1, true), new EmptyBorder(4,8,4,8)));
		hdr.add(bagde, BorderLayout.EAST);
		
		return hdr;
	}

	private Component buildFooter() {
		// TODO Auto-generated method stub
		return null;
	}

	private Component BuildBody() {
		// TODO Auto-generated method stub
		return null;
	}

}
